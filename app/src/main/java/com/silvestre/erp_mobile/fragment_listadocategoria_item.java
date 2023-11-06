package com.silvestre.erp_mobile;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
import com.silvestre.erp_mobile.adaptadores.adapter_listacategoriasItems;
import com.silvestre.erp_mobile.model.tbl01items;
import com.silvestre.erp_mobile.procedimientos.ParseoJsonCategoriasItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_listadocategoria_item extends Fragment {

    RecyclerView rcvwItems;
    RecyclerView.Adapter rcvwAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog = null;
    ArrayList<tbl01items> _lstItems;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vista= inflater.inflate(R.layout.fragment_listadocategoria_item,container,false);
        rcvwItems= vista.findViewById(R.id.listadocategorias_item_rcvw_items);
        rcvwItems.setHasFixedSize(true);

        //layoutManager= new LinearLayoutManager(getActivity());
        layoutManager=new GridLayoutManager(getActivity(),2);
        rcvwItems.setLayoutManager(layoutManager);
        rcvwItems.setItemAnimator(new DefaultItemAnimator());
        _lstItems= new ArrayList<tbl01items>();
        CargarItems();
        //ImageView img = vista.findViewById(R.id.xxx);
        //Glide.with(this).load("https://www.inkatubos.com/wp-content/uploads/2019/04/Mesa-de-trabajo-1-copia-15.jpg").into(img);
        return vista;
    }

    private void CargarItems()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //ArrayList<String> lstproduct= parseo.parseDataArrayString(response);
                        ArrayList<tbl01items> lista= ParseoJsonCategoriasItems.parseDataListCategoriasItems(response);
                        Log.d("VOLLEY LISTADO======>",String.valueOf(lista.size()));

                        rcvwAdapter= new adapter_listacategoriasItems(getContext(), lista,fragment_listadocategoria_item.this);
                        rcvwItems.setAdapter(rcvwAdapter);

                        //Log.d("VOLLEY LISTADO======>",String.valueOf(lista.size()));
                        //adapter_listaproducto adaptador= new adapter_listaproducto(getActivity(),lista);
                        //adaptador.addAll(lista);
                        //lstviewProductos.setAdapter(adaptador);
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
                //params.put("ruc",variables_globales.LOGIN_RUC);20600085612
                params.put("ruc","20600253230");
                params.put("IdCategoria","6");
                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }

}
