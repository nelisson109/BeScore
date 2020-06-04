package com.example.bescore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bescore.R;
import com.example.bescore.models.Mensaje;

import java.util.ArrayList;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajesViewHolder> implements View.OnClickListener {

    private ArrayList<Mensaje> listaMensajes;
    private View.OnClickListener listener;

    public MensajesAdapter(ArrayList<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.lista_mensajes_holder;
        LayoutInflater inflador = LayoutInflater.from(context);
        boolean attach = false;
        View view = inflador.inflate(layoutId, parent, attach);

        MensajesViewHolder mensajesViewHolder = new MensajesViewHolder(view);

        view.setOnClickListener(this);

        return mensajesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, int position) {

        Mensaje mensaje = listaMensajes.get(position);
        holder.bind(mensaje);

    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    class MensajesViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView tv_Contestador;
        TextView tv_TextoMensaje;

        public MensajesViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            tv_Contestador = itemView.findViewById(R.id.tv_Contestador);
            tv_TextoMensaje = itemView.findViewById(R.id.tv_TextoMensaje);
        }

        void bind(Mensaje mensaje){

            String nombre = mensaje.getRemitente();
            String texto = mensaje.getTexto();

            tv_Contestador.setText(nombre);
            tv_TextoMensaje.setText(texto);
        }
    }
}
