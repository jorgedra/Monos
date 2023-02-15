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
        //---------------------------------------------------------------------------------
//        ArrayList<Alumno> alumnos3 = new ArrayList<Alumno>();
//        alumnos3.add(new Alumno("antonio", "1dam"));
//        alumnos3.add(new Alumno("david", "2dam"));
//        alumnos3.add(new Alumno("olga", "1dam"));
//        myRef.child("alumnosarraylist").setValue(alumnos3);
//
//        //-----------------borrado de datos -------------------------------------
//        myRef.child("alumnos").child("juan").setValue(null);
//        myRef.child("alumnoshashmap").child("andres").removeValue();
//        myRef.child("hashmapequipos").child("equipo3").setValue(null);
//        myRef.child("saludo3").removeValue();
//        myRef.child("equipos").removeValue();
//        //-------------------actualizacion de datos---------------------------------------------------------
//        myRef.child("alumnoshashmap").child("carlos").child("curso").setValue("cursonuevo");
//        myRef.child("hashmapequipos").child("equipo4").child("nombre").setValue("nueva marca");
//        myRef.child("hashmapequipos").child("equipo4").child("idEquipo").setValue("nuevo identificador");
//        //    Alumno a_maria_nuevo = new Alumno("nombre nuevo maria", "curso nuevo maria" );
//
//        Equipo equipo4nuevo = new Equipo("id4nuevo", "marca4 nuevo");
//        Map<String,Object> nuevoequipo4 = new HashMap<>();
//        nuevoequipo4.put("equipo4",equipo4nuevo);
//        myRef.child("hashmapequipos").updateChildren(nuevoequipo4);
//        //--------------------------------------------------------------------------------------------
//
//        //    Map<String, Object> nuevoMap = new HashMap<String,Object>();
//        //    nuevoMap.put("maria",a_maria_nuevo);
//        //   myRef.child("alumnoshashmap").updateChildren(nuevoMap);
//
//        //--------------------------------------------------------------------------------------
//
//        // LEER UN ALUMNO CON CLAVE "clave2" (HASHMAP -> alumnoshashmap )
//        // Read from the database alumno key -> "clave2"
//        DatabaseReference myRefalumnos1 = database.getReference("alumnoshashmap");
//        myRefalumnos1.child("olga").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Alumno value = dataSnapshot.getValue(Alumno.class);
//                if(value != null) {
//                    System.out.println(value.toString());
//                    Log.i("firebase1", value.toString());
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.i( "firebase1", String.valueOf(error.toException()));
//            }
//        });
//        //---------------------------------------------------------------------------------------
//        // LEER TODOS LOS ALUMNOS
//        DatabaseReference myRefalumnos2 = database.getReference("alumnoshashmap");
//        myRefalumnos2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> keys = new ArrayList<String>();
//                List<Alumno> alumnos = new ArrayList<Alumno>();
//                for (DataSnapshot keynode : snapshot.getChildren()) {
//                    keys.add(keynode.getKey());
//                    alumnos.add(keynode.getValue(Alumno.class));
//                }
//                for (String k : keys) {
//                    System.out.println(k);
//                    Log.i("firebase1", "clave leida " + k);
//                }
//                for (Alumno a : alumnos) {
//                    System.out.println(a.toString());
//                    Log.i("firebase1", "alumno leido " + a.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.i( "firebase1", String.valueOf(error.toException()));
//            }
//        });
//    }



    }
    public void mostrar_monos (View view){
        Intent intent = new Intent(this, MostrarMonosActivity.class);
        startActivity(intent);
    }


    public void loguearUsuario (View view){
        String email = String.valueOf(edt_login_email.getText()).trim();
        String password = String.valueOf(edt_login_clave.getText());
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

    public void registrarUsuario (View view){
        String email = String.valueOf(edt_login_email.getText()).trim();
        String password = String.valueOf(edt_login_clave.getText());
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

    public void cerrarUsuario (View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "se cerró la sesión correctamente", Toast.LENGTH_SHORT).show();
    }
}