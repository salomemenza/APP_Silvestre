package com.silvestre.erp_mobile.procedimientos;

import android.util.Log;

import com.silvestre.erp_mobile.model.tbl01alm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseo {

    public static ArrayList<String> parseDataArrayString(String response) {
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
                //objalm.setCodAlm(dataobj.getString("codAlm"));
                //objalm.setNombreAlmacen(dataobj.getString("nomAlm"));
                JSONObject dataobj = dataArray.getJSONObject(i);
                Log.d("VOLLEY======>",jsonObject.getString("origen"));
                if(jsonObject.getString("origen").equals("almacen"))
                {
                    //Log.d("VOLLEY======>",dataobj.getString("codAlm") + "-" + dataobj.getString("nomAlm"));
                    arrayString.add(dataobj.getString("codAlm") + "-" + dataobj.getString("nomAlm"));
                    //lsttbl01alm.add(objalm);
                }
                else if(jsonObject.getString("origen").equals("familias"))
                {
                    arrayString.add(dataobj.getString("codFam") + "-" + dataobj.getString("nomFamilia"));
                }
                else if(jsonObject.getString("origen").equals("subfamilias"))
                {
                    arrayString.add(dataobj.getString("codSub") + "-" + dataobj.getString("nombreSubFam"));
                }


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
}
