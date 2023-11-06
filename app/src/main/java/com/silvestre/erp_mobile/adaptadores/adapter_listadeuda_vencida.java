package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.fragment_enviosms;
import com.silvestre.erp_mobile.fragment_listadocategoria_item;
import com.silvestre.erp_mobile.fragment_listadocategorias;
import com.silvestre.erp_mobile.model.tbl01cat;
import com.silvestre.erp_mobile.model.tbldeudavencida;
import com.silvestre.erp_mobile.model.tbllistadoproducto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_listadeuda_vencida extends ArrayAdapter<tbldeudavencida> {
    public ArrayList<tbldeudavencida> _lista= new ArrayList<tbldeudavencida>();

    public adapter_listadeuda_vencida(Context context, ArrayList<tbldeudavencida> users) {
        super(context, 0, users);
        _lista=users;
    }

    public void updateReceiptsList(ArrayList<tbldeudavencida> newlist) {
        //_lista.clear();
        _lista.addAll(newlist);
        this.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //tbllistadoproducto eProducto = getItem(position);
        tbldeudavencida eDeuda = _lista.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plantilla_listadodeuda_vencida, parent, false);
        }
        // Lookup view for data population

        TextView txt8diasxvencer = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt8diasxvencer);
        TextView txt0diasxvencer = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt0diasxvencer);
        TextView txt5diasvencido = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt5diasvencidos);
        TextView txt8diasvencido = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt8diasvencidos);
        TextView txt25diasvencido = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt25diasvencidos);
        TextView txt40diasvencido = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txt40diasvencidos);
        TextView txtnombrecliente = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txtnombrecliente);
        TextView txtlineacredito = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txtlineacredito);
        TextView txtlcodigocliente = (TextView) (TextView) convertView.findViewById(R.id.plantilla_dVencida_txtcodigocliente);

        // Populate the data into the template view using the data object
        txt8diasxvencer.setText(eDeuda.getPorVencer8Dias().toString());
        txt0diasxvencer.setText(eDeuda.getPorVencer0Dias().toString());
        txt5diasvencido.setText(eDeuda.getVencido5Dias().toString());
        txt8diasvencido.setText(eDeuda.getVencido8Dias().toString());
        txt25diasvencido.setText(eDeuda.getVencido25Dias().toString());
        txt40diasvencido.setText(eDeuda.getVencido40Dias().toString());
        txtnombrecliente.setText(eDeuda.getNombreCliente().toString());
        txtlineacredito.setText(eDeuda.getLinea().toString());
        txtlcodigocliente.setText(eDeuda.getId_Agenda().toString());


        // Return the completed view to render on screen
        return convertView;
    }
}
