package com.vazquez.julio.proyectofarru;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;



public class CursosADAPTER extends RecyclerView.Adapter <CursosADAPTER.ViewHolder> {
    List<Curso> cursoList = new ArrayList<>();
    Context context;

    public CursosADAPTER (Context context, List<Curso> cursoList) {
        this.context = context;
        this.cursoList = cursoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load("http://learnforfree.juliovazquez.net/assets/img-products/"+cursoList.get(position).getImagen()).into(holder.imgProducto);
        holder.txtPrecio.setText(String.valueOf(cursoList.get(position).getPrecio()));
        holder.txtMarca.setText(cursoList.get(position).getMarca());
        holder.txtNombre.setText(cursoList.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return cursoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto;
        TextView txtNombre;
        TextView txtMarca;
        TextView txtPrecio;
        CardView cardView;

        public ViewHolder (View item) {
            super(item);
            cardView = (CardView) item.findViewById(R.id.cardView);
            txtMarca = (TextView) item.findViewById(R.id.txtMarca);
            txtPrecio = (TextView) item.findViewById(R.id.txtPrecio);
            txtNombre = (TextView) item.findViewById(R.id.txtNombre);
            imgProducto = (ImageView) item.findViewById(R.id.imgProducto);
            itemView.setTag(itemView);
        }
    }
}
