package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.bescore.R;
import com.example.bescore.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Ajustes extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;

    private TextView tvNombreActual;
    private EditText etNombreNuevo;
    private TextView tvApellidosActuales;
    private EditText etApellidosNuevos;
    private EditText etPasswordNuevo;
    private EditText etPasswordRepite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tvNombreActual = (TextView) findViewById(R.id.etNombreActual);
        etNombreNuevo = (EditText) findViewById(R.id.etNombreNuevo);
        tvApellidosActuales = (TextView) findViewById(R.id.etApellidosActuales);
        etApellidosNuevos = (EditText) findViewById(R.id.etApellidosNuevos);
        etPasswordNuevo = (EditText) findViewById(R.id.etPasswordNuevo);
        etPasswordRepite = (EditText) findViewById(R.id.etPasswordRepite);
        establecerIcono();

        cargarDatosUser();
    }

    public void actualizarNombre(View v) {
        String idUser = mAuth.getCurrentUser().getUid();
        String nombre = etNombreNuevo.getText().toString();

        HashMap<String, Object> values = new HashMap<>();
        values.put("nombre", nombre);
        dataBaseReference.child("Users").child(idUser).updateChildren(values);
        cambioCorrecto();
        cargarDatosUser();
        etNombreNuevo.setText(" ");
    }

    public void actualizarApellido(View v) {
        String idUser = mAuth.getCurrentUser().getUid();
        String apellidos = etApellidosNuevos.getText().toString();

        HashMap<String, Object> values = new HashMap<>();
        values.put("apellidos", apellidos);
        dataBaseReference.child("Users").child(idUser).updateChildren(values);
        cambioCorrecto();
        cargarDatosUser();
        etApellidosNuevos.setText(" ");
    }

    public void actualizarPassword(View v) {
        String idUser = mAuth.getCurrentUser().getUid();
        String password, passwordRepite;
        password = etPasswordNuevo.getText().toString().trim();
        passwordRepite = etPasswordRepite.getText().toString().trim();
        FirebaseUser user;
        user = mAuth.getCurrentUser();
        if (TextUtils.isEmpty(password))
            return;
        if (user!=null) {
            HashMap<String, Object> values = new HashMap<>();
            values.put("password", password);
            dataBaseReference.child("Users").child(idUser).updateChildren(values);
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        cambioCorrecto();
                    }
                }
            });
            etPasswordNuevo.setText(" ");
            etPasswordRepite.setText(" ");
        }else{
            cambioIncorrecto();
            etPasswordNuevo.setText(" ");
            etPasswordRepite.setText(" ");
        }
        cargarDatosUser();
    }

    public void cargarDatosUser() {
        String id = mAuth.getCurrentUser().getUid();
        System.out.println("El id del usuario: " + id);

        dataBaseReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                cargarAjustes(user);

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

    public void cargarAjustes(Usuario user) {
        String nombre = user.getNombre();
        tvNombreActual.setText("" + nombre);

        String apellidos = user.getApellidos();
        tvApellidosActuales.setText("" + apellidos);
    }

    public void cambioCorrecto() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("CAMBIOS CONFIRMADOS");
        alerta.setMessage("Los cambios realizados han sido guardados correctamente.");
        alerta.setPositiveButton("OK", null);
        alerta.create();
        alerta.show();
    }

    public void cambioIncorrecto() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("CAMBIOS NO REALIZADOS");
        alerta.setMessage("Los cambios no han podido ser realizados correctamente, pruebe nuevamente si quiere que los cambios se guarde.");
        alerta.setPositiveButton("OK", null);
        alerta.create();
        alerta.show();
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
