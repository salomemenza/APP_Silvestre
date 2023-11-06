package com.silvestre.erp_mobile.procedimientos;

import com.silvestre.erp_mobile.model.tbl01items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseoJsonCategoriasItems {
    public static ArrayList<tbl01items> parseDataListCategoriasItems(String response) {
        ArrayList<tbl01items> lstcategoriasItems = new ArrayList<tbl01items>();
        //ArrayList<String> arrayString= new ArrayList<>();
        tbl01items objcategoriaitem = new tbl01items();
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objcategoriaitem= new tbl01items();
                objcategoriaitem.setCodi(dataobj.getString("codi"));
                objcategoriaitem.setCodf(dataobj.getString("codf"));
                objcategoriaitem.setDescripcion(dataobj.getString("descripcion"));
                objcategoriaitem.setIdCategoria(Integer.parseInt(dataobj.getString("idCategoria")));
                objcategoriaitem.setNombre(dataobj.getString("nombre"));
                objcategoriaitem.setMarca(dataobj.getString("marca"));
                objcategoriaitem.setStoc(Double.parseDouble(dataobj.getString("stoc")));
                objcategoriaitem.setPrecio(Double.parseDouble(dataobj.getString("precio")));
                objcategoriaitem.setUrlImagen(dataobj.getString("urlImagen"));

                lstcategoriasItems.add(objcategoriaitem);
            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return lstcategoriasItems;

        }


    }
}
