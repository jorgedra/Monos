package com.example.monos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monos.Clases.Mono;
import com.example.monos.RecycleView.ListaMonosAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MostrarMonosActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        else{
            Toast.makeText(this, "debes autenticarte primero", Toast.LENGTH_SHORT).show();
            FirebaseUser user = mAuth.getCurrentUser();
            //updateUI(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    //---------------------------------------------------------------------
    private RecyclerView rv_monos = null;
    private ListaMonosAdapter adaptadorMonos = null;
    private DatabaseReference myRefmonos = null;
    private DatabaseReference myRefmonos1 = null;
    private ArrayList<Mono> monos;
    private EditText edt_buscar_nombre1;
    public static int PETICION1 = 1;
    //-----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_monos);
        rv_monos = (RecyclerView) findViewById(R.id.rv_monos);
        edt_buscar_nombre1 = (EditText) findViewById(R.id.edt_buscar_nombre);
        //-------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        monos = new ArrayList<Mono>();
        //-----------------------------------------------------------
        adaptadorMonos = new ListaMonosAdapter(this,monos);
        rv_monos.setAdapter(adaptadorMonos);
        myRefmonos = FirebaseDatabase.getInstance().getReference("Monos");
        myRefmonos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                adaptadorMonos.getMonos().clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Mono m = (Mono) dataSnapshot.getValue(Mono.class);
                    monos.add(m);

                }
                adaptadorMonos.setMonos(monos);
                adaptadorMonos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            rv_monos.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            // In portrait
            rv_monos.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void addMono(View view) {
        Intent intent = new Intent(this, AddMonosActivity.class);
        startActivity(intent);
    }
    public void buscarMonos(View view) {

        String texto = String.valueOf(edt_buscar_nombre1.getText());
        myRefmonos1 = FirebaseDatabase.getInstance().getReference("Monos");
        myRefmonos1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> keys1 = new ArrayList<String>();
                ArrayList<Mono> monos1 = new ArrayList<Mono>();
                for (DataSnapshot keynode : snapshot.getChildren()) {
                    keys1.add(keynode.getKey());
                    Mono m = keynode.getValue(Mono.class);
                    if(m.getNombre().contains(texto)) {
                        monos1.add(keynode.getValue(Mono.class));
                    }
                }
                adaptadorMonos.setMonos(monos1);
                adaptadorMonos.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });

    }
    //---------------------------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PETICION1 && resultCode == Activity.RESULT_OK) {
            int posicion = data.getIntExtra(DetallesMonosActivity.EXTRA_POSICION_DEVUELTA,-1);
            String tipo = data.getStringExtra(DetallesMonosActivity.EXTRA_TIPO);
            if(tipo.equalsIgnoreCase("edicion"))
            {
                adaptadorMonos.notifyItemChanged(posicion);
                adaptadorMonos.notifyDataSetChanged();
            }
            else if(tipo.equalsIgnoreCase("borrado"))
            {
                adaptadorMonos.notifyItemRemoved(posicion);
                adaptadorMonos.notifyDataSetChanged();
            }
            else{
                adaptadorMonos.notifyDataSetChanged();
            }


            // this.recreate();
            //  getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
    }
    //------------------------------------------------------------------------------

}