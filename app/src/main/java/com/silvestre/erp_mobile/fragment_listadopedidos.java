package com.silvestre.erp_mobile;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_listapedidos;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.mst01ped;
import com.silvestre.erp_mobile.procedimientos.ParseoJsonPedidos;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_listadopedidos extends Fragment {

    private RequestQueue queue;
    ListView lvwPedidos;
    ImageButton btnBuscar;
    TextView txtTexto;
    ProgressDialog progressDialog = null;


    public fragment_listadopedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_listadopedidos, container, false);
        lvwPedidos=vista.findViewById(R.id.listadopedidos_lvwpedidos);
        btnBuscar=vista.findViewById(R.id.listadopedidos_imgbtnbuscar);
        txtTexto=vista.findViewById(R.id.listadopedidos_txttexto);

        lvwPedidos.setClickable(true);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarPedidos();
            }
        });

        lvwPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lvwPedidos.getItemAtPosition(position);
                mst01ped objPedido= (mst01ped)lvwPedidos.getItemAtPosition(position);

                Gson gson = new Gson();
                String jsonPedido = gson.toJson(objPedido);
                Bundle bundle = new Bundle();
                bundle.putString("pedido", jsonPedido);



                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

                fragment_pedidos fragmentNext= new fragment_pedidos();
                fragmentNext.setArguments(bundle);

                transaction.addToBackStack(null);
                transaction.replace(R.id.escenario,fragmentNext).commit();
            }
        });
        return vista;
    }

    private void CargarPedidos()
    {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<mst01ped> lista= ParseoJsonPedidos.parseDataListPedidos(response);
                        adapter_listapedidos adaptador= new adapter_listapedidos(getActivity(),lista);
                        lvwPedidos.setAdapter(adaptador);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ruc",variables_globales.LOGIN_RUC);
                params.put("texto",txtTexto.getText().toString());


                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }

}
