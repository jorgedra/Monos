package com.example.monos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.monos.Clases.Mono;
import com.example.monos.RecycleView.ListaMonosAdapter;
import com.example.monos.Utilidades.ImagenesFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AddMonosActivity extends AppCompatActivity {

    private EditText edt_add_nombre;
    private EditText edt_add_id;
    private EditText edt_add_region;
    private EditText edt_add_familia;

    private ListaMonosAdapter monosAdapter = null;
    private FirebaseAuth mAuth;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;

    ImageView img_add_mono;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monos);
        //------------------------------------------------
        edt_add_id = (EditText) findViewById(R.id.edt_detalles_id);
        edt_add_nombre = (EditText) findViewById(R.id.edt_detalles_nombre);
        edt_add_familia = (EditText) findViewById(R.id.edt_detalles_familia);
        edt_add_region = (EditText) findViewById(R.id.edt_detalles_region);
        img_add_mono = (ImageView) findViewById(R.id.img_add_mono);

    }

    public void add_mono_realtime(View view) {
        int id = Integer.valueOf(String.valueOf(edt_add_id.getText()));
        String nombre = String.valueOf(edt_add_nombre.getText());
        String familia = String.valueOf(edt_add_familia.getText());
        String region = String.valueOf(edt_add_region.getText());

        Mono m = new Mono(id,nombre, familia,region);
        //--------------------------------------------
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Monos").child(m.getNombre()).setValue(m);
        Toast.makeText(this,"mono añadido correctamente",Toast.LENGTH_LONG).show();
        // codigo para guardar la imagen del usuario en firebase store
        if(imagen_seleccionada != null) {
            String carpeta = m.getNombre();
            ImagenesFirebase.subirFoto(carpeta,m.getNombre(), img_add_mono);
        }
        finish();
        monosAdapter.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------
    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------
    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_add_mono.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}