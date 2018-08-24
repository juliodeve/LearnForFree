package com.vazquez.julio.proyectofarru;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean loginuUser = true;
    EditText txtMail;
    EditText txtPass;
    Button btnSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Iniciar sesión");

        txtMail = (EditText) findViewById(R.id.txtNombre);
        txtPass = (EditText) findViewById(R.id.etPass);
        btnSesion = (Button) findViewById(R.id.btnSesion);
        txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    if (loginuUser){
                        taskBtnAccesUser();
                    } else {
                        taskBtnAccesAdmin();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Sin acceso a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void taskBtnAccesUser() {
        final String user = String.valueOf(txtMail.getText());
        final String pass = String.valueOf(txtPass.getText());
        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                final String res = s.enviarPost(user, pass, "http://learnforfree.juliovazquez.net//movil/LoginUsuario.php");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("OK")) {
                            goMainScreenUser(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    private void taskBtnAccesAdmin() {
        final String user = String.valueOf(txtMail.getText());
        final String pass = String.valueOf(txtPass.getText());
        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                final String res = s.enviarPost(user, pass, "http://learnforfree.juliovazquez.net//movil/LoginAdmin.php");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("OK")) {
                            goMainScreenAdmin(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    private void goMainScreenAdmin( String mail) {
        Intent intent = new Intent(this, Main3Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }

    private void goMainScreenUser(String mail) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbUsuario:
                if (checked)
                    loginuUser = true;
                    break;
            case R.id.rbAdmin:
                if (checked)
                    loginuUser = false;
                    break;
        }
    }

}
