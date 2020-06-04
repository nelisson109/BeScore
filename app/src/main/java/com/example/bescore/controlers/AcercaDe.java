package com.example.bescore.controlers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bescore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcercaDe extends AppCompatActivity {

    private TextView tvAcercaDe;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        tvAcercaDe = (TextView) findViewById(R.id.tvAcercaDe);
        mAuth = FirebaseAuth.getInstance();

        establecerIcono();
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
