package com.silvestre.erp_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.procedimientos.parseo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class productos extends Fragment {
    private RequestQueue queue;
    ImageButton btnbuscar;
    EditText txtalmacen;
    EditText txtfamilia;
    EditText txtsubfamilia;
    public String _Origen="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_productos, container, false);
        //return inflater.inflate(R.layout.fragment_productos, container, false);

        txtalmacen= vista.findViewById(R.id.txtalmacen);
        btnbuscar= vista.findViewById(R.id.btnbuscar);
        txtfamilia=vista.findViewById(R.id.txtfamilia);
        txtsubfamilia=vista.findViewById(R.id.txtsubfamilia);

        txtalmacen.setInputType(InputType.TYPE_NULL);
        txtfamilia.setInputType(InputType.TYPE_NULL);
        txtsubfamilia.setInputType(InputType.TYPE_NULL);

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtalmacen.getText().toString().equals(""))
                {Toast.makeText(getActivity(), "Debe seleccionar un almacen", Toast.LENGTH_SHORT).show();}
                else if(txtfamilia.getText().toString().equals(""))
                {Toast.makeText(getActivity(), "Debe seleccionar una familia", Toast.LENGTH_SHORT).show();}
                else if(txtsubfamilia.getText().toString().equals(""))
                {Toast.makeText(getActivity(), "Debe seleccionar una subfamilia", Toast.LENGTH_SHORT).show();}
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("codfam", txtfamilia.getText().toString().substring(0,2));
                    bundle.putString("subfam",txtsubfamilia.getText().toString().substring(0,5));

                    final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

                    fragment_listadoproductos fragmentNext= new fragment_listadoproductos();
                    fragmentNext.setArguments(bundle);

                    
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.escenario,fragmentNext).commit();
                    //transaction.replace(R.id.escenario,new fragment_listadoproductos()).commit();
                }

            }
        });

        txtalmacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cargar_Almacen();
            }
        });


        txtfamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtalmacen.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Debe seleccionar un almacen valido", Toast.LENGTH_SHORT).show();
                }
                else {
                    txtsubfamilia.setText("");
                    CargarFamilias();
                }


            }
        });

        txtsubfamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtfamilia.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Debe seleccionar una familia válido", Toast.LENGTH_SHORT).show();
                }
                else {
                    CargarSubFamilias();
                }
            }
        });

        /*
        mostrar el teclado virtual
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

        */

        //FragmentManager fmng= getSupportFragmentManager();

        return vista;
    }

    private void CargarSubFamilias()
    {

        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<String> lstsubfam= parseo.parseDataArrayString(response);
                        String[] strNames= new String[lstsubfam.size()];
                        strNames=lstsubfam.toArray(strNames);

                        final AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
                        alerDialog.setTitle("Sub Familias");

                        alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });

                        alerDialog.setItems(strNames, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                                ListView lw = ((AlertDialog) dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(which);
                                checkedItem.toString();
                                //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                                txtsubfamilia.setText(checkedItem.toString());

                            }

                        });

                        alerDialog.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ruc",variables_globales.LOGIN_RUC);
                params.put("codfam",txtfamilia.getText().toString().substring(0,2));

                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }


    private void CargarFamilias()
    {
        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<String> lstfamilias= parseo.parseDataArrayString(response);
                        String[] strNames= new String[lstfamilias.size()];
                        strNames=lstfamilias.toArray(strNames);

                        final AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
                        alerDialog.setTitle("Familias");

                        alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });
                        /*.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Send the positive button event back to the host activity
                                //mListener.onDialogPositiveClick(NoticeDialogFragment.this);
                                dialog.dismiss();


                                ListView lw = ((AlertDialog) dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(id);
                                checkedItem.toString();
                                //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                                txtfamilia.setText(checkedItem.toString());
                            }
                        });
                        */

                        /*
                        alerDialog.setView(View.inflate(R.layout.alert_dialog_personalizado, null))
                                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });
                        */
                        //String[] types = {"01-La Victoria", "02-Lurigancho"};

                        alerDialog.setItems(strNames, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                                ListView lw = ((AlertDialog) dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(which);
                                checkedItem.toString();
                                //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                                txtfamilia.setText(checkedItem.toString());

                            }

                        });

                        alerDialog.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "multipart/form-data; charset=utf-8");
                return headers;
            }
            */
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ruc",variables_globales.LOGIN_RUC);

                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }
    private void Cargar_Almacen()
    {
        String url = "";


/*
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("ruc",(Object)variables_globales.RUC_EMPRESA);

        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                //JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<String> lsttbl01alm= parseo.parseDataArrayString(response);
                        String[] strNames= new String[lsttbl01alm.size()];
                        strNames=lsttbl01alm.toArray(strNames);

                        AlertDialog.Builder b = new AlertDialog.Builder (getActivity());
                        b.setTitle("Almacenes");
                        //String[] types = {"01-La Victoria", "02-Lurigancho"};

                        b.setItems(strNames, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                                ListView lw = ((AlertDialog) dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(which);
                                checkedItem.toString();
                                //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                                txtalmacen.setText(checkedItem.toString());

                            }

                        });

                        b.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "multipart/form-data; charset=utf-8");
                return headers;
            }
            */
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ruc",variables_globales.LOGIN_RUC);

                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }
/*
    public ArrayList<String> parseData(String response) {
        ArrayList<tbl01alm> lsttbl01alm= new ArrayList<tbl01alm>();
        ArrayList<String> arrayString= new ArrayList<>();
        tbl01alm objalm= new tbl01alm();
        try {


            JSONObject jsonObject = new JSONObject(response);
            //jsonObject=response;
            //jsonObject=response;

            //if (jsonObject.getString("status").equals("true")) {
            //Log.d("VOLLEY======>",jsonObject.toString());
            JSONArray dataArray = jsonObject.getJSONArray("body");
            //arrayString= new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                objalm.setCodAlm(dataobj.getString("codAlm"));
                objalm.setNombreAlmacen(dataobj.getString("nomAlm"));
                arrayString.add(dataobj.getString("codAlm") + "-" + dataobj.getString("nomAlm"));
                lsttbl01alm.add(objalm);
            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return arrayString;
            //return lsttbl01alm;
        }


    }
    */
}
