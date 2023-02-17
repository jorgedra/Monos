package com.example.monos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monos.Clases.Mono;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText edt_login_email;
    private EditText edt_login_clave;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_clave = (EditText) findViewById(R.id.edt_login_clave);

        mAuth = FirebaseAuth.getInstance();
        // PRUEBAS EN FIREBASE REALTIME
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        Mono m1 = new Mono(1, "Mono Maicero", "Cebidae", "Sudamérica");
        Mono m2 = new Mono(2, "Mico Nocturno", "Aotidae", "Centro y Sudamérica");
        Mono m3 = new Mono(3, "Mono aullador", "Atelidae", "Norte de Sudamérica");

        Map<String, Mono> Monos = new HashMap<String, Mono>();
        Monos.put(m1.getNombre(), m1);
        Monos.put(m2.getNombre(), m2);
        Monos.put(m3.getNombre(), m3);
        myRef.child("Monos").setValue(Monos);
    }

    // este boton esta para demostrar que no puedes entrar a ver los monos sin registrarte
    public void mostrar_monos(View view) {
        Intent intent = new Intent(this, MostrarMonosActivity.class);
        startActivity(intent);
    }


    public void loguearUsuario(View view) {
        String email = String.valueOf(edt_login_email.getText()).trim();
        String password = String.valueOf(edt_login_clave.getText());
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "los campos tienen que tener texto", Toast.LENGTH_SHORT).show();

        }
        else {
            System.out.println("aqui llegamos");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("firebasedb", "logueado correctamente");
                                Toast.makeText(MainActivity.this, "logueado correctamente", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Intent intent = new Intent(MainActivity.this, MostrarMonosActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("firebasedb", "error al loguearse", task.getException());
                                Toast.makeText(MainActivity.this, "error al loguearse", Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
        }
    }

    public void registrarUsuario(View view) {
        String email = String.valueOf(edt_login_email.getText()).trim();
        String password = String.valueOf(edt_login_clave.getText());
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "los campos tienen que tener texto", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("firebase1", "usuario registrado correctamente");
                                Toast.makeText(MainActivity.this, "usuario registrado correctamente", Toast.LENGTH_SHORT).show();
//                            // updateUI(user);
                                Intent intent = new Intent(MainActivity.this, MostrarMonosActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("firebase1", "no se pudo registrar al usuario", task.getException());
                                Toast.makeText(MainActivity.this, "no se pudo registrar al usuario", Toast.LENGTH_SHORT).show();
                                //  updateUI(null);
                            }
                        }

                    });
        }
    }


    public void cerrarUsuario(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "se cerró la sesión correctamente", Toast.LENGTH_SHORT).show();
    }
}