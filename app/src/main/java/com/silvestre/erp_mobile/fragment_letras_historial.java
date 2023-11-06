package com.silvestre.erp_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.letraEmitidaResumen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class fragment_letras_historial extends Fragment {

    RecyclerView rcvwLetrasHistorial;
    RecyclerView.Adapter rcvwAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout layout;
    private RequestQueue queue;

    Button add;
    ProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_a1:
                Toast.makeText(getActivity(), "Haz hecho clic en Registrar", Toast.LENGTH_SHORT).show();

                fragment_letras_registro fRegistro = new fragment_letras_registro();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.escenario, fRegistro);
                transaction.addToBackStack(null);
                transaction.commit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("MiFragment", "onCreateOptionsMenu() llamado");
        inflater.inflate(R.menu.menu_add, menu); // Inflar el menú adicional
        super.onCreateOptionsMenu(menu, inflater);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View vista = inflater.inflate(R.layout.fragment_letras_historial, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        //layout = vista.findViewById(R.id.listadoLetrasHistorial_layout);
        rcvwLetrasHistorial = vista.findViewById(R.id.recyclerView);
        rcvwLetrasHistorial.setHasFixedSize(true);
        rcvwLetrasHistorial.setLayoutManager(layoutManager);
        rcvwLetrasHistorial.setItemAnimator(new DefaultItemAnimator());
        queue = Volley.newRequestQueue(getActivity());
        cargarData();
        return vista;
    }
    /*private void cargarData() {

        letraEmitidaResumen[] letraEmitidaResumen = new letraEmitidaResumen[24];
        LocalDate fechaInicial = LocalDate.of(2023, 8, 26); // Fecha fija

        for (int i = 0; i < letraEmitidaResumen.length; i++) {
            String id = Integer.toString(i + 1);
            letraEmitidaResumen[i] = new letraEmitidaResumen(id, "1", "AVERAS", "", "", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.", fechaInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "");
            fechaInicial = fechaInicial.plusDays(1); // Agregar 1 día en cada iteración
        }

        rcvwAdapter= new adapter_letras_historial(getContext(), letraEmitidaResumen,fragment_letras_historial.this);
        rcvwLetrasHistorial.setAdapter(rcvwAdapter);
    }*/

    private void cargarData() {
        // Crear una solicitud POST
        String url = "https://desarrollo.gruposilvestre.com.pe:443/SilvestreCRMPro/api/APP/APP_ObtenerHistorialCab";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("IdEmpresa", "1");
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

                            letraEmitidaResumen[] letraEmitidaResumen = new letraEmitidaResumen[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String idSeguimiento = obj.optString("Id_Seguimiento");
                                String idVendedor = obj.optString("");
                                String usuario = obj.optString("Usuario");
                                String latitud = obj.optString("Latitud");
                                String longitud = obj.optString("Longitud");
                                String observacion = obj.optString("Observacion");
                                String fechaRegistro = obj.optString("FechaRegistro");
                                String fotografia = obj.optString("Fotografia");

                                letraEmitidaResumen[i] = new letraEmitidaResumen(idSeguimiento, idVendedor, usuario, latitud, longitud, observacion, fechaRegistro, fotografia);
                            }

                            rcvwAdapter = new adapter_letras_historial(getContext(), letraEmitidaResumen, fragment_letras_historial.this);
                            rcvwLetrasHistorial.setAdapter(rcvwAdapter);
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
