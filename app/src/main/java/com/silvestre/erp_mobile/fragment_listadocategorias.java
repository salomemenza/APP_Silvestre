package com.silvestre.erp_mobile;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silvestre.erp_mobile.adaptadores.adapter_listacategorias;
import com.silvestre.erp_mobile.model.tbl01cat;
import com.silvestre.erp_mobile.procedimientos.ParseoJsonCategorias;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_listadocategorias extends Fragment {
    RecyclerView rcvwCategorias;
    RecyclerView.Adapter rcvwAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnExportar;
    RelativeLayout layout;

    //ArrayList<tbl01cat> _lstcategroias;
    private RequestQueue queue;
    ProgressDialog progressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vista = inflater.inflate(R.layout.fragment_listadocategorias, container, false);
        rcvwCategorias=vista.findViewById(R.id.listadocategorias_rcvw_categorias);
        btnExportar=vista.findViewById(R.id.listadocategorias_btnexportar);
        layout=vista.findViewById(R.id.listadocategorias_layout);
        rcvwCategorias.setHasFixedSize(true);

        layoutManager= new LinearLayoutManager(getActivity());
        rcvwCategorias.setLayoutManager(layoutManager);
        rcvwCategorias.setItemAnimator(new DefaultItemAnimator());

        //_lstcategroias= new ArrayList<tbl01cat>();
        //removedItems = new ArrayList<Integer>();

        //adapter = new CustomAdapter(data);
        //falta llenar el adaptador
        //rcvwCategorias.setAdapter(rcvwAdapter);
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
        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ExportarPDF();
                PdfDocument myPdf = new PdfDocument();
                PdfDocument.PageInfo pageInfo= new PdfDocument.PageInfo.Builder(400,900,1).create();
                PdfDocument.Page pagePdf = myPdf.startPage(pageInfo);

                Paint myPaint = new Paint();
                //String textoPrueba = new String("Ruc: "+ variables_globales.RUC_EMPRESA);

                int x=10;
                int y=80;


                pagePdf.getCanvas().drawText("PERFILES METALICOS J & J" ,(pagePdf.getCanvas().getWidth()/2)-12,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("COTIZACIÓN NRO 001-1" ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("RUC: " + "1046119959" ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();


                pagePdf.getCanvas().drawText("CLIENTE: " + "ANDY VERA SANCHO" ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("DIRECCIÓN : " + "JR 1RO DE MAYO 310 - SANTA ANITA" ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("VENDEDOR : " + "AVERAS - APP " ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("FORMA DE PAGO: " + "CONTADO " ,x,y,myPaint);
                y += myPaint.descent() - myPaint.ascent();

                pagePdf.getCanvas().drawText("OBSERVACIONES: " + "Este pedidos es de prueba y se realizara una descarga y envio automatico.",x,y,myPaint);

                myPdf.finishPage(pagePdf);
                //String Path=Environment.getDownloadCacheDirectory().getPath() + "/mypdf.pdf";
                //String Path="/"+ Environment.DIRECTORY_DOWNLOADS + "/mypdf.pdf";
                //String Path=requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/mypdf.pdf";
                String Path =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mypdf.pdf";
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
        });

        CargarProductos();
        return vista;

    }
    /*
    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
    */
  /*
    private void ExportarPDF(){
        try{


            Bitmap bitmap= Bitmap.createBitmap(layout.getWidth(),layout.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas= new Canvas(bitmap);
            layout.draw(canvas);
            //ImageView img = null;
            //img.setImageBitmap(bitmap);
            File file= savebitmap(bitmap);

            Intent intent = new Intent();
            file.getPath();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file:/"+file.getPath()), "image/*");
            startActivity(intent);


        }
        catch(Exception ex)
        {

            ex.getMessage();
        }


    }
*/
    private void CargarProductos()
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
                        ArrayList<tbl01cat> lista= ParseoJsonCategorias.parseDataListCategorias(response);

                        rcvwAdapter= new adapter_listacategorias(getContext(), lista,fragment_listadocategorias.this);
                        rcvwCategorias.setAdapter(rcvwAdapter);

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
                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity() );
        queue.add(stringRequest);
    }

}
