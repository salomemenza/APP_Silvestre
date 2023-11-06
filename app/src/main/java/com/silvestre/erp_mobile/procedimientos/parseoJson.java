package com.silvestre.erp_mobile.procedimientos;

import android.util.Log;

import com.silvestre.erp_mobile.model.tbllistadoproducto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class parseoJson {
    public static ArrayList<tbllistadoproducto> parseDataListProductos(String response) {
        ArrayList<tbllistadoproducto> lstproductos= new ArrayList<tbllistadoproducto>();
        //ArrayList<String> arrayString= new ArrayList<>();
        tbllistadoproducto objproducto= new tbllistadoproducto();
        try {


            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objproducto= new tbllistadoproducto();
                objproducto.setCodf(dataobj.getString("codf"));
                objproducto.setCodi(dataobj.getString("codi"));
                objproducto.setMarca(dataobj.getString("marc"));
                objproducto.setDescripcion(dataobj.getString("descr"));
                objproducto.setUMedida(dataobj.getString("umed"));
                objproducto.setStock(Integer.parseInt(dataobj.getString("stoc")));
                //objproducto.setPrecio(Double.parseDouble(dataobj.getString("precio")));
                objproducto.setPrecio(Double.parseDouble(String.format(Locale.US, "%.2f", Double.parseDouble(dataobj.getString("precio")))));
                objproducto.setMoneda(dataobj.getString("moneda"));
                //objproducto.setPrecio(0);
                //Log.d("VOLLEY======>andyddd",jsonObject.toString());
                lstproductos.add(objproducto);
            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            Log.d("VOLLEY LISTADO======>",String.valueOf(lstproductos.size()));
            return lstproductos;
            //return lsttbl01alm;
        }


    }
}
