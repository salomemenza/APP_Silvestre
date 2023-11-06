package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.fragment_letras_historial;
import com.silvestre.erp_mobile.fragment_letras_historial_detalle;
import com.silvestre.erp_mobile.model.letraEmitidaResumen;

public class adapter_letras_historial extends RecyclerView.Adapter<adapter_letras_historial.ViewHolder> {
    private letraEmitidaResumen[] letrasEmitidas;
    private Context mContext;
    private fragment_letras_historial fragment;

    public adapter_letras_historial(Context context, letraEmitidaResumen[] letrasEmitidas, fragment_letras_historial fragment) {
        this.letrasEmitidas = letrasEmitidas;
        this.mContext = context;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId;
        TextView textViewObs;
        TextView textViewFecha;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.cardTitle);
            textViewObs = itemView.findViewById(R.id.cardSubtitle);
            textViewFecha = itemView.findViewById(R.id.cardDate);
            cv = itemView.findViewById(R.id.cv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.plantilla_cardview_letras_historial_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final letraEmitidaResumen letrasEmitidasList = letrasEmitidas[position];

        holder.textViewId.setText("Nro: "+letrasEmitidasList.getIdSeguimiento());
        holder.textViewObs.setText(letrasEmitidasList.getObservacion());
        holder.textViewFecha.setText("Fecha: "+letrasEmitidasList.getFechaRegistro());

        holder.cv.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition));
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Has seleccionado el ID: " + letrasEmitidasList.getIdSeguimiento(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("id", letrasEmitidasList.getIdSeguimiento());
                bundle.putString("fecha", letrasEmitidasList.getFechaRegistro());

                final FragmentTransaction transaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

                fragment_letras_historial_detalle fragmentNext= new fragment_letras_historial_detalle();
                fragmentNext.setArguments(bundle);


                transaction.addToBackStack(null);
                transaction.replace(R.id.escenario,fragmentNext).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return letrasEmitidas.length;
    }


}
