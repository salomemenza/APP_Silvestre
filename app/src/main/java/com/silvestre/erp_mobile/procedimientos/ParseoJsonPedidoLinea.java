package com.silvestre.erp_mobile.procedimientos;

import com.silvestre.erp_mobile.model.dtl01ped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseoJsonPedidoLinea {
    public static ArrayList<dtl01ped> parseDataListPedidoLinea(String response) {
        ArrayList<dtl01ped> lstpedidos= new ArrayList<dtl01ped>();
        //ArrayList<String> arrayString= new ArrayList<>();
        dtl01ped objpedidoLinea= new dtl01ped();
        try {


            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objpedidoLinea= new dtl01ped();

                objpedidoLinea.setIdPedido(Integer.parseInt(dataobj.getString("idPedido")));
                //Log.d("VOLLEY==1====>",String.valueOf(dataobj.getIdPedido()));
                objpedidoLinea.setCodf(dataobj.getString("codf"));
                //objpedido.setFecha(Date.parse("01/01/2019"));
                objpedidoLinea.setCodi(dataobj.getString("codi"));
                //Log.d("VOLLEY==2====>",String.valueOf(objpedido.getCodcli()));
                objpedidoLinea.setDescr(dataobj.getString("descr"));
                //Log.d("VOLLEY==3====>",String.valueOf(objpedido.getNomCli()));
                objpedidoLinea.setMarc(dataobj.getString("marc"));
                //Log.d("VOLLEY==4====>",String.valueOf(objpedido.getDirent()));
                objpedidoLinea.setUmed(dataobj.getString("umed"));
                //Log.d("VOLLEY==5====>",String.valueOf(objpedido.getRuccli()));
                objpedidoLinea.setCant(Double.parseDouble(dataobj.getString("cant")));
                //Log.d("VOLLEY==6====>",String.valueOf(objpedidoLinea.getFlag()));
                objpedidoLinea.setPreu(Double.parseDouble(dataobj.getString("preu")));
                objpedidoLinea.setTota(Double.parseDouble(dataobj.getString("tota")));
                objpedidoLinea.setTotn(Double.parseDouble(dataobj.getString("totn")));

                lstpedidos.add(objpedidoLinea);
            }



            //Intent intent = new Intent(login.this,MainActivity.class);
            //startActivity(intent);
            //}
            //return objtblUsuario;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {

            return lstpedidos;

        }


    }
}
