package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bescore.R;
import com.example.bescore.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {
    private TextView tvPrueba;
    private TextView tv_Apellidos;
    private TextView tv_Email;
    private TextView tvTituloPerfil;
    private TextView tv_Descripcion;
    private EditText et_Estado;

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvPrueba = (TextView) findViewById(R.id.tv_Nombre);
        tv_Apellidos = (TextView) findViewById(R.id.tv_Apellidos);
        tv_Email = (TextView) findViewById(R.id.tv_Email);
        tv_Descripcion = (TextView) findViewById(R.id.tv_Descripcion);
        et_Estado = (EditText) findViewById(R.id.et_Estado);
        tvTituloPerfil = (TextView) findViewById(R.id.tvTituloPerfil);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/hemi head bd it.ttf");
        tvTituloPerfil.setTypeface(fuente);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        establecerIcono();

        cargarDatosUser();
    }

    public void cargarDatosUser() {
        String id = mAuth.getCurrentUser().getUid();
        System.out.println("El id del usuario: " + id);

        dataBaseReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                cargarPerfil(user);

                /*String nombre = dataSnapshot.child("nombre").getValue().toString();
                String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                String email = dataSnapshot.child("usuario").getValue().toString();
                String idTablon = dataSnapshot.child("idTablon").getValue().toString();
                cargarPerfil(nombre, apellidos, email);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void cargarPerfil(Usuario user) {
        String nombre = user.getNombre();
        tvPrueba.setText("" + nombre);
        System.out.println("********************Nombre: " + nombre);

        String apellidos = user.getApellidos();
        tv_Apellidos.setText("" + apellidos);
        System.out.println("********************apellidos: " + apellidos);

        String email = user.getUsuario();
        tv_Email.setText("" + email);
        System.out.println("********************email: " + email);

        String descripcion = user.getDescripcion();
        tv_Descripcion.setText("" + descripcion);
        System.out.println("********************email: " + descripcion);//se pueden borrar
    }


    public void crearEstado(View v) {
        String estado = et_Estado.getText().toString();

        String id = mAuth.getCurrentUser().getUid();
        String name = tvPrueba.getText().toString();
        String apellidos = tv_Apellidos.getText().toString();
        String remitente = name + " " + apellidos;
        //Date fecha = new Date();

        Map<String, Object> datosEstado = new HashMap<>();
        datosEstado.put("idUser", id);
        datosEstado.put("remitente", remitente);
        datosEstado.put("estado", estado);
        //datosEstado.put("fecha", fecha);
        //puede ser conveniente pasar tb el idTablon

        dataBaseReference.child("Estados").push().setValue(datosEstado);//subida de datos a BBDD

        abrirTablon();
    }

    public void abrirTablon() {
        Intent i = new Intent(this, Tablon.class);
        startActivity(i);
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
        actionBar.setIcon(R.mipmap.ic_be_score_round);
        actionBar.setDisplayShowHomeEnabled(true);

        // Activar flecha ir atrás (ir a la Parent Activity declarada en el manifest)
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
