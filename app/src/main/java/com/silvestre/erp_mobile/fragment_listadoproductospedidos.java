package com.silvestre.erp_mobile;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_listaproducto;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.tbllistadoproducto;
import com.silvestre.erp_mobile.procedimientos.parseoJson;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_listadoproductospedidos extends Fragment {

    private RequestQueue queue;
    ListView lvwProductos;
    EditText txtTexto;
    ImageButton btnBuscar;
    ProgressDialog progressDialog = null;


    public fragment_listadoproductospedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_listadoproductospedidos, container, false);
        lvwProductos=vista.findViewById(R.id.listadoproductopedido_lvwproductos);
        btnBuscar=vista.findViewById(R.id.listadoproductopedido_imgbtnbuscar);
        txtTexto=vista.findViewById(R.id.listadoproductopedido_txttexto);

        lvwProductos.setClickable(true);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarProductos();
            }
        });

        lvwProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbllistadoproducto objProducto= (tbllistadoproducto)lvwProductos.getItemAtPosition(position);
                Gson gson = new Gson();
                String jsonProducto = gson.toJson(objProducto);

                /*
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);
                fragment_pedidos fragmentNext= new fragment_pedidos();
                transaction.replace(R.id.escenario,fragmentNext).commit();
                */


               // getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);

                //getFragmentManager().popBackStack();



                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        1,
                        new Intent().putExtra("PRODUCTO", jsonProducto)
                );
                getFragmentManager().popBackStack();



            }
        });

        return vista;
    }

    private void CargarProductos()
    {
        //codfamilia=codfam;
        //codsubfamilia=codsub;
        //final String codsubfam=codsub;

        /*
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Cargando");
        progressDoalog.setTitle("Conexion Servidor");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        */
        if(txtTexto.getText().toString().trim().compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Debe Ingresar un texto para la busqueda",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //ArrayList<String> lstproduct= parseo.parseDataArrayString(response);
                        ArrayList<tbllistadoproducto> lista= parseoJson.parseDataListProductos(response);

                        //Log.d("VOLLEY LISTADO======>",String.valueOf(lista.size()));
                        adapter_listaproducto adaptador= new adapter_listaproducto(getActivity(),lista);
                        //adaptador.addAll(lista);
                        lvwProductos.setAdapter(adaptador);
                        progressDialog.dismiss();
                        //String[] strNames= new String[lstproduct.size()];
                        //strNames=lstproduct.toArray(strNames);
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
