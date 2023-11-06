package com.silvestre.erp_mobile.procedimientos;

import com.silvestre.erp_mobile.model.mst01ped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ParseoJsonPedidos {
    public static ArrayList<mst01ped> parseDataListPedidos(String response) {
        ArrayList<mst01ped> lstpedidos= new ArrayList<mst01ped>();
        //ArrayList<String> arrayString= new ArrayList<>();
        mst01ped objpedido= new mst01ped();
        try {


            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("body");
            //Log.d("VOLLEY======>",jsonObject.getString("origen"));
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);
                //Log.d("VOLLEY======>",jsonObject.toString());
                objpedido= new mst01ped();

                objpedido.setIdPedido(Integer.parseInt(dataobj.getString("idPedido")));
                //Log.d("VOLLEY==1====>",String.valueOf(objpedido.getIdPedido()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //Log.d("VOLLEY==1.5====>",String.valueOf(dataobj.getString("fecha")));

                objpedido.setFecha(sdf.parse(dataobj.getString("fecha")));
                //objpedido.setFecha(Date.parse("01/01/2019"));
                objpedido.setCodcli(dataobj.getString("codcli"));
                //Log.d("VOLLEY==2====>",String.valueOf(objpedido.getCodcli()));
                objpedido.setNomCli(dataobj.getString("nomcli"));
                //Log.d("VOLLEY==3====>",String.valueOf(objpedido.getNomCli()));
                objpedido.setDirent(dataobj.getString("dirent"));
                //Log.d("VOLLEY==4====>",String.valueOf(objpedido.getDirent()));
                objpedido.setRuccli(dataobj.getString("ruccli"));
                //Log.d("VOLLEY==5====>",String.valueOf(objpedido.getRuccli()));
                objpedido.setFlag(dataobj.getString("flag"));
                //Log.d("VOLLEY==6====>",String.valueOf(objpedido.getFlag()));
                objpedido.setTotn(Double.parseDouble(dataobj.getString("totn")));
                //Log.d("VOLLEY==7====>",String.valueOf(objpedido.getTotn()));
                objpedido.setFlag_EstadoPedido(Integer.parseInt(dataobj.getString("flag_EstadoPedido")));
                //Log.d("VOLLEY==8====>",String.valueOf(objpedido.getFlag_EstadoPedido()));
                objpedido.setEstadoPedido(dataobj.getString("estadoPedido"));
                //Log.d("VOLLEY==9====>",String.valueOf(objpedido.getEstadoPedido()));
                //Log.d("VOLLEY==Entidad====>",objpedido.toString());
                objpedido.setCondicionVenta(dataobj.getString("condicionVenta"));
                //Log.d("VOLLEY==Entidad====>",objpedido.toString());
                lstpedidos.add(objpedido);
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
