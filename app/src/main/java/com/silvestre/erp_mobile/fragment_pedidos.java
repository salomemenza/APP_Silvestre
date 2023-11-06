package com.silvestre.erp_mobile;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_listaproducto_pedido;
import com.silvestre.erp_mobile.global.variables_globales;
import com.silvestre.erp_mobile.model.dtl01ped;
import com.silvestre.erp_mobile.model.mst01cli;
import com.silvestre.erp_mobile.model.mst01ped;
import com.silvestre.erp_mobile.model.tbllistadoproducto;
import com.silvestre.erp_mobile.procedimientos.ParseoJsonPedidoLinea;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_pedidos extends Fragment {

    private RequestQueue queue;
    EditText txtcondicion;
    ListView lvwProductos;
    Button btnAgregarProducto;
    Button btnGuardar;
    Button btnExportarPdf;
    Button btnAdjuntarImagen;
    TextView txtTotal;
    mst01cli _objCliente;
    ArrayList<tbllistadoproducto> _lstProductos=new ArrayList<tbllistadoproducto>();
    adapter_listaproducto_pedido _adaptador;
    tbllistadoproducto _objproducto= new tbllistadoproducto();
    int _IdPedido=0;
    private String JsonPedido;
    ProgressDialog progressDialog = null;
    String _JsonObjPedido;
    String _JsonObjPedido_IdPedido;
    int _Edicion=0;
    Boolean _SeleccionaImagen=false;
    Bitmap bmp=null;

    public fragment_pedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View vista = inflater.inflate(R.layout.fragment_pedidos, container, false);
        //return inflater.inflate(R.layout.fragment_pedidos, container, false);
        _objCliente= new mst01cli();

        _objCliente.setRucCli(getArguments().getString("ruccli"));
        _objCliente.setNomCli(getArguments().getString("nomcli"));
        _objCliente.setDirCli(getArguments().getString("dircli"));
        _objCliente.setCodcli(getArguments().getString("codcli"));
        _JsonObjPedido=getArguments().getString("pedido");


        //codfamilia=getArguments().getString("codfam");
        //codsubfamilia=getArguments().getString("subfam");



        txtcondicion= vista.findViewById(R.id.pedidos_txtcondicion);
        lvwProductos= vista.findViewById(R.id.pedidos_lvwproductos);
        btnAgregarProducto= vista.findViewById(R.id.pedidos_btnagregarItem);
        btnGuardar = vista.findViewById(R.id.pedidos_btnGuardar);
        btnExportarPdf= vista.findViewById(R.id.pedidos_btnExportarpdf);
        btnAdjuntarImagen=vista.findViewById(R.id.pedidos_btnAddImagen);

        btnAdjuntarImagen.setVisibility(View.GONE);
        btnExportarPdf.setVisibility(View.GONE);

        txtTotal=vista.findViewById(R.id.pedidos_txttotal);

        txtcondicion.setInputType(InputType.TYPE_NULL);

        //_lstProductos= new ArrayList<tbllistadoproducto>();

        //final adapter_listaproducto _adaptador=new adapter_listaproducto(getActivity(),_lstProductos);
        if(_JsonObjPedido != null &&  !_JsonObjPedido.isEmpty() && !_JsonObjPedido.equals("null"))
        {
            try{
                JSONObject obj= new JSONObject(_JsonObjPedido);

                _JsonObjPedido_IdPedido=obj.getString("IdPedido");
                txtcondicion.setText(obj.getString("CondicionVenta"));
                _IdPedido=Integer.parseInt(_JsonObjPedido_IdPedido);
                _objCliente.setCodcli(obj.getString("Codcli"));
                _objCliente.setNomCli(obj.getString("NomCli"));
                _objCliente.setDirCli(obj.getString("Dirent"));
                _objCliente.setRucCli(obj.getString("Ruccli"));
                btnExportarPdf.setVisibility(View.VISIBLE);
                btnAdjuntarImagen.setVisibility(View.VISIBLE);
            }
            catch (Exception ex)
            {}

            if(_Edicion==0)
                CargarPedidoLinea();
            /*
            double total=0;
            for(tbllistadoproducto e :  _lstProductos)
            {
                total=total + (e.getPrecio()*e.getCantidad());
            }
            txtTotal.setText(String.format("%.2f", total));
            */
            btnExportarPdf.setEnabled(true);
            btnAdjuntarImagen.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnAgregarProducto.setEnabled(true);
            lvwProductos.setClickable(true);
        }
        else {
            btnExportarPdf.setEnabled(false);
            btnAdjuntarImagen.setEnabled(false);
        }
        Log.d("VOLLEY==Linea===>","SEGUNDO");
        _adaptador= new adapter_listaproducto_pedido(getActivity(),_lstProductos,txtTotal);
        lvwProductos.setAdapter(_adaptador);



        txtcondicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialog = new AlertDialog.Builder ( getActivity(),R.style.AlerDialogEstiloERP);
                alerDialog.setTitle("Tipo de Venta");

                alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
                String[] types = {"01-Contado", "02-Credito"};

                alerDialog.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(which);
                        checkedItem.toString();
                        //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                        txtcondicion.setText(checkedItem.toString());

                    }

                });

                alerDialog.show();
            }
        });

        btnExportarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportarPDF();
                //CargarFoto();
            }
        });
        btnAdjuntarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarFoto();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //_adaptador.updateReceiptsList(_lstProductos);
                final AlertDialog.Builder alerDialog = new AlertDialog.Builder (getActivity(),R.style.AlerDialogEstiloERP);
                alerDialog.setTitle("Toma de Pedidos");
                alerDialog.setMessage("Desea registrar el pedido?");
                alerDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnviarPedido();

                    }
                });
                alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
                /*
                String[] types = {"01-Contado", "02-Credito"};

                alerDialog.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(which);
                        checkedItem.toString();
                        //Toast.makeText(getActivity(), checkedItem.toString(), Toast.LENGTH_SHORT).show();
                        txtcondicion.setText(checkedItem.toString());

                    }

                });
                */

                alerDialog.show();

            }
        });
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcondicion.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Debe seleccionar el tipo de venta", Toast.LENGTH_SHORT).show();}
                else {
                    _Edicion=1;
                    Bundle bundle = new Bundle();
                    bundle.putString("Origen", "PEDIDOS");
                    //bundle.putString("subfam",txtsubfamilia.getText().toString().substring(0,5));

                    fragment_listadoproductospedidos fragmentC = new fragment_listadoproductospedidos();
                    fragmentC.setTargetFragment(fragment_pedidos.this, 1);
                    getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.escenario, fragmentC).commit();
                    //getFragmentManager().beginTransaction().replace(R.id.escenario, fragmentC).commit();




                    //ft.addToBackStack(fragment.getClass().getName());
                    //ft.add(R.id.content, fragment, tag);
                    //ft.commit();

                }
            }
        });
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);


            }
        }
        return vista;
    }

    private void CargarFoto() {
        ImageView imgView;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Imagen"),10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==-1){//si Viene de eleccion de Imagen de la camara
            //Uri path= data.getData();
            _SeleccionaImagen=true;

            Uri selectedImage= data.getData();
            String pathImage= selectedImage.getPath();
            if(pathImage != null)
            {
                InputStream imageStream = null;
                try {
                    //imageStream = getContentResolver().openInputStream(
                    imageStream = getActivity().getContentResolver().openInputStream(
                            selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bmp = BitmapFactory.decodeStream(imageStream);
            }


            //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //Bitmap bitmap= BitmapFactory.decodeFile(path.getPath(),bmOptions);
            //ExportarPDF(bmp);

        }
        if (resultCode == 1) {
            //if (requestCode==AppConstant.FRAGMENT_CODE){
            //String datafromC = data.getStringExtra("datafrom C");
            String dataS= data.getStringExtra("PRODUCTO");
            try{
                JSONObject jsonObject = new JSONObject(dataS);

                _objproducto= new tbllistadoproducto();
                _objproducto.setCodf(jsonObject.getString("Codf"));
                _objproducto.setCodi(jsonObject.getString("Codi"));
                _objproducto.setMarca(jsonObject.getString("Marca"));
                _objproducto.setDescripcion(jsonObject.getString("Descripcion"));
                _objproducto.setUMedida(jsonObject.getString("UMedida"));
                _objproducto.setStock(Integer.parseInt(jsonObject.getString("Stock")));
                _objproducto.setPrecio(Double.parseDouble(jsonObject.getString("Precio")));
                _objproducto.setMoneda(jsonObject.getString("Moneda"));
                _objproducto.setCantidad(1);
                _objproducto.setSubtotal(_objproducto.getPrecio());
                _lstProductos.add(_objproducto);
                //_adaptador.notifyDataSetChanged();

                //don't DISPLAY THE ITEMS




            }
            catch (Exception ex)
            {
                Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
            finally {
                super.onActivityResult(requestCode, resultCode, data);
            }



            //}
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //adapter_listaproducto adaptador= new adapter_listaproducto(getActivity(),_lstProductos);
        /*if(_lstProductos.size()>1)
        {
            _adaptador.clear();
        }*/
        //_adaptador.add(_objproducto);
        //_adaptador.notifyDataSetChanged();
        Log.d("VOLLEY==Linea===>","RESUME");
        _adaptador.notifyDataSetChanged();
        //lvwProductos.setAdapter(_adaptador);
        double total=0;
        for(tbllistadoproducto e :  _lstProductos)
        {
            total=total + (e.getPrecio()*e.getCantidad());
        }
        txtTotal.setText(String.format(Locale.US,"%.2f", total));
    }

    private  void ExportarPDF(){
        //ExportarPDF();
        PdfDocument myPdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo= new PdfDocument.PageInfo.Builder(400,900,1).create();
        PdfDocument.Page pagePdf = myPdf.startPage(pageInfo);


        Paint myPaint = new Paint();
        //String textoPrueba = new String("Ruc: "+ variables_globales.RUC_EMPRESA);

        int x=10;
        int y=80;

        //Bitmap logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo_pdf_total);



        myPaint.setColor(Color.BLUE);
        myPaint.setTextSize(14);
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        pagePdf.getCanvas().drawText( "" + " - PERFILES METALICOS J & J S.A.C." ,pageInfo.getPageWidth()/2,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();


        //pagePdf.getCanvas().drawBitmap( Logo ,x,y,myPaint);
        //y += myPaint.descent() - myPaint.ascent();

        myPaint.setTextSize(10);
        pagePdf.getCanvas().drawText( "MZ B LOTE 12 COOP. PARAISO DE LA MOLINA" ,pageInfo.getPageWidth()/2,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        myPaint.setTextSize(10);
        pagePdf.getCanvas().drawText( "TELEF. : 3680944 - 3682201" ,pageInfo.getPageWidth()/2,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText( "CEL. : 989633348 - 989633302- 994382071" ,pageInfo.getPageWidth()/2,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText( "" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText( "" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();
        pagePdf.getCanvas().drawText( "" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();
        pagePdf.getCanvas().drawText( "" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        myPaint.setTextAlign(Paint.Align.LEFT);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());


        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(null);
        myPaint.setTextSize(14);
        pagePdf.getCanvas().drawText("COTIZACIÓN NRO: 001-" + String.valueOf(_IdPedido) + "    FECHA : "  + date  ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        myPaint.setTextSize(12);
        pagePdf.getCanvas().drawText("_______________________________________________________________________" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();


        pagePdf.getCanvas().drawText("RUC                   : " + _objCliente.getRucCli() ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();


        pagePdf.getCanvas().drawText("CLIENTE           : " + _objCliente.getNomCli() ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("DIRECCIÓN       : " + _objCliente.getDirCli() ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("VENDEDOR      : " + variables_globales.LOGIN_USUARIO ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();


        pagePdf.getCanvas().drawText("FORMA DE PAGO : " + "CONTADO " ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("----------------------------------- LISTA DE PRODUCTOS ------------------------------------" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();



        for(tbllistadoproducto e :  _lstProductos)
        {
            pagePdf.getCanvas().drawText(e.getCodf() + "   -   " + e.getDescripcion().trim() ,x,y,myPaint);
            y += myPaint.descent() - myPaint.ascent();

            pagePdf.getCanvas().drawText("   "+ String.valueOf(e.getCantidad()) + " " + e.getUMedida() +  " x " + "S/ " +  String.valueOf(e.getPrecio())  + " --> S/ " + String.valueOf(e.getSubtotal()) ,x,y,myPaint);
            y += myPaint.descent() - myPaint.ascent();
            /*
            i.setIdPedido(_IdPedido);
            i.setCodf(e.getCodf().trim());
            i.setCodi(e.getCodi().trim());
            i.setDescr(e.getDescripcion().trim());
            i.setMarc(e.getMarca());
            i.setUmed(e.getUMedida());
            i.setCant(e.getCantidad());
            i.setPreu(e.getPrecio());
            detalle.add(i);
            */
        }
        pagePdf.getCanvas().drawText("",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();


        pagePdf.getCanvas().drawText("TOTAL A PAGAR  :",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        Double Subtotal=0.0,Igv=0.0;
        Subtotal=Double.parseDouble(txtTotal.getText().toString())/((variables_globales.IGV/100)+1);
        Igv=Double.parseDouble(txtTotal.getText().toString())-Subtotal;
        pagePdf.getCanvas().drawText("SUBTOTAL          S/  :     " + String.format(Locale.US,"%.2f", Subtotal) ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("IGV                         S/  :     " + String.format(Locale.US,"%.2f", Igv)  ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("TOTAL                  S/  :     " + txtTotal.getText().toString(),x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("--------------------------------------- CUENTA BANCARIA ----------------------------------------" ,x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("Sirvase a depositar en la siguiente cuenta corriente:",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("BCP SOLES: " + "",x,y,myPaint);
        y += myPaint.descent() - myPaint.ascent();

        pagePdf.getCanvas().drawText("",x,y,myPaint);

        Paint myPaintAndy=new Paint();
        myPaintAndy.setTextAlign(Paint.Align.RIGHT);
        myPaintAndy.setTextSize(7);
        pagePdf.getCanvas().drawText("Desarrollado por Andy Vera - Cel: 993767251",350,880,myPaintAndy);
        pagePdf.getCanvas().drawText("Correo: andy_14_08_@hotmail.com",350,890,myPaintAndy);
        myPdf.finishPage(pagePdf);

        if(_SeleccionaImagen){
            PdfDocument.PageInfo pageInfo2= new PdfDocument.PageInfo.Builder(400,900,1).create();
            PdfDocument.Page pagePdf2 = myPdf.startPage(pageInfo2);

            int y2=80;

            Bitmap Logo= Bitmap.createScaledBitmap(bmp,400,400,false);
            pagePdf2.getCanvas().drawBitmap(Logo,x,y2,myPaint);
            //y += myPaint.descent() - myPaint.ascent()
            myPdf.finishPage(pagePdf2);
        }
        _SeleccionaImagen=false;


        //String Path=Environment.getDownloadCacheDirectory().getPath() + "/mypdf.pdf";
        //String Path="/"+ Environment.DIRECTORY_DOWNLOADS + "/mypdf.pdf";
        //String Path=requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/mypdf.pdf";
        String Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + _objCliente.getRucCli() + "_" + _objCliente.getNomCli() + ".pdf";
        File myfile=new File(Path);
        //File myfile= new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        try {
            myPdf.writeTo(new FileOutputStream(myfile));


            //Intent target = new Intent(Intent.ACTION_VIEW);
            //target.setDataAndType(Uri.fromFile(myfile),"application/pdf");
            //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            //Intent intent = Intent.createChooser(target, "Open File");
            //startActivity(target);

            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_VIEW);
            Uri pdfUri;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                pdfUri= FileProvider.getUriForFile(getContext(),BuildConfig.APPLICATION_ID + ".provider",myfile);
                //pdfUri= FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(myfile.getPath()));
            }
            else {
                pdfUri=Uri.fromFile(myfile);
            }
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();


        }
        finally {
            myPdf.close();
        }

    }
    private void EnviarPedido()
    {
        mst01ped Cabecera= new mst01ped();

        Cabecera.setIdPedido(_IdPedido);
        Cabecera.setCodcli(_objCliente.getCodcli());
        Cabecera.setNomCli(_objCliente.getNomCli().trim());
        Cabecera.setDir(_objCliente.getDirCli().trim());
        Cabecera.setDirent(_objCliente.getDirCli().trim());
        Cabecera.setRuccli(_objCliente.getRucCli());
        Cabecera.setMone("S");
        Cabecera.setTotn(Double.parseDouble(txtTotal.getText().toString()));
        Cabecera.setCodUsuarioregistro(variables_globales.LOGIN_USUARIO);
        Cabecera.setCodcdv(txtcondicion.getText().toString().substring(0,2));
        Cabecera.setFlag_EstadoPedido(0);
        Cabecera.setRuc(variables_globales.LOGIN_RUC);

        ArrayList<dtl01ped> detalle = new ArrayList<dtl01ped>();

        for(tbllistadoproducto e :  _lstProductos)
        {
            dtl01ped i= new dtl01ped();
            i.setIdPedido(_IdPedido);
            i.setCodf(e.getCodf().trim());
            i.setCodi(e.getCodi().trim());
            i.setDescr(e.getDescripcion().trim());
            i.setMarc(e.getMarca());
            i.setUmed(e.getUMedida());
            i.setCant(e.getCantidad());
            i.setPreu(e.getPrecio());
            detalle.add(i);
        }


        Gson gson = new Gson();
        Pedido pedido = new Pedido();
        pedido.Cabecera=Cabecera;
        pedido.Detalle=detalle;

        JsonPedido=gson.toJson(pedido);

/*
        String url = variables_globales.URL_RegistrarPedido;
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.d("VOLLEY","ENTRO AL RESPONSE");
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("200")){

                                Toast.makeText(getActivity(),"Se registro correctamente el Pedido",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(getActivity(),"ERROR AL REGISTRAR",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        finally {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("",JsonPedido);



                return params;
            }

        };

*/
/*
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("pedido",(Object) JsonPedido);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = variables_globales.URL_RegistrarPedido;
        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.POST, url,postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(login.this,response,Toast.LENGTH_LONG).show();

                        try{
                            Log.d("VOLLEY","ENTRO AL RESPONSE");
                            Log.d("VOLLEY",response.toString());
                            JSONObject jsonObject = response;
                            if(jsonObject.getString("status").equals("200")){

                                Toast.makeText(getActivity(),"Se registro correctamente el Pedido",Toast.LENGTH_LONG).show();
                            }
                            if(jsonObject.getString("status").equals("400")){

                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(getActivity(),"ERROR AL REGISTRAR",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        finally {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


*/
/*
        Map<String, String> params = new HashMap<String, String>();
        params.put("pedido", JsonPedido);

        String url = variables_globales.URL_RegistrarPedido;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });

        */



        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = "";
        queue = Volley.newRequestQueue(getActivity() );
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject= new JSONObject(response);
                            progressDialog.dismiss();
                            if(jsonObject.getString("status").equals("200")){

                                Toast.makeText(getActivity(),"Se registro correctamente el Pedido",Toast.LENGTH_LONG).show();
                            }
                            if(jsonObject.getString("status").equals("400")){

                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"ERROR AL REGISTRAR",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        finally {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    progressDialog.dismiss();
                    return JsonPedido==null ? null : JsonPedido.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException ex){
                    progressDialog.dismiss();
                    return null;
                }
            }

        };
        //queue = Volley.newRequestQueue(getActivity() );
        //jsonObjReq.setTag("PEDIDO");
        //queue.add(stringRequest);
        queue.add(stringRequest);
    }

    public  class  Pedido{
        private mst01ped Cabecera;
        private ArrayList<dtl01ped> Detalle;
    }

    private void CargarPedidoLinea()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar_personalizado);

        String url = "";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("VOLLEY==Linea===>",response);
                        ArrayList<dtl01ped> lista= ParseoJsonPedidoLinea.parseDataListPedidoLinea(response);
                        Log.d("VOLLEY==Linea===>",response.toString());
                        //ArrayList<tbllistadoproducto> lstproductosPedido = new ArrayList<tbllistadoproducto>();
                        _lstProductos = new ArrayList<tbllistadoproducto>();
                        for(dtl01ped e :  lista) {
                            tbllistadoproducto elistado= new tbllistadoproducto();
                            /*
                            _objproducto.setCodf(jsonObject.getString("Codf"));
                            _objproducto.setCodi(jsonObject.getString("Codi"));
                            _objproducto.setMarca(jsonObject.getString("Marca"));
                            _objproducto.setDescripcion(jsonObject.getString("Descripcion"));
                            _objproducto.setUMedida(jsonObject.getString("UMedida"));
                            _objproducto.setStock(Integer.parseInt(jsonObject.getString("Stock")));
                            _objproducto.setPrecio(Double.parseDouble(jsonObject.getString("Precio")));
                            _objproducto.setMoneda(jsonObject.getString("Moneda"));
                            */

                            elistado.setCodf(e.getCodf());
                            elistado.setCodi(e.getCodi());
                            elistado.setMarca(e.getMarc());
                            elistado.setDescripcion(e.getDescr());
                            elistado.setUMedida(e.getUmed());
                            elistado.setStock(0);
                            elistado.setPrecio(e.getPreu());
                            elistado.setMoneda("S");
                            elistado.setCantidad(e.getCant());
                            elistado.setSubtotal(elistado.getCantidad()*elistado.getPrecio());

                            _lstProductos.add(elistado);
                        }
                        //Log.d("VOLLEY==Linea===>",String.valueOf(_lstProductos.size()));
                        Log.d("VOLLEY==Linea===>","PRIMERO");
                        _adaptador= new adapter_listaproducto_pedido(getActivity(),_lstProductos,txtTotal);
                        lvwProductos.setAdapter(_adaptador);

                        double total=0;
                        for(tbllistadoproducto e :  _lstProductos)
                        {
                            total=total + (e.getPrecio()*e.getCantidad());
                        }
                        txtTotal.setText(String.format(Locale.US,"%.2f", total));
                        //_adaptador= new adapter_listaproducto_pedido(getActivity(),_lstProductos,txtTotal);
                        //lvwProductos.setAdapter(_adaptador);
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
                params.put("IdPedido",_JsonObjPedido_IdPedido);



                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }
}
