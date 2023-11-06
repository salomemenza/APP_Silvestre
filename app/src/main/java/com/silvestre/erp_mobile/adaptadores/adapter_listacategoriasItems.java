package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.fragment_listadocategoria_item;
import com.silvestre.erp_mobile.model.tbl01items;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class adapter_listacategoriasItems extends RecyclerView.Adapter<adapter_listacategoriasItems.MyViewHolder> {
    private ArrayList<tbl01items> lstcatItems;
    private Context mContext;
    private fragment_listadocategoria_item Fragment;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtnombre;
        TextView txtmarca;
        TextView txtprecio;
        ImageView imgCategoriaItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtnombre = (TextView) itemView.findViewById(R.id.plantilla_cardview_catitem_nombre);
            this.txtmarca = (TextView) itemView.findViewById(R.id.plantilla_cardview_catitem_marca);
            this.imgCategoriaItem = (ImageView) itemView.findViewById(R.id.plantilla_cardview_catitem_imagen);
            this.txtprecio=(TextView) itemView.findViewById(R.id.plantilla_cardview_catitem_precio);
        }
    }

    public adapter_listacategoriasItems(Context contexts, ArrayList<tbl01items> data, fragment_listadocategoria_item fragment) {
        this.lstcatItems = data;
        this.mContext=contexts;
        this.Fragment=fragment;
    }

    @Override
    public adapter_listacategoriasItems.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantilla_cardview_categorias_items, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        adapter_listacategoriasItems.MyViewHolder myViewHolder = new adapter_listacategoriasItems.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final adapter_listacategoriasItems.MyViewHolder holder, final int listPosition) {

        TextView txtnombre = holder.txtnombre;
        TextView txtmarca = holder.txtmarca;
        TextView txtprecio= holder.txtprecio;

        ImageView imgCategoriaitem = holder.imgCategoriaItem;

        txtnombre.setText(lstcatItems.get(listPosition).getNombre());
        txtmarca.setText(lstcatItems.get(listPosition).getMarca());
        txtprecio.setText("S/ " + (String.format(Locale.US,"%.2f",lstcatItems.get(listPosition).getPrecio())));
        String url=lstcatItems.get(listPosition).getUrlImagen();
        Picasso.get().load(url).into(imgCategoriaitem);

    }

    @Override
    public int getItemCount() {
        return lstcatItems.size();
    }
}
