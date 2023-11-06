package com.silvestre.erp_mobile.adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.silvestre.erp_mobile.R;
import com.silvestre.erp_mobile.model.tbllistadoproducto;

import java.util.ArrayList;
import java.util.Locale;

public class adapter_listaproducto_pedido extends ArrayAdapter<tbllistadoproducto> {
    public ArrayList<tbllistadoproducto> _lista= new ArrayList<tbllistadoproducto>();
    private TextView txtcantidad;
    private TextView txtsubtotal;
    private TextView txttotal2,txtprec;
    private TextView txtprecioTitulo;
    private EditText txtCantidadDecimal,txtNuevoPrecio;
    public adapter_listaproducto_pedido(Context context, ArrayList<tbllistadoproducto> users,TextView total) {
        super(context, 0, users);
        _lista=users;
        txttotal2=total;
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
        //Controles controles=null;
        final tbllistadoproducto eProducto = _lista.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plantilla_listadoproducto_pedido, parent, false);
        }
        // Lookup view for data population
        TextView txtdesv = (TextView) convertView.findViewById(R.id.plantilla_pedido_txtdescproducto);
        //TextView txtund = (TextView) convertView.findViewById(R.id.txtund);
        txtsubtotal = (TextView) convertView.findViewById(R.id.plantilla_pedido_txtsubtotal);
        TextView txtmarc = (TextView) convertView.findViewById(R.id.plantilla_pedido_txtmarca);
        txtprec = (TextView) convertView.findViewById(R.id.plantilla_pedido_txtprecio);
        txtcantidad = (TextView) convertView.findViewById(R.id.plantilla_pedido_txtcantidad);
        //TextView txtstoc = (TextView) convertView.findViewById(R.id.txtstock);
        ImageButton btnEliminar = (ImageButton) convertView.findViewById(R.id.plantilla_pedido_btneliminar);
        ImageButton btnAdicionar = (ImageButton) convertView.findViewById(R.id.plantilla_pedido_btnadicionar);
        ImageButton btnRestar = (ImageButton) convertView.findViewById(R.id.plantilla_pedido_btnrestar);
        ImageButton btnCantidad= (ImageButton) convertView.findViewById(R.id.plantilla_pedido_btnCantidad);


        // Populate the data into the template view using the data object
        txtdesv.setText(eProducto.getDescripcion());
        //txtund.setText(eProducto.getUMedida());
        txtmarc.setText(eProducto.getMarca());
        txtprec.setText(String.valueOf(eProducto.getPrecio()));
        txtcantidad.setText(String.valueOf(eProducto.getCantidad()));
        txtsubtotal.setText(String.valueOf(eProducto.getSubtotal()));


        //txttotal2= (TextView) R.layout.fragment_pedidos convertView.findViewById(R.id.pedidos_txttotal);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "EVENTO ELIMINAR", Toast.LENGTH_SHORT).show();
                _lista.remove(eProducto);
                notifyDataSetChanged();
                CalcularTotal();

            }
        });


        btnRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int cantidad;
                //cantidad= Integer.parseInt(txtcantidad.getText().toString());
                if(eProducto.getCantidad()==1){
                    return;
                }

                //cantidad=Integer.parseInt(txtcantidad.getText().toString())-1;


                txtcantidad.setText(String.valueOf(eProducto.getCantidad()-1));

                double subtotal;
                subtotal=(eProducto.getCantidad()-1)*eProducto.getPrecio();
                //txtsubtotal.setText(String.valueOf(subtotal));
                txtsubtotal.setText(String.format(Locale.US,"%.2f", subtotal));
                //String.format("%.2f", subtotal)
                eProducto.setCantidad((eProducto.getCantidad()-1));
                eProducto.setSubtotal(subtotal);
                CalcularTotal();
                notifyDataSetChanged();

            }
        });
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int cantidad;
                //cantidad=Integer.parseInt(txtcantidad.getText().toString())+1;

                if((eProducto.getCantidad()+1) >eProducto.getStock())
                {
                    Toast.makeText(getContext(), "Supero la cantidad disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                txtcantidad.setText(String.valueOf(eProducto.getCantidad()+1));
                double subtotal;
                subtotal=(eProducto.getCantidad()+1)*eProducto.getPrecio();
                txtsubtotal.setText(String.valueOf(subtotal));
                eProducto.setCantidad(eProducto.getCantidad()+1);
                eProducto.setSubtotal(subtotal);
                CalcularTotal();
                notifyDataSetChanged();
            }
        });

        txtprec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.plantilla_alertdialog_cantidadpedido);
                dialog.setTitle("Nuevo Precio");

                Button dialogbutton= (Button)dialog.findViewById(R.id.plantilla_alertdialog_cantidadpedido_btnCantidad);
                txtNuevoPrecio = (EditText)dialog.findViewById(R.id.plantilla_alertdialog_cantidadpedido_txtcantidad);

                txtprecioTitulo= (TextView)dialog.findViewById(R.id.txtcantidad);
                txtprecioTitulo.setText("Precio S/:");
                dialogbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //txtcantidad.setText("2.2");

                        if((Double.parseDouble(txtNuevoPrecio.getText().toString()))<0)
                        {
                            Toast.makeText(getContext(), "EL precio tiene que ser mayor o igual a 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        txtprec.setText(txtNuevoPrecio.getText().toString());
                        eProducto.setPrecio(Double.parseDouble(txtprec.getText().toString()));
                        double subtotal;
                        subtotal=((Double.parseDouble(txtcantidad.getText().toString())))* Double.parseDouble(txtNuevoPrecio.getText().toString());
                        txtsubtotal.setText(String.format(Locale.US,"%.2f", subtotal));

                        eProducto.setCantidad(Double.parseDouble(txtcantidad.getText().toString()));
                        //eProducto.setSubtotal(subtotal);
                        eProducto.setSubtotal(Double.parseDouble(String.format(Locale.US,"%.2f", subtotal)));
                        CalcularTotal();
                        notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        btnCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.plantilla_alertdialog_cantidadpedido);
                dialog.setTitle("Cantidad");

                Button dialogbutton= (Button)dialog.findViewById(R.id.plantilla_alertdialog_cantidadpedido_btnCantidad);
                txtCantidadDecimal = (EditText)dialog.findViewById(R.id.plantilla_alertdialog_cantidadpedido_txtcantidad);

                dialogbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //txtcantidad.setText("2.2");

                        if((Double.parseDouble(txtCantidadDecimal.getText().toString())) >eProducto.getStock())
                        {
                            Toast.makeText(getContext(), "Supero la cantidad disponible", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtCantidadDecimal.getText().toString().compareTo("")==0)
                        {
                            return;
                        }
                        if(Double.parseDouble(txtCantidadDecimal.getText().toString())<=0)
                        {
                            Toast.makeText(getContext(), "La cantidad tiene que ser mayor a 0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        txtcantidad.setText(txtCantidadDecimal.getText().toString());
                        double subtotal;
                        subtotal=((Double.parseDouble(txtCantidadDecimal.getText().toString())))*eProducto.getPrecio();
                        txtsubtotal.setText(String.format(Locale.US,"%.2f", subtotal));

                        eProducto.setCantidad((Double.parseDouble(txtCantidadDecimal.getText().toString())));
                        //eProducto.setSubtotal(subtotal);
                        eProducto.setSubtotal(Double.parseDouble(String.format(Locale.US,"%.2f", subtotal)));
                        CalcularTotal();
                        notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        //txtstoc.setText(String.valueOf(eProducto.getStock()));
        // Return the completed view to render on screen



        return convertView;
    }

    private void CalcularTotal()
    {
        double total=0;
        for(tbllistadoproducto e :  _lista)
        {
            total=total + (e.getPrecio()*e.getCantidad());
        }
                txttotal2.setText(String.format(Locale.US,"%.2f", total));
    }






}
