package com.silvestre.erp_mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.silvestre.erp_mobile.global.variables_globales;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class fragment_registroqr extends Fragment {
    private RequestQueue queue;
    ImageView imgFotografia;

    ImageButton btnQR,btnEliminarFotografia, btnTomarFoto;
    Button btnGuardar;
    EditText txtNroPedido,txtCliente,txtNroGuia,txtObservacion;
    String _RucEmpresa,_NombreEmpresa,_CodigoEmpresa,_NombreCliente;
    Bitmap bmp=null;
    ProgressDialog progressDialog = null;
    String currentPhotoPath;
    private String mCurrentPhotoPath;
    public static final int MY_DEFAULT_TIMEOUT = 15000;

    private String strqr;
    //private static final String REQUEST_CODE_QR_SCAN = "101";
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int CAMERA_REQUEST = 1888;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoURI;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;


    public fragment_registroqr() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registroqr, container, false);
        imgFotografia= vista.findViewById(R.id.registroqr_Imagen);
        btnGuardar=vista.findViewById(R.id.registroqr_btnGuardar);
        btnEliminarFotografia=vista.findViewById(R.id.registroqr_btnEliminarFoto);
        btnQR=vista.findViewById(R.id.registroqr_btncamara);
        btnTomarFoto=vista.findViewById(R.id.registroqr_btnTomarFoto);

        txtNroPedido=vista.findViewById(R.id.registroqr_NroPedido);
        txtCliente=vista.findViewById(R.id.registroqr_Cliente);
        txtNroGuia=vista.findViewById(R.id.registroqr_Guia);
        txtObservacion=vista.findViewById(R.id.registroqr_Observacion);
        txtNroPedido.setInputType(InputType.TYPE_NULL);
        txtNroGuia.setInputType(InputType.TYPE_NULL);
        txtCliente.setInputType(InputType.TYPE_NULL);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarDialogo();
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                225);
                    }


                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {

                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                226);
                    }
                } else {
                    dispatchTakePictureIntent();
                }

            }
        });

        btnEliminarFotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotografia.setImageDrawable(null);
            }
        });


        //handleSSLHandshake();




        return vista;
    }

    private void copyInputStreamToFile( InputStream in, OutputStream file ) {
        try {
            OutputStream out = file;
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Enables https connections
     */

    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
    private void MostrarDialogo(){
        //String IdVendedor="46119959";
        if(txtNroPedido.getText().toString().equals("") || isNumeric(txtNroPedido.getText().toString())==false){
            final AlertDialog.Builder alerDialog = new AlertDialog.Builder (getContext(),R.style.AlerDialogEstiloERP);
            alerDialog.setTitle("Registro de Etiquetas");
            alerDialog.setMessage("No se ha registrado una etiqueta valida");
            alerDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;

                }
            });
            alerDialog.show();
        }
        else {
            GuardarRegistro();
        }
    }
    private void GuardarRegistro()
    {
        String imageString;




        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) imgFotografia.getDrawable();
        if(drawable==null){
            imageString="";
        }
        else {
            Bitmap bitmap = drawable.getBitmap();
            bitmap =bmp;
            bitmap=getResizedBitmap(bitmap, 800);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }


        strqr="{ 'IdEmpresa': '" + (_CodigoEmpresa.equals("S") ? "1" : (_CodigoEmpresa.equals("N") ? "2" : "5")) + "'" +
                ",'IdVendedor': '" + variables_globales.LOGIN_NRODNI + "'" +
                ",'RucEmpresa': '" + (_CodigoEmpresa.equals("S") ? "20191503482" : (_CodigoEmpresa.equals("N") ? "20509089923" : "20607720992"))  + "'" +
                ",'NombreEmpresa': '" + (_CodigoEmpresa.equals("S") ? "SILVESTRE" : (_CodigoEmpresa.equals("N") ? "NEOAGRUM" : "CLENVI")) + "'" +
                ",'NroPedido': '" + txtNroPedido.getText().toString() + "'" +
                ",'NombreCliente': '" + _NombreCliente + "'" +
                ",'NroGuia': '" + txtNroGuia.getText().toString() + "'" +
                ",'Latitud': " + "0.00" + "" +
                ",'Longitud': " + "0.00" + "" +
                ",'Fotografia': '" + imageString + "'" +
                ",'ModeloCelular': '" + "" + "'" +
                ",'Token': '" + "" + "'" +
                ",'Observaciones': '" + txtObservacion.getText().toString() + "'" +
                "}";
        //JsonPedido=gson.toJson(qr);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = variables_globales.URL_RegistrarQR;

        try{
            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("https://wsdynamics.gruposilvestre.com.pe:443", session);
                }
            };

            // Tell the URLConnection to use our HostnameVerifier
            URL url2 = new URL(url);
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url2.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
            InputStream in = urlConnection.getInputStream();
            copyInputStreamToFile(in, System.out);
        }catch (Exception ex){

        }
        queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //JSONObject jsonObject= new JSONObject(response);
                            //{"d":"1~Se registro la Trama de Planta Correctamente."}
                            response=response.replace("\"","").replace("d:","").replace("{","").replace("}","");


                            String[] Respuesta= response.split("~");
                            Log.d("RESPUESTA",response.toString());
                            progressDialog.dismiss();
                            if(Respuesta[0].equals("1") || Respuesta[0].equals("2") || Respuesta[0].equals(3)){

                                Toast.makeText(getActivity(),"Se registro correctamente la etiqueta",Toast.LENGTH_LONG).show();
                                Limpiar();

                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Se registro correctamente la etiqueta",Toast.LENGTH_LONG).show();
                                //Toast.makeText(getActivity(),Respuesta[1].toString(),Toast.LENGTH_LONG).show();
                                Limpiar();
                            }

                        } catch (Exception e) {

                            Toast.makeText(getActivity(),"ERROR AL REGISTRAR",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        finally {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Se registro correctamente la etiqueta",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"No se logro registrar la etiqueta",Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //progressDialog.dismiss();
                    return strqr==null ? null : strqr.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };
        //queue = Volley.newRequestQueue(getActivity() );
        //jsonObjReq.setTag("PEDIDO");
        //queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            MY_DEFAULT_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try{

            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this.getContext(), "No se pudo obtener una respuesta", Toast.LENGTH_SHORT).show();
                String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
                if (resultado != null) {
                    Toast.makeText(this.getContext(), "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (requestCode == REQUEST_CODE_QR_SCAN) {
                if (data != null) {
                    String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                    //Toast.makeText(this.getContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();
                    String[] ArrayLectura  = lectura.split("\\|");
                    _CodigoEmpresa=ArrayLectura[0];
                    _RucEmpresa=ArrayLectura[1];
                    _NombreEmpresa=ArrayLectura[2];
                    txtNroPedido.setText(ArrayLectura[3]);
                    txtCliente.setText(ArrayLectura[4]);
                    txtNroGuia.setText(ArrayLectura[5]);
                    _NombreCliente=ArrayLectura[4];
                }
            }
            if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                    bmp=bitmap;
                    imgFotografia.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            /*if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras(); // Aquí es null
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPhotoImageView.setImageBitmap(imageBitmap);
            }*/

            }
/*
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //bmp=photo;
                imgFotografia.setImageBitmap(photo);
            }
*/



        }
        catch(Exception ex){
            Log.d("error",ex.getMessage());
        }
    }


    private void Limpiar()
    {
        txtCliente.setText("");
        txtNroGuia.setText("");
        txtNroPedido.setText("");
        txtObservacion.setText("");
        imgFotografia.setImageDrawable(null);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try{
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
        }
        catch(Exception ex){
            String q=ex.getMessage();
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

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
