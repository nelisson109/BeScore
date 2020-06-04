package com.example.bescore.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bescore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NuevaCuenta extends AppCompatActivity {//CONTROLADOR DEL FORMULARIO DE ALTA
    private static final String TAG = "Autentificacion";
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etCorreo;
    private EditText etContraseña;
    private EditText etRepite;
    private EditText etDescripcion;
    private TextView tvTituloNuevaCuenta;

    FirebaseAuth mAuth;
    DatabaseReference dataBaseReference;

    private String usuario;
    private String password;
    private String nombre;
    private String apellidos;
    private String repite;
    private String descripcion;

    /*
     * ArrayList<String?> listaAmigos = new ArrayList(); con sus getters y setters
     * ArrayList<String> listaComentarios = new ArrayList(); con getters y setters
     * puede hacer falta un array de fotos o lista de fotos
     * puede hacer falta un array de videos o lista de videos
     * puede hacer falta un array de retos o lista de retos
     * lista de estados, se mostrará el ultimo
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etContraseña = (EditText) findViewById(R.id.etConttraseña);
        etRepite = (EditText) findViewById(R.id.etRepite);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        tvTituloNuevaCuenta = (TextView) findViewById(R.id.tvTituloNuevaCuenta);

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/hemi head bd it.ttf");
        tvTituloNuevaCuenta.setTypeface(fuente);

    }


    public void crearCuenta(View v) {//este metodo comprueba que los campos se rellenen correctamente y llama al registro

        nombre = etNombre.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        usuario = etCorreo.getText().toString().trim();
        password = etContraseña.getText().toString().trim();
        repite = etRepite.getText().toString().trim();
        descripcion = etDescripcion.getText().toString().trim();

        if (!nombre.isEmpty() && !apellidos.isEmpty() && !usuario.isEmpty() && !password.isEmpty() && !repite.isEmpty()) {
            if (password.length() >= 6) {
                if (!password.equals(repite)) {
                    Toast.makeText(this, "Debes introducir la misma contraseña", Toast.LENGTH_LONG).show();
                } else {
                    registrarUsuario();
                }
            } else {
                Toast.makeText(this, "La contraseña debe tener 6 caracteres", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Debe completar los campos", Toast.LENGTH_LONG).show();
        }

    }


    public void registrarUsuario() {//este metodo crea el nuevo usuario y lo almacena en BBDD

        mAuth.createUserWithEmailAndPassword(usuario, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("nombre", nombre);
                    parametros.put("apellidos", apellidos);
                    parametros.put("usuario", usuario);
                    parametros.put("password", password);
                    parametros.put("descripcion", descripcion);
                    String idUser = mAuth.getCurrentUser().getUid();

                    dataBaseReference.child("Users").child(idUser).setValue(parametros).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                crearTablon(nombre);
                                //Intent intent = new Intent(NuevaCuenta.this, Home.class);
                                Intent intent = new Intent(NuevaCuenta.this, Perfil.class);
                                //intent.putExtra("dataBaseReference", dataBaseReference);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(NuevaCuenta.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(NuevaCuenta.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                }
            }
        });
    }


    public void crearTablon(String nombre) {
        String id = mAuth.getCurrentUser().getUid();
        String comentario;
        comentario = "Su nombre es... " + nombre;//Comentario de pruebas
        Log.e("NombreUser ", " " + nombre);

        Map<String, Object> datosTablon = new HashMap<>();
        datosTablon.put("idUser", id);
        datosTablon.put("nombre", nombre);
        datosTablon.put("comentario", comentario);

        dataBaseReference.child("Tablon").push().setValue(datosTablon);//subida de datos a BBDD
        dataBaseReference.child("Tablon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//Recorrer los hijos de Tablon
                    String claveTablon = snapshot.getKey();
                    completarUser(claveTablon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void completarUser(String idTablon) {
        String id = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> values = new HashMap<>();
        values.put("idTablon", idTablon);

        dataBaseReference.child("Users").child(id).updateChildren(values);
    }

}
