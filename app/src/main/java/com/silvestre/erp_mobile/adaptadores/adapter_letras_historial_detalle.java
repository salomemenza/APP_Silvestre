package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.fragment_letras_historial_detalle;
import com.silvestre.erp_mobile.model.letraEmitida;

public class adapter_letras_historial_detalle extends RecyclerView.Adapter<adapter_letras_historial_detalle.ViewHolder> {

    private letraEmitida[] letraEmitida;
    private Context mContext;
    private fragment_letras_historial_detalle fragment;

    public adapter_letras_historial_detalle(Context context, letraEmitida[] letraEmitida, fragment_letras_historial_detalle fragment) {
        this.letraEmitida = letraEmitida;
        this.mContext = context;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId;
        TextView textViewObs;
        TextView textViewFecha;
        TextView textViewRecepcionado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.cardTitle);
            textViewObs = itemView.findViewById(R.id.cardSubtitle);
            textViewFecha = itemView.findViewById(R.id.cardDate);
            textViewRecepcionado = itemView.findViewById(R.id.cardRecepcionado);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.plantilla_cardview_letras_historial_detalle_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_letras_historial_detalle.ViewHolder holder, int position) {
        final letraEmitida letraEmitidaList = letraEmitida[position];

        String recepcionado = letraEmitidaList.flagRecepcionado ? "Enviado":"Aceptado";

        Log.d("letraEmitidaResumenList {}:", letraEmitidaList.getIdletra().toString());
        holder.textViewId.setText("Nro: "+ letraEmitidaList.getIdletra().toString());
        holder.textViewObs.setText("Nro de cuota: "+ letraEmitidaList.getNroCuota().toString());
        holder.textViewFecha.setText("Fecha Vcmto: "+ letraEmitidaList.getVencimiento().toString());
        holder.textViewRecepcionado.setText("Estado: "+ recepcionado);
        /*holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, letraEmitidaList.getId(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return letraEmitida.length;
    }
}
