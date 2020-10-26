package com.example.camara;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnguardar;
    private ImageView img;
    private TextView tTextView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    boolean control = false;
    private  final String ruta_foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ISTLImage";

    private File file = new File(ruta_foto);
    String imagenFileName = "imagenApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file.mkdir();

        btnguardar = (Button)findViewById(R.id.buttonToma);
        btnguardar.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imageView);
        tTextView=(TextView)findViewById(R.id.textView);
        enableMyCamera();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.action_delete:

                //borrarImagen();

                break;
            case R.id.action_save:
                guardarImagen();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v==btnguardar){
            tomarfoto();
        }
    }
    private void tomarfoto(){
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(tomarFotoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(tomarFotoIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int resquestCode,int resultCode, Intent data) {
        super.onActivityResult(resquestCode, resultCode, data);
        if (resquestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imagenBitmap = (Bitmap) extra.get("data");
            img.setImageBitmap(imagenBitmap);
            tTextView.setVisibility(View.GONE);
            control=true;
        }
    }

    private void  enableMyCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        !=PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
    }

    private void guardarImagen () {

        FileOutputStream fos = null;
        try {
            if (control == true) {
                Bitmap imagen = ((BitmapDrawable) img.getDrawable()).getBitmap();
                File image = new File(file, imagenFileName + ".jpg");
                fos = new FileOutputStream(image);
                imagen.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.close();
                Toast.makeText(getApplicationContext(), "LA IMAGEN SEA GUARDO ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "NO HAY IMAGEN PARA GUARDAR", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}