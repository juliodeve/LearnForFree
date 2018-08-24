package com.vazquez.julio.proyectofarru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {

    EditText txtId;
    EditText txtNombre;
    Spinner spCategoria;
    EditText txtPrecio;
    EditText txtUrl;
    EditText txtCreador;
    EditText txtUnidades;
    Spinner spNivel;
    Button btnImagen;
    Button btnAgregar;
    ImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        setTitle("Agregar un curso");
        requestStoragePermission();

        txtId = (EditText) findViewById(R.id.txtId);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        spCategoria = (Spinner) findViewById(R.id.spCategoria);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        txtCreador = (EditText) findViewById(R.id.txtCreador);
        txtUnidades = (EditText) findViewById(R.id.txtUnidades);
        spNivel = (Spinner) findViewById(R.id.spNivel);
        btnImagen = (Button) findViewById(R.id.btnImagen);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        imageView =(ImageView) findViewById(R.id.imageView);

        btnAgregar.setOnClickListener(this);
        btnImagen.setOnClickListener(this);

    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                goLoginScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void uploadMultipart() {
        //getting name for the image
//        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "http://learnforfree.juliovazquez.net//movil/RegistrarCurso.php")
                    .addFileToUpload(path, "img") //Adding file
                    .addParameter("prod-codigo", txtId.getText().toString())
                    .addParameter("prod-name", txtNombre.getText().toString())
                    .addParameter("prod-categoria", spCategoria.getSelectedItem().toString())
                    .addParameter("prod-stock", txtUnidades.getText().toString())
                    .addParameter("prod-price", txtPrecio.getText().toString())
                    .addParameter("prod-model", txtUrl.getText().toString())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


        txtUrl.setText("");
        txtPrecio.setText("");
        txtUnidades.setText("");
        txtId.setText("");
        txtNombre.setText("");
        spCategoria.setSelection(0);
        spNivel.setSelection(0);
        btnAgregar.setVisibility(View.GONE);
        Toast.makeText(this, "Cargando imagen", Toast.LENGTH_LONG).show();
    }


    //method to show file chooser
    private void showFileChooser() {
        btnAgregar.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnImagen) {
            showFileChooser();
        }
        if (v == btnAgregar) {
            uploadMultipart();
        }
    }
}
