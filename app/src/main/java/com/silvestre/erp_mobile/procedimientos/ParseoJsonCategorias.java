package com.silvestre.erp_mobile.procedimientos;

import android.util.Base64;

import com.silvestre.erp_mobile.model.tbl01cat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseoJsonCategorias {

    public static ArrayList<tbl01cat> parseDataListCategorias(String response) {
        ArrayList<tbl01cat> lstcategorias = new ArrayList<tbl01cat>();
        //ArrayList<String> arrayString= new ArrayList<>();
        tbl01cat objcategoria = new tbl01cat();
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objcategoria= new tbl01cat();

                objcategoria.setActivo(Integer.parseInt(dataobj.getString("activo")));
                objcategoria.setIdCategoria(Integer.parseInt(dataobj.getString("idCategoria")));
                objcategoria.setNomCategoria(dataobj.getString("nomCategoria"));
                //objcategoria.setImagen(dataobj.getString("imagen64").getBytes());
                objcategoria.setImagen(Base64.decode(dataobj.getString("imagen64"), Base64.DEFAULT));
                objcategoria.setUrlImagen(dataobj.getString("urlImagen"));
                lstcategorias.add(objcategoria);
            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return lstcategorias;

        }


    }
}
