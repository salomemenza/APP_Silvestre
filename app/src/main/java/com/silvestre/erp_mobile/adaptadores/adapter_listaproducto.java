package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.model.tbllistadoproducto;

import java.util.ArrayList;

public class adapter_listaproducto extends ArrayAdapter<tbllistadoproducto> {
    public ArrayList<tbllistadoproducto> _lista= new ArrayList<tbllistadoproducto>();

    public adapter_listaproducto(Context context, ArrayList<tbllistadoproducto> users) {
        super(context, 0, users);
        _lista=users;
    }

    public void updateReceiptsList(ArrayList<tbllistadoproducto> newlist) {
        //_lista.clear();
        _lista.addAll(newlist);
        this.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //tbllistadoproducto eProducto = getItem(position);
        tbllistadoproducto eProducto = _lista.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plantilla_listadoproducto, parent, false);
        }
        // Lookup view for data population
        TextView txtdesv = (TextView) convertView.findViewById(R.id.txtdescproducto);
        TextView txtund = (TextView) convertView.findViewById(R.id.txtund);
        TextView txtmarc = (TextView) convertView.findViewById(R.id.txtmarca);
        TextView txtprec = (TextView) convertView.findViewById(R.id.txtprecio);
        TextView txtstoc = (TextView) convertView.findViewById(R.id.txtstock);
        // Populate the data into the template view using the data object
        txtdesv.setText(eProducto.getDescripcion());
        txtund.setText(eProducto.getUMedida());
        txtmarc.setText(eProducto.getMarca());
        txtprec.setText(String.valueOf(eProducto.getPrecio()));
        txtstoc.setText(String.valueOf(eProducto.getStock()));
        // Return the completed view to render on screen
        return convertView;
    }




}
