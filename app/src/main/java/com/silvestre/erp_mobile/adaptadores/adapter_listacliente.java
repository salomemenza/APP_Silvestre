package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.model.mst01cli;

import java.util.ArrayList;

public class adapter_listacliente extends ArrayAdapter<mst01cli> {

    public adapter_listacliente(Context context, ArrayList<mst01cli> users) {
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        mst01cli eCliente = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plantilla_listadocliente, parent, false);
        }
        // Lookup view for data population
        TextView txtnombrecli = (TextView) convertView.findViewById(R.id.txtnombrecliente);
        TextView txtruccli = (TextView) convertView.findViewById(R.id.txtruccliente);
        ImageView imgCliente = (ImageView) convertView.findViewById(R.id.imgcliente);

        // Populate the data into the template view using the data object
        txtnombrecli.setText(eCliente.getNomCli());
        txtruccli.setText(eCliente.getRucCli());

        if(eCliente.getRucCli().substring(0,3).equals("DNI"))
        {
            imgCliente.setImageResource(R.drawable.customer_x48);
        }
        else {
            imgCliente.setImageResource(R.drawable.factory_x48);
        }

        // Return the completed view to render on screen
        return convertView;
    }




}
