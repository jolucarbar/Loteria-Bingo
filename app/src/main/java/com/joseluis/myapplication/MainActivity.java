package com.joseluis.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Variables de lógica
    private List<Integer> bombo = new ArrayList<>();
    private List<Boolean> marcados = new ArrayList<>();
    private LinkedList<Integer> historial = new LinkedList<>(); // LinkedList facilita añadir al principio

    // Variables de interfaz
    private TextView tvNumeroActual;
    private TableroAdapter tableroAdapter;
    private HistorialAdapter historialAdapter;

    // Variable para la voz
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Enlazar vistas
        tvNumeroActual = findViewById(R.id.tvNumeroActual);
        Button btnExtraer = findViewById(R.id.btnExtraer);
        Button btnReiniciar = findViewById(R.id.btnReiniciar);
        RecyclerView rvTablero = findViewById(R.id.rvTablero);
        RecyclerView rvHistorial = findViewById(R.id.rvHistorial);

        // 2. Configurar el Tablero Principal
        // Llenamos la lista de marcados con 90 'false' iniciales
        for (int i = 0; i < 90; i++) {
            marcados.add(false);
        }
        tableroAdapter = new TableroAdapter(marcados);
        rvTablero.setAdapter(tableroAdapter);

        // 3. Configurar el Historial Lateral
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));
        historialAdapter = new HistorialAdapter(historial);
        rvHistorial.setAdapter(historialAdapter);

        // 4. Inicializar el juego por primera vez
        reiniciarJuego();

        // 5. Configurar botones
        btnExtraer.setOnClickListener(v -> extraerNumero());
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());

        // Inicializamos el TextToSpeech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Configuramos el idioma (Español)
                Locale localeEspanol = new Locale("es", "ES");
                int result = tts.setLanguage(localeEspanol);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "El idioma de voz no está soportado en este dispositivo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Fallo al inicializar la voz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void extraerNumero() {
        if (!bombo.isEmpty()) {
            int numeroSorteado = bombo.remove(0);

            tvNumeroActual.setText(String.valueOf(numeroSorteado));

            marcados.set(numeroSorteado - 1, true);
            tableroAdapter.notifyItemChanged(numeroSorteado - 1);

            historial.addFirst(numeroSorteado);
            if (historial.size() > 5) {
                historial.removeLast();
            }
            historialAdapter.notifyDataSetChanged();

            // --- NUEVO: Hacer que la app cante el número ---
            if (tts != null) {
                // QUEUE_FLUSH hace que si pulsas muy rápido, corte el número anterior y diga el nuevo
                tts.speak("El " + numeroSorteado, TextToSpeech.QUEUE_FLUSH, null, null);
            }

        } else {
            Toast.makeText(this, "¡Ya salieron todos los números!", Toast.LENGTH_LONG).show();
        }
    }

    private void reiniciarJuego() {
        // Limpiamos y rellenamos el bombo con los 90 números
        bombo.clear();
        for (int i = 1; i <= 90; i++) {
            bombo.add(i);
        }
        Collections.shuffle(bombo); // ¡Mezclamos las bolas!

        // Limpiamos el tablero
        for (int i = 0; i < 90; i++) {
            marcados.set(i, false);
        }
        tableroAdapter.notifyDataSetChanged();

        // Limpiamos el historial
        historial.clear();
        historialAdapter.notifyDataSetChanged();

        // Reiniciamos el texto principal
        tvNumeroActual.setText("--");

        Toast.makeText(this, "¡Partida reiniciada! Mucha suerte.", Toast.LENGTH_SHORT).show();
    }

    // ========================================================================
    // CLASE INTERNA: Adaptador súper sencillo para el historial
    // ========================================================================
    private static class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {
        private List<Integer> historial;

        public HistorialAdapter(List<Integer> historial) {
            this.historial = historial;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Creamos un TextView simple por código para no tener que hacer otro archivo XML
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(8, 16, 8, 16);
            tv.setTextSize(18f);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv.setText(String.valueOf(historial.get(position)));
            // Hacemos que el último número (el primero de la lista) destaque un poco más
            if (position == 0) {
                holder.tv.setTypeface(null, android.graphics.Typeface.BOLD);
                holder.tv.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
            } else {
                holder.tv.setTypeface(null, android.graphics.Typeface.NORMAL);
                holder.tv.setTextColor(android.graphics.Color.BLACK);
            }
        }

        @Override
        public int getItemCount() {
            return historial.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView;
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Apagamos y liberamos el motor de voz para evitar fugas de memoria
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}