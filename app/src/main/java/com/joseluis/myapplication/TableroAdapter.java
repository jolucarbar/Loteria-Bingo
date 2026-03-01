package com.joseluis.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TableroAdapter extends RecyclerView.Adapter<TableroAdapter.TableroViewHolder> {

    // Esta lista guardará el estado de cada número (true = ya salió, false = no ha salido)
    private List<Boolean> marcados;

    // Constructor: recibe la lista desde el MainActivity
    public TableroAdapter(List<Boolean> marcados) {
        this.marcados = marcados;
    }

    // 1. Infla (convierte en código) el diseño XML que creamos en el paso 1
    @NonNull
    @Override
    public TableroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tablero, parent, false);
        return new TableroViewHolder(view);
    }

    // 2. Aquí es donde le damos vida a cada celda individual
    @Override
    public void onBindViewHolder(@NonNull TableroViewHolder holder, int position) {
        // La posición en las listas va de 0 a 89, pero nuestros números son de 1 a 90
        int numeroReal = position + 1;
        holder.tvNumeroCelda.setText(String.valueOf(numeroReal));

        // Comprobamos si este número ya salió en la lotería
        if (marcados.get(position)) {
            // Si ya salió: Fondo rojo, texto blanco
            holder.tvNumeroCelda.setBackgroundColor(Color.parseColor("#D32F2F"));
            holder.tvNumeroCelda.setTextColor(Color.WHITE);
        } else {
            // Si no ha salido: Fondo gris claro, texto negro
            holder.tvNumeroCelda.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tvNumeroCelda.setTextColor(Color.BLACK);
        }
    }

    // 3. Le decimos al RecyclerView cuántos elementos hay en total
    @Override
    public int getItemCount() {
        return marcados.size(); // Siempre será 90
    }

    // Clase interna que "sostiene" la vista de la celda
    public static class TableroViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroCelda;

        public TableroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroCelda = itemView.findViewById(R.id.tvNumeroCelda);
        }
    }
}