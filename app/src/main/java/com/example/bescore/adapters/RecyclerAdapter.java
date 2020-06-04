package com.example.bescore.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bescore.R;
import com.example.bescore.controlers.PantallaComentarios;
import com.example.bescore.controlers.PantallaMensajes;
import com.example.bescore.models.Estado;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TablonViewHolder>  implements View.OnClickListener {

    private ArrayList<Estado> listaEstados;
    private View.OnClickListener listener;

    public RecyclerAdapter(ArrayList<Estado> listaEstados) {
        this.listaEstados = listaEstados;
    }

    @NonNull
    @Override
    public TablonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//llenamos los datos de los elementos del recycler

        Context context = parent.getContext();
        int layoutId = R.layout.lista_holder;//hay qe crear uno nuevo sin botones
        LayoutInflater inflador = LayoutInflater.from(context);
        boolean attach = false;
        View view = inflador.inflate(layoutId, parent, attach);

        TablonViewHolder tablonViewHolder = new TablonViewHolder(view);

        view.setOnClickListener(this);

        return tablonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TablonViewHolder holder, int position) {

        Estado estado = listaEstados.get(position);
        holder.bind(estado);

    }

    @Override
    public int getItemCount() {//contador de elementos del recyclerview. No necesario
        return listaEstados.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }


    class TablonViewHolder extends RecyclerView.ViewHolder{//esta clase me permite crear la vista holder de la lista
        TextView tvTextoEstado;
        TextView tvRemitente;
        ImageButton btnComentar;
        Context context;

        public TablonViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            tvTextoEstado = itemView.findViewById(R.id.tvTextoEstado);
            tvRemitente = itemView.findViewById(R.id.tv_Contestador);
            btnComentar = itemView.findViewById(R.id.btnComentar);

        }

        void bind(final Estado estado){//el bindeo me permite cargar todas las vistas del recycler, por posicion

            String emisor = estado.getRemitente();
            String texto = estado.getEstado();

            tvTextoEstado.setText(texto);
            tvRemitente.setText(emisor);

            btnComentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PantallaMensajes.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Estado", estado);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }
}
