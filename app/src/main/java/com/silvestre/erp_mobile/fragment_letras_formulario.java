package com.silvestre.erp_mobile;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.interfaz.DataUpdateListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;




public class fragment_letras_formulario extends Fragment {
    private TextInputLayout editTextCodigo, editTextNroLetra, editTextNombreAceptante, editTextImporte, editTextNroCuota;
    private Button buttonEnviar;
    private ImageButton btnSearch;
    private DataUpdateListener dataUpdateListener;
    private RequestQueue queue;
    private TextView text;
    private String nroLetra, idEstado, estadoNombre, idAceptante, aceptanteNombre, importe, emision, vcmto;
    private Integer idLetra;
    private double idFinanciamiento, nroCuota;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_letras_formulario, container, false);


        editTextCodigo = view.findViewById(R.id.editTextCodigo);
        editTextNroLetra = view.findViewById(R.id.editTextNroLetra);
        editTextNombreAceptante = view.findViewById(R.id.editTextNombreAceptante);
        editTextImporte = view.findViewById(R.id.editTextImporte);
        editTextNroCuota = view.findViewById(R.id.editTextNroCuota);
        buttonEnviar = view.findViewById(R.id.buttonEnviar);
        btnSearch = view.findViewById(R.id.btnSearch);
        text = view.findViewById(R.id.text);
        queue = Volley.newRequestQueue(getActivity());


        Bundle data = getArguments();


        if (data != null) {
            String codigo = data.getString("id_letra");
            Log.d("My App codigo", codigo);
            this.recuperarDatos(codigo);
           /*if (codigo.equals("37999")){
               btnSearch.setVisibility(View.GONE);
               this.recuperarDatos(codigo);
           }else{
               btnSearch.setVisibility(View.VISIBLE);
               Toast.makeText(getContext(), "El id "+codigo+" no existe", Toast.LENGTH_SHORT).show();
           }*/
        }else{
            Log.d("MyAPP","llego data vacia del scanner");
        }


        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });


        return view;
    }


    private void recuperarDatos(String codigo) {
        String url = "https://desarrollo.gruposilvestre.com.pe:443/SilvestreCRMPro/api/APP/APP_obtenerLetra";

        int numero = Integer.parseInt(codigo);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("IdEmpresa", "1");
            requestBody.put("Id_Letra", numero);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                           JSONObject bodyObject = response.getJSONObject("body");
                           String valueString = bodyObject.getString("value");
                           Log.d("MyAPP", valueString);
                           JSONArray valueArray = new JSONArray(valueString);


                           if (valueArray.length() > 0) {
                               JSONObject firstItem = valueArray.getJSONObject(0);
                               idLetra = firstItem.getInt("ID_Letra");
                               nroLetra = firstItem.getString("NroLetra");
                               idEstado = firstItem.getString("ID_Estado");
                               estadoNombre = firstItem.getString("EstadoNombre");
                               nroCuota = firstItem.getDouble("NroCuota");
                               idFinanciamiento = firstItem.getDouble("ID_Financiamiento");
                               idAceptante = firstItem.getString("ID_Aceptante");
                               aceptanteNombre = firstItem.getString("AceptanteNombre");
                               importe = firstItem.getString("Importe");
                               emision = firstItem.getString("Emision");
                               vcmto = firstItem.getString("Vcmto");

                               editTextCodigo.getEditText().setText(codigo);
                               editTextNroLetra.getEditText().setText(nroLetra);
                               editTextNombreAceptante.getEditText().setText(aceptanteNombre);
                               editTextImporte.getEditText().setText(importe);
                               editTextNroCuota.getEditText().setText("1");
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                } else {
                    Log.d("MyApp error", error.getMessage());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Agrega el encabezado de autorización con el token Bearer
                headers.put("Authorization", "Bearer " + variables_globales.Token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


    private void enviarDatos() {
        // se debe de consirar estos campos, si en caso se cambia de valor
       /*String codigo = editTextCodigo.getEditText().getText().toString();
       String nroLetra = editTextNroLetra.getEditText().getText().toString();
       String nombreAceptante = editTextNombreAceptante.getEditText().getText().toString();*/


        // Validación de datos, por ejemplo, asegúrate de que no estén vacíos
        if (idLetra != 0 && !nroLetra.isEmpty() && !importe.isEmpty()) {
            if (dataUpdateListener != null) {
                dataUpdateListener.onDataUpdated(idLetra, nroLetra, (int) Math.floor(nroCuota), (int) Math.floor(idFinanciamiento), idAceptante, importe, emision, vcmto);
            }


            // Cerrar el formulario
            getFragmentManager().popBackStack();
        }
    }


    public void setDataUpdateListener(DataUpdateListener listener) {
        this.dataUpdateListener = listener;
    }


    public void tuMetodoOnClick(View v) {
        Toast.makeText(getContext(), "Hola", Toast.LENGTH_SHORT).show();
    }
}
