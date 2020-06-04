package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bescore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {//Controlador del LOGIN

    private EditText etUsuario;
    private EditText tvPassword;
    private Button btnEntrar;
    private TextView tvTitulo;

    private static final String TAG = "Antut" ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;//comprueba que user and pass sean correctos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        tvTitulo = (TextView) findViewById(R.id.tvTitulo);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/hemi head bd it.ttf");
        tvTitulo.setTypeface(fuente);

        mAuth = FirebaseAuth.getInstance();


        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){

                    startActivity(new Intent(MainActivity.this,Home.class));

                }
                else {
                     Toast.makeText(MainActivity.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();

                }
            }
        };*/


    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
*/


    public void entrar(View v){
        String usuario = etUsuario.getText().toString();
        String password = tvPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            Intent i = new Intent(MainActivity.this, Perfil.class);
                            startActivity(i);

                        }else{
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void crearCuenta(View v){
        Intent i = new Intent(this,NuevaCuenta.class);
        startActivity(i);
    }

}
