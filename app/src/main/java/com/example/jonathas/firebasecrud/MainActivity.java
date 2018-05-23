package com.example.jonathas.firebasecrud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText edNome, edEmail;
    ListView listView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edNome = (EditText) findViewById(R.id.editNome);
        edEmail = (EditText) findViewById(R.id.editEmail);
        listView = (ListView) findViewById(R.id.listView1);

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

        if (id == R.id.menu_novo) {
            Pessoa p = new Pessoa();
            p.setId(UUID.randomUUID().toString());
            p.setNome(edNome.getText().toString());
            p.setEmail(edEmail.getText().toString());

            databaseReference.child("Pessoa").child(p.getId()).setValue(p);
            limparCampos();
        }
        return true;
    }

    private void limparCampos() {
        edEmail.setText("");
        edEmail.setText("");
    }
}
