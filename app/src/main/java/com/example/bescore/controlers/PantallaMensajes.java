package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bescore.R;
import com.example.bescore.adapters.MensajesAdapter;
import com.example.bescore.adapters.RecyclerAdapter;
import com.example.bescore.models.Estado;
import com.example.bescore.models.Mensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PantallaMensajes extends AppCompatActivity {
    private RecyclerView recyclerMensajes;
    ArrayList<Mensaje> listaMensajes = new ArrayList<>();
    private MensajesAdapter mensajesAdapter;

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;

    private TextView tvNombreCompleto;
    private TextView tvEstado;
    private EditText et_Comentar;
    private Button btnGuardarComentario;
    private String idEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mensajes);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tvNombreCompleto = (TextView) findViewById(R.id.tvNombreCompleto);
        tvEstado = (TextView) findViewById(R.id.tvEstado);
        et_Comentar = (EditText) findViewById(R.id.et_Comentar);
        btnGuardarComentario = (Button) findViewById(R.id.btnGuardarComentario);

        establecerIcono();

        Bundle estadoRecibido = getIntent().getExtras();
        if (estadoRecibido!=null){
            Estado estado = (Estado) estadoRecibido.getSerializable("Estado");
            String remitente = estado.getRemitente();
            String textoEstado = estado.getEstado();
            idEstado = estado.getIdEstado();
            tvNombreCompleto.setText(remitente);
            tvEstado.setText(textoEstado);
        }

        recyclerMensajes = (RecyclerView) findViewById(R.id.recyclerMensajes);
        recyclerMensajes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerMensajes.setLayoutManager(linearLayoutManager);

        cargarListaMensajes();

    }

    public void cargarListaMensajes(){

        dataBaseReference.child("Mensajes").orderByChild("idEstado").equalTo(idEstado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listaMensajes.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String textoMensaje = snapshot.child("texto").getValue().toString();
                        String remitente = snapshot.child("remitente").getValue().toString();
                        String idEstado = snapshot.getKey();
                        //Date fecha = (Date) snapshot.child("fecha").getValue();
                        listaMensajes.add(new Mensaje(idEstado, remitente, textoMensaje));
                    }
                    cargarRecyclerMensajes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cargarRecyclerMensajes(){
        mensajesAdapter = new MensajesAdapter(listaMensajes);
        recyclerMensajes.setAdapter(mensajesAdapter);
    }

    public void obtenerNombreRespondedor(View v){//necesito el nombre de la persona que escribe el comentario
        String idUser = mAuth.getCurrentUser().getUid();
        dataBaseReference.child("Users").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                generarNombreCompleto(nombre, apellidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void generarNombreCompleto(String nombre, String apellidos){
        String nombreCompleto = nombre + " " + apellidos;
        subirMensaje(nombreCompleto);
    }

    public void subirMensaje(String nombreCompleto){
        String idUser = mAuth.getCurrentUser().getUid();
        String remitente = nombreCompleto;
        String mensaje = et_Comentar.getText().toString();

        Map<String, Object> datosMensaje = new HashMap<>();
        datosMensaje.put("idEstado", idEstado);
        datosMensaje.put("remitente", remitente);
        datosMensaje.put("texto", mensaje);

        dataBaseReference.child("Mensajes").push().setValue(datosMensaje);//subida de datos a BBDD
        et_Comentar.setText(" ");
    }


    //METODOS NECESARIOS PARA LA BARRA DE MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            lanzarAjustes();
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe();
            return true;
        }
        if (id == R.id.tablon) {
            lanzarTablon();
            return true;
        }
        if (id == R.id.perfil) {
            lanzarPerfil();
            return true;
        }
        if (id == R.id.cerrarSesion) {
            mAuth.signOut();
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe() {
        Intent intent = new Intent(this, AcercaDe.class);
        startActivity(intent);
    }

    public void lanzarAjustes(){
        Intent intent = new Intent(this, Ajustes.class);
        startActivity(intent);
    }

    public void lanzarTablon(){
        Intent intent = new Intent(this, Tablon.class);
        startActivity(intent);
    }

    public void lanzarPerfil(){
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }

    public void establecerIcono(){
        //Obtenemos la ActionBar instalada por AppCompatActivity
        ActionBar actionBar = getSupportActionBar();

        //Establecemos el icono en la ActionBar
        actionBar.setIcon(R.mipmap.ic_be_score);
        actionBar.setDisplayShowHomeEnabled(true);

        // Activar flecha ir atrás (ir a la Parent Activity declarada en el manifest)
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
