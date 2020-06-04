package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bescore.R;
import com.example.bescore.adapters.RecyclerAdapter;
import com.example.bescore.models.Estado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tablon extends AppCompatActivity {
    private RecyclerView recyclerTablon;
    ArrayList<Estado> listaEstados = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private TextView tvTituloTablon;

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablon);

        tvTituloTablon = (TextView) findViewById(R.id.tvTituloTablon);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        establecerIcono();

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/hemi head bd it.ttf");
        tvTituloTablon.setTypeface(fuente);

        recyclerTablon = (RecyclerView) findViewById(R.id.recyclerTablon);
        recyclerTablon.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerTablon.setLayoutManager(linearLayoutManager);

        cargarListaEstados();
    }


    public void cargarListaEstados(){

        dataBaseReference.child("Estados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listaEstados.clear();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String textoEstado = snapshot.child("estado").getValue().toString();
                        String remitente = snapshot.child("remitente").getValue().toString();
                        String idEstado = snapshot.getKey();
                        System.out.println(" idEstado : *****************************++" + idEstado);
                        //Date fecha = (Date) snapshot.child("fecha").getValue();
                        listaEstados.add(new Estado(idEstado, textoEstado, remitente));
                    }
                    cargarRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cargarRecycler(){
        recyclerAdapter = new RecyclerAdapter(listaEstados);
        /*recyclerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tablon.this, PantallaComentarios.class);
                startActivity(i);
            }
        });*/
        recyclerTablon.setAdapter(recyclerAdapter);
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
