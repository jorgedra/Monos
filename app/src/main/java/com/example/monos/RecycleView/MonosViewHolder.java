package com.example.monos.RecycleView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.monos.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monos.Clases.Mono;
import com.example.monos.DetallesMonosActivity;
import com.example.monos.MostrarMonosActivity;
import com.example.monos.Utilidades.ImagenesBlopBitmap;

public class MonosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public static final String EXTRA_MONO_ITEM = "com.example.monoviewholder.mono";
    public static final String EXTRA_MONO_IMAGEN = "com.example.monoviewholder.imagenmono";
    public static final String EXTRA_POSICION_CASILLA = "com.example.monoviewholder.posicion";
    // atributos
    private TextView txt_item_nombre;
    private TextView txt_item_id;
    private TextView txt_item_familia;
    private TextView txt_item_region;
    private ImageView img_item_mono;
    //-------------------------------------
    private ListaMonosAdapter lpa;
    private Context contexto;


    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public MonosViewHolder(@NonNull View itemView, ListaMonosAdapter listaProductosAdapter) {
        super(itemView);
        txt_item_id = (TextView) itemView.findViewById(R.id.txt_item_id);
        txt_item_nombre = (TextView) itemView.findViewById(R.id.txt_item_nombre);
        txt_item_familia = (TextView) itemView.findViewById(R.id.txt_item_familia);
        txt_item_region = (TextView) itemView.findViewById(R.id.txt_item_region);
        img_item_mono = (ImageView) itemView.findViewById(R.id.img_item_mono);
        //-----------------------------------------------------------------------------
        lpa = listaProductosAdapter;
        itemView.setOnClickListener(this);
    }

    public TextView getTxt_item_nombre() {
        return txt_item_nombre;
    }

    public void setTxt_item_nombre(TextView txt_item_nombre) {
        this.txt_item_nombre = txt_item_nombre;
    }

    public TextView getTxt_item_id() {
        return txt_item_id;
    }

    public void setTxt_item_id(TextView txt_item_id) {
        this.txt_item_id = txt_item_id;
    }

    public TextView getTxt_item_familia() {
        return txt_item_familia;
    }

    public void setTxt_item_familia(TextView txt_item_familia) {
        this.txt_item_familia = txt_item_familia;
    }

    public TextView getTxt_item_region() {
        return txt_item_region;
    }

    public void setTxt_item_region(TextView txt_item_region) {
        this.txt_item_region = txt_item_region;
    }

    public ImageView getImg_item_mono() {
        return img_item_mono;
    }

    public void setImg_item_mono(ImageView img_item_mono) {
        this.img_item_mono = img_item_mono;
    }


    public ListaMonosAdapter getLpa() {
        return lpa;
    }

    public void setLpa(ListaMonosAdapter lpa) {
        this.lpa = lpa;
    }

    @Override
    public void onClick(View view) {
        int posicion = getLayoutPosition();
        Mono m = lpa.getMonos().get(posicion);
        Intent intent = new Intent(lpa.getContexto(), DetallesMonosActivity.class);
        intent.putExtra(EXTRA_MONO_ITEM,m);
        img_item_mono.buildDrawingCache();
        Bitmap foto_bm = img_item_mono.getDrawingCache();
        intent.putExtra(EXTRA_MONO_IMAGEN , ImagenesBlopBitmap.bitmap_to_bytes_png(foto_bm));
        intent.putExtra(EXTRA_POSICION_CASILLA, posicion);
        Context contexto = lpa.getContexto();
        ((MostrarMonosActivity) contexto).startActivityForResult(intent, MostrarMonosActivity.PETICION1);
    }
}
