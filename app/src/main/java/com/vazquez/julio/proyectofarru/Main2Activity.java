package com.vazquez.julio.proyectofarru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    List<Curso> cursoList = new ArrayList<>();
    RecyclerView recyclerView;
    CursosADAPTER cursosADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Inicio");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                final String res = s.enviarPost("http://learnforfree.juliovazquez.net//movil/Cursos.php");
                cursoList = ParserCursos.parser(res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cursosADAPTER = new CursosADAPTER(getApplicationContext(), cursoList);
                        recyclerView.setAdapter(cursosADAPTER);
                        recyclerView.setHasFixedSize(true);
                    }
                });
            }
        };
        tr.start();
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

    private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
