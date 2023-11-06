package com.silvestre.erp_mobile;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.silvestre.erp_mobile.adaptadores.adapter_letras_registro;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.interfaz.DataUpdateListener;
import com.silvestre.erp_mobile.model.letraEmitidaResumen;
import com.silvestre.erp_mobile.model.letraEmitida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_letras_registro extends Fragment implements DataUpdateListener {

    private RecyclerView recyclerView;
    private ImageButton btnAbrirFormulario;
    private DataUpdateListener dataUpdateListener;
    RecyclerView.Adapter adapter;
    private List<letraEmitida> data = new ArrayList<>();
    private Button btnAgregarImagen, btnRegistrar;
    private Uri photoURI;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0;
    private String mCurrentPhotoPath, base64Image;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    Bitmap bmp = null;
    ImageView imgFotografia;

    private RequestQueue queue;

    private TextInputLayout textInputLayoutComentario;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_letras_registro, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imgFotografia = view.findViewById(R.id.imagenScreen);
        btnAbrirFormulario = view.findViewById(R.id.btnAbrirFormulario);
        btnAgregarImagen = view.findViewById(R.id.btnAgregarImagen);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        queue = Volley.newRequestQueue(getActivity());
        TextView textViewEmpty = view.findViewById(R.id.textViewEmpty);
        textInputLayoutComentario = view.findViewById(R.id.editTextComentario);

        if (data.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            // Si hay datos, muestra el RecyclerView y oculta el TextView "Vacío"
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        }

        adapter = new adapter_letras_registro(data, getContext());
        recyclerView.setAdapter(adapter);
        btnAbrirFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrirFormulario("314284");
                scanCode();
            }
        });

        btnAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        // Explicar por qué se necesitan los permisos aquí
                    }

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 225);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.isEmpty()) {
                    // El RecyclerView está vacío, muestra un mensaje o realiza la acción deseada.
                    Toast.makeText(getContext(), "No hay datos para registrar", Toast.LENGTH_SHORT).show();
                } else {
                    // El RecyclerView tiene al menos 1 elemento, realiza la acción de registro.
                    registraLetraEmitida();
                }
            }
        });
        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    photoURI = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    //Uri photoURI = FileProvider.getUriForFile(AddActivity.this, "com.example.android.fileprovider", photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
                }
            }
        } catch (Exception ex) {
            String q = ex.getMessage();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyApp", Integer.toString(requestCode));
        try {

            if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                    bmp = bitmap;
                    imgFotografia.setImageBitmap(bitmap);
                    base64Image = convertBitmapToBase64(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            Log.d("error", ex.getMessage());
        }
    }


    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volunme up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        abrirFormulario(result.getContents());
        /*if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }*/
    });

    private void abrirFormulario(String codigo) {

        Bundle bundle = new Bundle();
        bundle.putString("id_letra", codigo);

        fragment_letras_formulario formularioFragment = new fragment_letras_formulario();
        formularioFragment.setDataUpdateListener(this);
        formularioFragment.setArguments(bundle);
        formularioFragment.setTargetFragment(this, 0);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.escenario, formularioFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataUpdated(Integer Id_Letra, String NroLetra, Integer NroCuota, Integer
            Id_Financiamiento, String Id_Aceptante, String Importe, String Emision, String Vcmto) {
        letraEmitida nuevoObjeto = new letraEmitida(0, Id_Letra, NroLetra, NroCuota, Id_Financiamiento, Id_Aceptante, Importe, Emision, Vcmto, true, false, "", "");

        // Agregar el objeto a la lista de objetos
        data.add(nuevoObjeto);

        // Notificar al adaptador que se han realizado cambios en los datos
        adapter.notifyDataSetChanged();

        Log.d("MiApp", "Nuevo objeto agregado: Nombre=" + Id_Letra + ", Apellido=" + NroCuota + ", Edad=" + Id_Financiamiento);
    }

    /*final letraEmitida data, final VolleyCallback callback*/
    public void registraLetraEmitida() {
        String url = "https://desarrollo.gruposilvestre.com.pe:443/SilvestreCRMPro/api/APP/APP_RegistrarLetras"; // Reemplaza con la URL de tu servidor

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("IdEmpresa", "1");
            jsonBody.put("Id_Vendedor", "398");
            jsonBody.put("Usuario", "Andy Vera");
            jsonBody.put("Latitud", 12.345);
            jsonBody.put("Longitud", 67.890);
            jsonBody.put("Observacion", textInputLayoutComentario.getEditText().getText().toString());
            jsonBody.put("FechaRegistro", LocalDate.now());
            //jsonBody.put("Fotografia", base64Image);
            jsonBody.put("Fotografia", "image/jpg");

            JSONArray lstDetalleArray = new JSONArray();

            // Iterar a través de los elementos del RecyclerView
            for (letraEmitida elemento : data) {
                try {
                    // Crear un objeto JSON para cada detalle
                    JSONObject detalle = new JSONObject();
                    detalle.put("Id_Letra", elemento.getIdletra());

                    // Agregar el detalle al JSONArray
                    lstDetalleArray.put(detalle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Agregar el JSONArray de detalles al JSON principal
            jsonBody.put("lstDetalle", lstDetalleArray);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Integer status = response.getInt("status");
                                String message = response.getString("message");
                                Log.d("MyApp", message);
                                showSuccessDialog(status, message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Maneja errores aquí
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + variables_globales.Token);
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void showSuccessDialog(int status, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Resultado");
        builder.setMessage(message);

        if (status == 400) {
            builder.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    redirectToAnotherFragment();
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void redirectToAnotherFragment() {
        // Reemplaza "YourFragmentClass" con la clase del fragment al que deseas redirigir
        fragment_letras_historial fragment = new fragment_letras_historial();


        // Agrega el nuevo fragment al gestor de fragmentos
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.escenario, fragment)
                .addToBackStack(null)
                .commit();
    }


}
