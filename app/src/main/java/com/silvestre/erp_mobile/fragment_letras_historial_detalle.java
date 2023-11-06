package com.silvestre.erp_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_letras_historial;
import com.silvestre.erp_mobile.adaptadores.adapter_letras_historial_detalle;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.letraEmitida;
import com.silvestre.erp_mobile.model.letraEmitidaResumen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class fragment_letras_historial_detalle extends Fragment {

    RecyclerView rcvwLetrasHistorialDetalle;
    RecyclerView.Adapter rcvwAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout layout;
    private RequestQueue queue;
    private TextView textViewId, title, subTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letras_historial_detalle, container, false);
        layoutManager = new LinearLayoutManager(getActivity());


        // Enlaza la vista de TextView en el diseño del fragmento
        title = rootView.findViewById(R.id.title);
        subTitle = rootView.findViewById(R.id.subtitle);

        layout = rootView.findViewById(R.id.listadoLetrasHistorialDetalle_layout);
        rcvwLetrasHistorialDetalle = rootView.findViewById(R.id.recyclerViewx);
        rcvwLetrasHistorialDetalle.setHasFixedSize(true);
        rcvwLetrasHistorialDetalle.setLayoutManager(layoutManager);
        rcvwLetrasHistorialDetalle.setItemAnimator(new DefaultItemAnimator());
        queue = Volley.newRequestQueue(getActivity());
        // Recupera los datos pasados desde la actividad anterior
        Bundle extras = getArguments();
        if (extras != null) {
            String idString = extras.getString("id");

            // Intenta convertir el valor de "idString" a un entero
            int id = (int) Double.parseDouble(idString);

            String fecha = extras.getString("fecha");

            if (title != null) {
                title.setText("Grupo Letra Nro: " + id);
            }

            if (fecha != null) {
                subTitle.setText("Fecha: " + fecha);
            }
            cargarData(id);
        }
        return rootView;
    }

    /*private void cargarData() {

        letraEmitida[] letraEmitida = new letraEmitida[24];
        LocalDate fechaInicial = LocalDate.of(2023, 8, 26);

        for (int i = 0; i < letraEmitida.length; i++) {
            String id = Integer.toString(i + 1);
            letraEmitida[i] = new letraEmitida(i+1,0,378852,"37999",2,0,"1233","100.00","","",true,true,fechaInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),"");
            fechaInicial = fechaInicial.plusDays(1); // Agregar 1 día en cada iteración
        }

        rcvwAdapter= new adapter_letras_historial_detalle(getContext(), letraEmitida,fragment_letras_historial_detalle.this);
        rcvwLetrasHistorialDetalle.setAdapter(rcvwAdapter);
    }*/
    private void cargarData(Integer idSeguimiento) {
        // Crear una solicitud POST
        String url = "https://desarrollo.gruposilvestre.com.pe:443/SilvestreCRMPro/api/APP/APP_ObtenerHistorialDet";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("IdEmpresa", "1");
            jsonBody.put("Id_Seguimiento", idSeguimiento);
            jsonBody.put("Id_Vendedor", "398");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtén la cadena JSON del campo "value"
                            String jsonValue = response.getJSONObject("body").getString("value");

                            // Convierte la cadena JSON en una matriz JSON
                            JSONArray jsonArray = new JSONArray(jsonValue);

                            letraEmitida[] letraEmitida = new letraEmitida[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                double idLetra = obj.optDouble("Id_Letra");
                                int nroCuota = obj.optInt("NroCuota");
                                String idAceptante = obj.optString("Id_Aceptante");
                                String agendaNombre = obj.optString("AgendaNombre");
                                String importe = obj.optString("Importe");
                                String emision = obj.optString("Emision");
                                String vcmto = obj.optString("Vcmto");
                                String fechaRegistro = obj.optString("FechaRegistro");
                                boolean flagRecepcionado = obj.optBoolean("FlagRecepcionado");

                                letraEmitida[i] = new letraEmitida(0, (int)idLetra, "123", nroCuota, 0, idAceptante, importe, emision, vcmto, true, false, fechaRegistro,null);

                            }

                            rcvwAdapter= new adapter_letras_historial_detalle(getContext(), letraEmitida,fragment_letras_historial_detalle.this);
                            rcvwLetrasHistorialDetalle.setAdapter(rcvwAdapter);
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
        ;

        queue.add(jsonObjectRequest);
    }


}
