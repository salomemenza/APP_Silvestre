package com.silvestre.erp_mobile.procedimientos;

import com.silvestre.erp_mobile.model.mst01cli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseoJsonClientes {
    public static ArrayList<mst01cli> parseDataListClientes(String response) {
        ArrayList<mst01cli> lstclientes= new ArrayList<mst01cli>();
        //ArrayList<String> arrayString= new ArrayList<>();
        mst01cli objcliente= new mst01cli();
        try {


            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objcliente= new mst01cli();

                objcliente.setCodcli(dataobj.getString("codcli"));
                objcliente.setDirCli(dataobj.getString("dirCli"));
                objcliente.setNomCli(dataobj.getString("nombre"));
                objcliente.setRucCli(dataobj.getString("rucCli"));

                lstclientes.add(objcliente);

            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {

            return lstclientes;
            //return lsttbl01alm;
        }


    }
}
