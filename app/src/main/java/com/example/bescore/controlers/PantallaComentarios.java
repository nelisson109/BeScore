package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bescore.R;
import com.example.bescore.models.Estado;
import com.example.bescore.models.NombreCompleto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PantallaComentarios extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;

    private TextView tv_NombreCompleto;
    private TextView tv_TextoEstado;
    private Button btnComentar;
    private Button btnLike;
    private EditText et_Comentario;
    private String idEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_comentarios);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tv_NombreCompleto = (TextView) findViewById(R.id.tv_NombreCompleto);
        tv_TextoEstado = (TextView) findViewById(R.id.tv_TextoEstado);
        btnComentar = (Button) findViewById(R.id.btnComentar);
        btnLike = (Button) findViewById(R.id.btnLike);
        et_Comentario = (EditText) findViewById(R.id.et_Comentario);

        Bundle estadoRecibido = getIntent().getExtras();
        if (estadoRecibido!=null){
            Estado estado = (Estado) estadoRecibido.getSerializable("Estado");
            String remitente = estado.getRemitente();
            String textoEstado = estado.getEstado();
            idEstado = estado.getIdEstado();
            tv_NombreCompleto.setText(remitente);
            tv_TextoEstado.setText(textoEstado);
        }
    }


    public void obtenerNombreResponde(View v){//necesito el nombre de la persona que escribe el comentario
        String idUser = mAuth.getCurrentUser().getUid();
        dataBaseReference.child("Users").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                escribirNombreCompleto(nombre, apellidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void escribirNombreCompleto(String nombre, String apellidos){
        String nombreCompleto = nombre + " " + apellidos;
        crearMensaje(nombreCompleto);
    }

    public void crearMensaje(String nombreCompleto){
        String idUser = mAuth.getCurrentUser().getUid();
        String remitente = nombreCompleto;
        String mensaje = et_Comentario.getText().toString();

        Map<String, Object> datosMensaje = new HashMap<>();
        datosMensaje.put("idEstado", idEstado);
        datosMensaje.put("remitente", remitente);
        datosMensaje.put("texto", mensaje);

        dataBaseReference.child("Mensajes").push().setValue(datosMensaje);//subida de datos a BBDD
    }
}
