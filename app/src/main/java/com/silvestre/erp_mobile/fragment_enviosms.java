package com.silvestre.erp_mobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_listadeuda_vencida;
import com.silvestre.erp_mobile.adaptadores.adapter_listapedidos;
import com.silvestre.erp_mobile.adaptadores.adapter_listaproducto;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.mst01ped;
import com.silvestre.erp_mobile.model.tblZonas;
import com.silvestre.erp_mobile.model.tbldeudavencida;
import com.silvestre.erp_mobile.model.tbllistadoproducto;
import com.silvestre.erp_mobile.procedimientos.ParseoJsonPedidos;
import com.silvestre.erp_mobile.procedimientos.parseoJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class fragment_enviosms extends Fragment {
    Double int_XVencer8=0.00;
    Double int_XVencer0=0.00;
    Double int_Vencido5=0.00;
    Double int_Vencido8=0.00;

    private RequestQueue queue;
    EditText txtZonas;
    TextView txtXVencer8,txtXVencer0,txtVencido8,txtVencido5,txtVencido25,txtVencido40;
    ImageButton btnEnvioSms;
    EditText txtCliente;
    ImageButton btnBuscar;
    ProgressDialog progressDialog = null;
    ArrayList<String> _lstZonas= new ArrayList<String>();
    ArrayList<tbldeudavencida> _lstDeudaVencida = new ArrayList<tbldeudavencida>();
    ArrayList<String> _lstSeleccion = new ArrayList<>();
    ListView lstDeudas;

    ArrayList<tbldeudavencida> _lstDeudaVencidaResumen = new ArrayList<tbldeudavencida>();
    ArrayList<tbldeudavencida> _lstDeudaVencidaDetalle = new ArrayList<tbldeudavencida>();

    public static final int MY_DEFAULT_TIMEOUT = 15000;
    private String strJson;


    public fragment_enviosms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_enviosms, container, false);
        btnEnvioSms=vista.findViewById((R.id.enviosms_btnenviar));
        btnBuscar=vista.findViewById((R.id.enviosms_btnbuscar));
        txtZonas=vista.findViewById(R.id.enviosms_txtzona);
        txtCliente=vista.findViewById(R.id.enviosms_txtcliente);
        lstDeudas=vista.findViewById(R.id.enviosms_lvwdeudas);
        txtXVencer8=vista.findViewById(R.id.enviosms_txtporvencer8);
        txtXVencer0=vista.findViewById(R.id.enviosms_txtporvencer0);
        txtVencido5=vista.findViewById(R.id.enviosms_txtvencido5);
        txtVencido8=vista.findViewById(R.id.enviosms_txtvencido8);


        txtZonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarZonas();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarClientes();
            }
        });


        btnEnvioSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_lstDeudaVencida.size()==0){
                    AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
                    View titleView = getLayoutInflater().inflate(R.layout.plantilla_customtitle_dialog, null);
                    TextView txtTitulo= titleView.findViewById(R.id.plantilla_alertdialog_txttitulo);
                    txtTitulo.setText("Envio de Mensajes SMS");
                    alerDialog.setCustomTitle(titleView);
                    alerDialog.setMessage("Primero debe cargar las deudas de los clientes.");

                    alerDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });


                    alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //LoginDialogFragment.this.getDialog().cancel();
                        }
                    });


                    alerDialog.show();
                }
                else{
                    CargarVencimientos();
                }

            }
        });

        return vista;
    }


    private void CargarVencimientos(){
        _lstSeleccion.clear();
        AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
        View titleView = getLayoutInflater().inflate(R.layout.plantilla_customtitle_dialog, null);
        TextView txtTitulo= titleView.findViewById(R.id.plantilla_alertdialog_txttitulo);
        txtTitulo.setText("Envio de Deudas");
        alerDialog.setCustomTitle(titleView);


        alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                //LoginDialogFragment.this.getDialog().cancel();
            }
        });

        ArrayList<String> lstVen= new ArrayList<>();
        lstVen.add("Desea enviar las deudas a los clientes?");
 /*
        lstVen.add("0 Dias por Vencer");
        lstVen.add("5 Dias Vencidos");
        lstVen.add("8 Dias Vencidos");
        lstVen.add("25 Dias Vencidos");
        lstVen.add("40 Dias Vencidos");
        lstVen.add("Todos");
*/
        CharSequence[] str = lstVen.toArray(new CharSequence[lstVen.size()]);

        alerDialog.setItems(str, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
/*
                dialog.dismiss();


                ListView lw = ((AlertDialog) dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(which);
                checkedItem.toString();

                String Tipo= checkedItem.toString().split(" ")[0].trim();
                EnviarSMS(Tipo);
*/
            }

        });
        /*
        alerDialog.setMultiChoiceItems(str, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                ListView lw = ((AlertDialog) dialogInterface).getListView();
                Object checkedItem = lw.getAdapter().getItem(i);
                if(b){

                    _lstSeleccion.add(checkedItem.toString().split(" ")[0]);
                }
                else {
                    _lstSeleccion.remove(checkedItem.toString().split(" ")[0]);
                }


            }
        });
        */

        alerDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                CargarDeudaResumen();
            }
        });

        alerDialog.show();
    }

    private void EnviarSMS(){
        int det=1;
        int cab=1;


        for (tbldeudavencida cr: _lstDeudaVencidaResumen
             ) {

            try {
                SmsManager sms = SmsManager.getDefault();
                if(cr.getSms().compareTo("Detalle")==0){
                    for (tbldeudavencida d: _lstDeudaVencidaDetalle.stream().filter(e -> e.getId_Agenda().compareTo(cr.getId_Agenda())==0).toArray(tbldeudavencida[]::new)
                         ) {
                        //Log.d("Detalle-->",cr.getSms());
                        SmsManager sms2 = SmsManager.getDefault();
                        //sms2.sendTextMessage("993767251",null,"det-->" + Integer.toString(det),null,null);
                        //sms2.sendTextMessage("993767251",null,d.getSms(),null,null);
                        sms2.sendTextMessage(d.getNumeroTelefono(),null,d.getSms(),null,null);
                    }

                }
                else{
                    //Log.d("Cabecera-->",cr.getSms());
                    //sms.sendTextMessage("993767251",null,cr.getSms(),null,null);
                    sms.sendTextMessage(cr.getNumeroTelefono(),null,cr.getSms(),null,null);
                }

                //Toast.makeText(this.getContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
            }
            catch (Exception error) {
                Toast.makeText(this.getContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }
        Toast.makeText(this.getContext(), "Los Mensajes estaran siendo enviados en segundo plano.", Toast.LENGTH_LONG).show();



    }
    private void CargarZonas(){
        _lstZonas.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        strJson="{ \"IdEmpresa\": " + variables_globales.LOGIN_IDEMPRESA +
                ",\"Texto\": \"" + variables_globales.LOGIN_NRODNI + "\"" +
                "}";

        String url = variables_globales.URL_ObtenerZonas;
        queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray dataArray = jsonObject.getJSONArray("body");
                                //Log.d("VOLLEY======>",jsonObject.getString("origen"));
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    _lstZonas.add(dataobj.getString("nombreZona"));

                                }
                                _lstZonas.add("TODO");

                                AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
                                View titleView = getLayoutInflater().inflate(R.layout.plantilla_customtitle_dialog, null);
                                TextView txtTitulo= titleView.findViewById(R.id.plantilla_alertdialog_txttitulo);
                                txtTitulo.setText("Zonas");
                                alerDialog.setCustomTitle(titleView);


                                alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        //LoginDialogFragment.this.getDialog().cancel();
                                    }
                                });

                                CharSequence[] str = _lstZonas.toArray(new CharSequence[_lstZonas.size()]);
                                alerDialog.setItems(str, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();


                                        ListView lw = ((AlertDialog) dialog).getListView();
                                        Object checkedItem = lw.getAdapter().getItem(which);
                                        checkedItem.toString();

                                        txtZonas.setText(checkedItem.toString());
                                        //txtcondicion.setText(checkedItem.toString());

                                    }

                                });

                                alerDialog.show();
                            }
                            catch (Exception ex){
                                progressDialog.dismiss();
                            }


                        } catch (Exception e) {

                            Toast.makeText(getActivity(),"No se pudo obtener las zonas",Toast.LENGTH_LONG).show();

                        }
                        finally {

                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();

                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization",variables_globales.Token);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //progressDialog.dismiss();
                    return strJson==null ? null : strJson.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }


    private void CargarClientes()
    {
        int_Vencido8=0.00;
        int_Vencido5=0.00;
        int_XVencer8=0.00;
        int_XVencer0=0.00;

        _lstDeudaVencida.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        strJson="{ \"IdEmpresa\": " + variables_globales.LOGIN_IDEMPRESA +
                ",\"NombreCliente\": \"" + txtCliente.getText() + "\"" +
                ",\"Zonas\": \"" + txtZonas.getText() + "\"" +
                ",\"NroDni\": \"" + variables_globales.LOGIN_NRODNI + "\"" +
                "}";

        String url = variables_globales.URL_ObtenerDeudaVencida;
        queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray dataArray = jsonObject.getJSONArray("body");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    tbldeudavencida edeuda= new tbldeudavencida();
                                    edeuda.setNumeroTelefono(dataobj.getString("telefono"));
                                    edeuda.setId_Agenda(dataobj.getString("id_Agenda"));
                                    edeuda.setNombreCliente(dataobj.getString("nombreCliente"));
                                    edeuda.setLinea(dataobj.getString("lineaCredito"));
                                    edeuda.setPorVencer8Dias(dataobj.getString("porVencer8Dias")=="0.00" ? "-" : dataobj.getString("porVencer8Dias"));
                                    int_XVencer8=int_XVencer8+Double.parseDouble(edeuda.getPorVencer8Dias().replace(",",""));

                                    edeuda.setPorVencer0Dias(dataobj.getString("porVencer0Dias"));
                                    int_XVencer0=int_XVencer0+Double.parseDouble(edeuda.getPorVencer0Dias().replace(",",""));
                                    edeuda.setVencido5Dias(dataobj.getString("vencido5Dias"));
                                    int_Vencido5=int_Vencido5+Double.parseDouble(edeuda.getVencido5Dias().replace(",",""));
                                    edeuda.setVencido8Dias(dataobj.getString("vencido8Dias"));
                                    int_Vencido8=int_Vencido8+Double.parseDouble(edeuda.getVencido8Dias().replace(",",""));
                                    edeuda.setVencido25Dias(dataobj.getString("vencido25Dias"));
                                    edeuda.setVencido40Dias(dataobj.getString("vencido40Dias"));
                                    _lstDeudaVencida.add(edeuda);
                                    //_lstDeudaVencida.add( dataobj.getString("nombreZona"));

                                }
                                DecimalFormat formatter = new DecimalFormat("#,###.00");
                                txtXVencer8.setText("$ " + formatter.format(int_XVencer8));
                                txtXVencer0.setText("$ " + formatter.format(int_XVencer0));
                                txtVencido5.setText("$ " + formatter.format(int_Vencido5));
                                txtVencido8.setText("$ " + formatter.format(int_Vencido8));

                                //ArrayList<tbldeudavencida> lista= parseoJson.parseDataListProductos(response);

                                //Log.d("VOLLEY LISTADO======>",String.valueOf(lista.size()));
                                adapter_listadeuda_vencida adaptador= new adapter_listadeuda_vencida(getActivity(),_lstDeudaVencida);
                                //adaptador.addAll(lista);
                                lstDeudas.setAdapter(adaptador);
                                progressDialog.dismiss();
                            }
                            catch (Exception ex){
                                progressDialog.dismiss();
                            }


                        } catch (Exception e) {

                            Toast.makeText(getActivity(),"No se pudo obtener los clientes",Toast.LENGTH_LONG).show();

                        }
                        finally {

                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();

                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization",variables_globales.Token);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //progressDialog.dismiss();
                    return strJson==null ? null : strJson.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void CargarDeudaResumen(){
        _lstDeudaVencidaResumen.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        strJson="{ \"IdEmpresa\": " + variables_globales.LOGIN_IDEMPRESA +
                "}";

        String url = variables_globales.URL_ObtenerDeudaVencidaResumen;
        queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray dataArray = jsonObject.getJSONArray("body");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    tbldeudavencida edeuda= new tbldeudavencida();
                                    edeuda.setId_Agenda(dataobj.getString("id_Agenda"));
                                    edeuda.setPorVencer8Dias(dataobj.getString("porVencer8Dias"));
                                    edeuda.setPorVencer0Dias(dataobj.getString("porVencer0Dias"));
                                    edeuda.setVencido5Dias(dataobj.getString("vencido5Dias"));
                                    edeuda.setVencido8Dias(dataobj.getString("vencido8Dias"));
                                    edeuda.setVencido25Dias(dataobj.getString("vencido25Dias"));
                                    edeuda.setVencido40Dias(dataobj.getString("vencido40Dias"));
                                    edeuda.setNumeroTelefono(dataobj.getString("telefono"));
                                    edeuda.setSms(dataobj.getString("mensaje"));
                                    _lstDeudaVencidaResumen.add(edeuda);
                                    //_lstDeudaVencida.add( dataobj.getString("nombreZona"));
                                }

                                progressDialog.dismiss();
                                CargarDeudaDetalle();
                            }
                            catch (Exception ex){
                                progressDialog.dismiss();
                            }


                        } catch (Exception e) {

                            Toast.makeText(getActivity(),"No se pudo obtener la deuda de los clientes",Toast.LENGTH_LONG).show();

                        }
                        finally {

                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();

                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization",variables_globales.Token);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //progressDialog.dismiss();
                    return strJson==null ? null : strJson.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void CargarDeudaDetalle(){
        _lstDeudaVencidaDetalle.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        strJson="{ \"IdEmpresa\": " + variables_globales.LOGIN_IDEMPRESA +
                "}";

        String url = variables_globales.URL_ObtenerDeudaVencidaDetalle;
        queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray dataArray = jsonObject.getJSONArray("body");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    tbldeudavencida edeuda= new tbldeudavencida();
                                    edeuda.setId_Agenda(dataobj.getString("id_Agenda"));
                                    edeuda.setPorVencer8Dias(dataobj.getString("documento"));
                                    edeuda.setNumeroTelefono(dataobj.getString("telefono"));
                                    edeuda.setSms(dataobj.getString("mensaje"));
                                    _lstDeudaVencidaDetalle.add(edeuda);
                                    //_lstDeudaVencida.add( dataobj.getString("nombreZona"));
                                }

                                progressDialog.dismiss();
                                EnviarSMS();
                            }
                            catch (Exception ex){
                                progressDialog.dismiss();
                            }


                        } catch (Exception e) {

                            Toast.makeText(getActivity(),"No se pudo obtener la deuda de los clientes",Toast.LENGTH_LONG).show();

                        }
                        finally {

                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();

                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization",variables_globales.Token);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //progressDialog.dismiss();
                    return strJson==null ? null : strJson.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}