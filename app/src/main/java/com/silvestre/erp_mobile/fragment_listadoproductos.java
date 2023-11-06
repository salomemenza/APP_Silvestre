package com.silvestre.erp_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_listadoproductos extends Fragment {
    ListView lstviewProductos;
    private String codfamilia;
    private String codsubfamilia;
    private RequestQueue queue;
    ProgressDialog progressDialog = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_listadoproductos, container, false);
        lstviewProductos=vista.findViewById(R.id.listadoproducto_lvwproductos);
        //Toast.makeText(getActivity(),getArguments().getString("edttext"),Toast.LENGTH_LONG).show();
        codfamilia=getArguments().getString("codfam");
        codsubfamilia=getArguments().getString("subfam");
        //codfamilia="01";
        //codsubfamilia="01-01";


        CargarProductos();
        //List<tbllistadoproducto> lista;


        //lista.add("hola");

        //adapter_listaproducto adaptador= new adapter_listaproducto(getActivity(),R.layout.plantilla_listadoproducto,lista);
        //lstviewProductos.setAdapter(adaptador);
        /*
        lstviewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbllistadoproducto objProducto= (tbllistadoproducto)lstviewProductos.getItemAtPosition(position);

                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

                fragment_pedidos fragmentNext= new fragment_pedidos();
                //fragmentNext.setArguments(bundle);

                //transaction.addToBackStack(null);

                transaction.replace(R.id.escenario,fragmentNext).commit();
            }
        });
        */
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
                        lstviewProductos.setAdapter(adaptador);
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
                params.put("codfam",codfamilia);
                params.put("codsub",codsubfamilia);

                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }


}
