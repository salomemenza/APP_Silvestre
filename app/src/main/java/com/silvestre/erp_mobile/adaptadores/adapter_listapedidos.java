package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.model.mst01ped;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class adapter_listapedidos extends ArrayAdapter<mst01ped> {
    public adapter_listapedidos(Context context, ArrayList<mst01ped> pedidos) {
        super(context, 0, pedidos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        mst01ped ePedido = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plantilla_listadopedidos, parent, false);
        }
        // Lookup view for data population
        TextView txtFecha = (TextView) convertView.findViewById(R.id.plantilla_listadopedido_fecha);
        ImageView imbEstado = (ImageView) convertView.findViewById(R.id.plantilla_listadopedido_imgestado);
        TextView txtCliente = (TextView) convertView.findViewById(R.id.plantilla_listadopedido_txtNomCliente);
        TextView txtEstado = (TextView) convertView.findViewById(R.id.plantilla_listadopedido_txtestado);
        TextView txtTotal = (TextView) convertView.findViewById(R.id.plantilla_listadopedido_txttotal);
        TextView txtIdPedido = (TextView) convertView.findViewById(R.id.plantilla_listadopedid_nropedido);

        // Populate the data into the template view using the data object
        //txtFecha.setText(ePedido.getFecha().toString());
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        txtFecha.setText(fmtOut.format(ePedido.getFecha()));
        txtTotal.setText(String.valueOf(ePedido.getTotn()));
        txtEstado.setText(String.valueOf(ePedido.getEstadoPedido()));
        txtCliente.setText(ePedido.getNomCli());
        txtIdPedido.setText(String.valueOf(ePedido.getIdPedido()));


        if(ePedido.getFlag_EstadoPedido()==0)//Emitido
        {
            txtEstado.setTextColor(Color.parseColor("#3b5998"));
            imbEstado.setImageResource(R.drawable.registrado_x48);
        }
        if(ePedido.getFlag_EstadoPedido()==1)//Registrado
        {
            txtEstado.setTextColor(Color.parseColor("#55b849"));
            imbEstado.setImageResource(R.drawable.procesando_x48);
        }
        if(ePedido.getFlag_EstadoPedido()==2)//Despachado
        {
            txtEstado.setTextColor(Color.parseColor("#FF9800"));
            imbEstado.setImageResource(R.drawable.delivery_x48);
        }
        if(ePedido.getFlag_EstadoPedido()==3)//entregado
        {
            txtEstado.setTextColor(Color.parseColor("#55b849"));
            imbEstado.setImageResource(R.drawable.paquete_entregado_x48);
        }


        // Return the completed view to render on screen
        return convertView;
    }
}
