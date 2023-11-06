package com.silvestre.erp_mobile.adaptadores;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.fragment_listadocategoria_item;
import com.silvestre.erp_mobile.fragment_listadocategorias;
import com.silvestre.erp_mobile.model.tbl01cat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_listacategorias extends RecyclerView.Adapter<adapter_listacategorias.MyViewHolder> {
    private ArrayList<tbl01cat> lstcat;
    private Context mContext;
    private fragment_listadocategorias Fragmento;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtcategoria;
        TextView txtsubtitulo;
        ImageView imgCategoria;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtcategoria = (TextView) itemView.findViewById(R.id.plantilla_cardview_cat_nomcat);
            this.txtsubtitulo = (TextView) itemView.findViewById(R.id.plantilla_cardview_cat_subtitulo);
            this.imgCategoria = (ImageView) itemView.findViewById(R.id.plantilla_cardview_cat_imagen);
        }
    }

    public adapter_listacategorias(Context contexts,ArrayList<tbl01cat> data,fragment_listadocategorias fragmento) {
        this.mContext = contexts;
        this.lstcat = data;
        this.Fragmento=fragmento;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantilla_cardview_categorias, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        final MyViewHolder myViewHolder = new MyViewHolder(view);


        myViewHolder.imgCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_listadocategoria_item fragmentC = new fragment_listadocategoria_item();

                //fragmentC.setTargetFragment(Fragmento, 1);
                AppCompatActivity activity = (AppCompatActivity) mContext;
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.escenario, fragmentC).addToBackStack(null).commit();
                //getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.escenario, fragmentC).commit();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView txtcategoria = holder.txtcategoria;
        TextView txtsubtitulo = holder.txtsubtitulo;
        ImageView imgCategoria = holder.imgCategoria;

        txtcategoria.setText(lstcat.get(listPosition).getNomCategoria());
        txtsubtitulo.setText(lstcat.get(listPosition).getRuc());
        //imgCategoria.setImageResource(lstcat.get(listPosition).getImage());
        //imgCategoria.setImageResource(R.drawable.factory_x48);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(lstcat.get(listPosition).getImagen(), 0, lstcat.get(listPosition).getImagen().length);
        //imgCategoria.setImageBitmap(bitmap);
        String url=lstcat.get(listPosition).getUrlImagen();
        Picasso.get().load(url).into(imgCategoria);

        /*
        imgCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Origen", "CATEGORIAS");
                //bundle.putString("subfam",txtsubfamilia.getText().toString().substring(0,5));
                //fragment_listadocategoria_item fragment = new fragment_listadocategoria_item();


            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return lstcat.size();
    }
}
