package com.example.ta4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private List<Categoria> listaCategorias;

    public CategoriaAdapter(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_items_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoriaActual = listaCategorias.get(position);
        holder.textViewNombre.setText(categoriaActual.getNombre());
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.item_nombre_categoria);
        }
    }
}