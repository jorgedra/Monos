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
import com.example.monos.RecycleView.MonosViewHolder;
import com.example.monos.Utilidades.ImagenesBlopBitmap;
import com.example.monos.Utilidades.ImagenesFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class DetallesMonosActivity extends AppCompatActivity {

    public static final String EXTRA_POSICION_DEVUELTA =  "com.example.detallesmonosactivity.posicion";
    public static final String EXTRA_TIPO = "com.example.detallesmonosactivity.tipo";
    EditText edt_detalles_nombre = null;
    EditText edt_detalles_id = null;

    EditText edt_detalles_familia = null;

    EditText edt_detalles_region = null;

    private ListaMonosAdapter monosAdapter = null;

    String id_antiguo ="";
    int posicion = -1;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    ImageView img_detalles_foto_mono = null;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_monos);
        edt_detalles_id = (EditText) findViewById(R.id.edt_detalles_id);
        edt_detalles_nombre = (EditText) findViewById(R.id.edt_detalles_nombre);
        edt_detalles_familia = (EditText) findViewById(R.id.edt_detalles_familia);
        edt_detalles_region = (EditText) findViewById(R.id.edt_detalles_region);
        img_detalles_foto_mono = (ImageView) findViewById(R.id.img_detalles_foto_mono);
        //--------------------------------------------------------------------------
        Intent intent = getIntent();
        if(intent != null)
        {
            Mono m = (Mono)intent.getSerializableExtra(MonosViewHolder.EXTRA_MONO_ITEM);
            edt_detalles_id.setText(String.valueOf(m.getIdMono()));
            edt_detalles_nombre.setText(m.getNombre());
            edt_detalles_familia.setText(m.getFamilia());
            edt_detalles_region.setText(m.getRegion());
            id_antiguo = m.getNombre();
            //cargo la foto
            byte[] fotobinaria = (byte[]) intent.getByteArrayExtra(MonosViewHolder.EXTRA_MONO_IMAGEN);
            Bitmap fotobitmap = ImagenesBlopBitmap.bytes_to_bitmap(fotobinaria, 200,200);
            img_detalles_foto_mono.setImageBitmap(fotobitmap);
            // obtengo la posicion
            posicion = intent.getIntExtra(MonosViewHolder.EXTRA_POSICION_CASILLA,-1);
        }

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
                img_detalles_foto_mono.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //-----------------------------------------------------------------------
    public void detalles_borrar_mono(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        int id = Integer.valueOf(String.valueOf(edt_detalles_id.getText())) ;
        String nombre = String.valueOf(edt_detalles_nombre.getText());
        String familia = String.valueOf(edt_detalles_familia.getText());
        String region = String.valueOf(edt_detalles_region.getText());

        Mono m = new Mono(id,nombre, familia,region);
        //--------------------------------------------
        if(id_antiguo.equalsIgnoreCase(nombre))
        {
            myRef.child("Monos").child(id_antiguo).removeValue();
            Toast.makeText(this,"mono borrado correctamente",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"no se pudo borrar el mono",Toast.LENGTH_LONG).show();
        }
        //-------------------------------------------
        // borramos la imagen del firebase store
        String carpeta = m.getNombre();
        ImagenesFirebase.borrarFoto(carpeta,m.getNombre());
        ImagenesFirebase.borrarCarpeta(carpeta);
        // cerramos la ventana y volvemos al recyclerview
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_POSICION_DEVUELTA, posicion);
        replyIntent.putExtra(EXTRA_TIPO, "borrado");
        setResult(RESULT_OK, replyIntent);
        finish();
    }
    public void detalles_editar_mono(View view) {
        int id = Integer.valueOf(String.valueOf(edt_detalles_id.getText())) ;
        String nombre = String.valueOf(edt_detalles_nombre.getText());
        String familia = String.valueOf(edt_detalles_familia.getText());
        String region = String.valueOf(edt_detalles_region.getText());

        Mono m = new Mono(id,nombre, familia,region);
        //--------------------------------------------
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Monos").child(id_antiguo).removeValue();
        myRef.child("Monos").child(m.getNombre()).setValue(m);
        Toast.makeText(this,"mono editado correctamente",Toast.LENGTH_LONG).show();
        //--------------------------------------------------
        if(imagen_seleccionada != null || !id_antiguo.equalsIgnoreCase(m.getNombre())) {
            String carpeta = m.getNombre();
            ImagenesFirebase.borrarFoto(id_antiguo,id_antiguo);
            ImagenesFirebase.subirFoto(carpeta,m.getNombre(), img_detalles_foto_mono);
        }
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_POSICION_DEVUELTA, posicion);
        replyIntent.putExtra(EXTRA_TIPO, "edicion");
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void cambiar_imagen_detalles(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

}