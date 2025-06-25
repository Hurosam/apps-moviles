package com.example.ta4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Gasto> listaGastos;

    public RecyclerAdapter(List<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_items_gasto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gasto gastoActual = listaGastos.get(position);
        holder.textViewMonto.setText("S/ " + String.format("%.2f", gastoActual.getMonto()));
        holder.textViewCategoria.setText(gastoActual.getCategoria());
        holder.textViewFecha.setText(gastoActual.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMonto;
        TextView textViewCategoria;
        TextView textViewFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMonto = itemView.findViewById(R.id.item_monto);
            textViewCategoria = itemView.findViewById(R.id.item_categoria);
            textViewFecha = itemView.findViewById(R.id.item_fecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MainActivity6.class);
                    intent.putExtra("GASTO_POSITION", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}