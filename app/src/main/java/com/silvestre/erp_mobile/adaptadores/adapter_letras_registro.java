package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.model.letraEmitida;

import java.util.List;

public class adapter_letras_registro extends RecyclerView.Adapter<adapter_letras_registro.ViewHolder> {

    private List<letraEmitida> listaObjeto;
    private Context context;

    public adapter_letras_registro(List<letraEmitida> listaObjeto, Context context) {
        this.listaObjeto = listaObjeto;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido, txtEdad;
        ImageButton btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido = itemView.findViewById(R.id.txtApellido);
            txtEdad = itemView.findViewById(R.id.txtEdad);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plantilla_cardview_letras_registro_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final letraEmitida objeto = listaObjeto.get(position);

        holder.txtNombre.setText(objeto.getNroLetra());
        holder.txtApellido.setText("Importe: "+objeto.getImporte().toString());
        holder.txtEdad.setText("Nro Cuota: "+objeto.getNroCuota().toString());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    removeItem(adapterPosition);
                }
            }
        });

        Log.d("MiApp", "Datos asignados a posición " + position + " Id_Letra = " + objeto.getIdletra() + ": Nro Letra=" + objeto.getNroLetra() + ", Importe = " + objeto.getImporte().toString() + ", Nro Cuota = " + objeto.getNroCuota());
    }

    private void removeItem(int position) {
        if (position >= 0 && position < listaObjeto.size()) {
            listaObjeto.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listaObjeto.size());
        }
    }

    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }


}
