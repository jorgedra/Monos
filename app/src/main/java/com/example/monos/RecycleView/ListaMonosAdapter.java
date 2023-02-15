package com.example.monos.RecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.monos.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monos.Clases.Mono;
import com.example.monos.Utilidades.ImagenesFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListaMonosAdapter extends RecyclerView.Adapter<MonosViewHolder> {

    private Context contexto = null;
    private ArrayList<Mono> monos = null;
    private LayoutInflater inflate = null;

    private FirebaseAuth mAuth;

    public ListaMonosAdapter(Context contexto, ArrayList<Mono> monos ) {
        this.contexto = contexto;
        this.monos = monos;
        inflate =  LayoutInflater.from(this.contexto);
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<Mono> getMonos() {
        return monos;
    }

    public void setMonos(ArrayList<Mono> monos) {
        this.monos = monos;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MonosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflate.inflate(R.layout.item_rv_mono,parent,false);
        MonosViewHolder mvh = new MonosViewHolder(mItemView,this);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MonosViewHolder holder, int position) {
        Mono m = this.getMonos().get(position);
        //        cargo la imagen desde la base de datos
        //-----------------------------------------------------------------
        String carpeta = m.getNombre();
        ImageView imagen = holder.getImg_item_mono() ;
        ImagenesFirebase.descargarFoto(carpeta,m.getNombre(),imagen);
        ImageView imagen1 = imagen;
        holder.setImg_item_mono(imagen1);
        //----------------------------------------------------------------------
        holder.getTxt_item_id().setText("Id " + String.valueOf(m.getIdMono()));
        holder.getTxt_item_nombre().setText("Nombre: " + m.getNombre());
        holder.getTxt_item_familia().setText("Familia: " + m.getFamilia());
        holder.getTxt_item_region().setText("Region: " + m.getRegion());


    }


    @Override
    public int getItemCount() {
        return this.monos.size();
    }

    public void addMono(Mono monoAñadido) {
        monos.add(monoAñadido);
        notifyDataSetChanged();
    }
}
