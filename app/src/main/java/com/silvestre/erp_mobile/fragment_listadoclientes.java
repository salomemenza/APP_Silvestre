package com.silvestre.erp_mobile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.silvestre.erp_mobile.adaptadores.adapter_listacliente;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.mst01cli;
import com.silvestre.erp_mobile.procedimientos.parseoJsonClientes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_listadoclientes extends Fragment {

    private RequestQueue queue;
    ListView lvwClientes;
    EditText txtTexto;
    EditText txtruc,txtnombrecliente,txtdireccion;
    ImageButton btnBuscar;
    ImageButton btnNuevo;
    ProgressDialog progressDialog = null;
    ArrayList<mst01cli> _lstClientes= new ArrayList<mst01cli>();
    adapter_listacliente _adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_listadoclientes, container, false);
        View vista = inflater.inflate(R.layout.fragment_listadoclientes, container, false);
        lvwClientes=vista.findViewById(R.id.listadocliente_lvwclientes);
        btnBuscar=vista.findViewById(R.id.listadocliente_imgbtnbuscar);
        btnNuevo=vista.findViewById(R.id.listadocliente_imgbtnnuevo);
        txtTexto=vista.findViewById(R.id.listadocliente_txttexto);

        lvwClientes.setClickable(true);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarClientes();
            }
        });

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarCliente();
            }
        });

        lvwClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lvwClientes.getItemAtPosition(position);
                mst01cli objCliente= (mst01cli)lvwClientes.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("codcli", objCliente.getCodcli());
                bundle.putString("dircli", objCliente.getDirCli());
                bundle.putString("nomcli", objCliente.getNomCli());
                bundle.putString("ruccli", objCliente.getRucCli());


                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

                fragment_pedidos fragmentNext= new fragment_pedidos();
                fragmentNext.setArguments(bundle);

                transaction.addToBackStack(null);
                transaction.replace(R.id.escenario,fragmentNext).commit();
            }
        });


        _adaptador= new adapter_listacliente(getActivity(),_lstClientes);
        lvwClientes.setAdapter(_adaptador);

        return vista;
    }

    private void AgregarCliente()
    {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.plantilla_alertdialog_nuevocliente);
        dialog.setTitle("Nuevo Cliente");

        Button dialogbutton= (Button)dialog.findViewById(R.id.dialogButtonOK);
        txtruc = (EditText)dialog.findViewById(R.id.plantilla_alertdialog_nuevocliente_txtruc);
        txtnombrecliente = (EditText)dialog.findViewById(R.id.plantilla_alertdialog_nuevocliente_txtnombre);
        txtdireccion = (EditText)dialog.findViewById(R.id.plantilla_alertdialog_nuevocliente_txtdireccion);


        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _lstClientes.clear();
                //_adaptador.clear();

                if(txtruc.getText().toString().compareTo("")==0 || txtnombrecliente.getText().toString().compareTo("")==0 ||
                txtdireccion.getText().toString().compareTo("")==0)
                {
                    dialog.dismiss();
                    return;
                }
                mst01cli eCliente= new mst01cli();
                eCliente.setCodcli("C000");
                eCliente.setDirCli(txtdireccion.getText().toString());
                eCliente.setNomCli(txtnombrecliente.getText().toString());

                eCliente.setRucCli(txtruc.getText().toString().trim());
                _lstClientes.add(eCliente);
                _adaptador.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();



    }
    /*
    @Override
    public void onResume() {
        super.onResume();
        _adaptador.notifyDataSetChanged();
    }
    */
    private void CargarClientes()
    {

        if(txtTexto.getText().toString().compareTo("")==0)
        {
            Toast.makeText(getActivity(),"Ingrese el Ruc/Nombre del cliente para la busqueda",Toast.LENGTH_LONG).show();
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
                        //ArrayList<mst01cli> lista= parseoJsonClientes.parseDataListClientes(response);
                        _lstClientes=parseoJsonClientes.parseDataListClientes(response);
                        //Log.d("VOLLEY LISTADO======>",String.valueOf(lista.size()));
                        _adaptador= new adapter_listacliente(getActivity(),_lstClientes);
                        //adaptador.addAll(lista);
                        lvwClientes.setAdapter(_adaptador);
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
