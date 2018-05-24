package com.example.jonathas.firebasecrud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText edNome, edEmail;
    ListView listView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private List<Pessoa> listPessoa = new ArrayList<Pessoa>();
    private ArrayAdapter<Pessoa> pessoaArrayAdapter;

    Pessoa pessoaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edNome = (EditText) findViewById(R.id.editNome);
        edEmail = (EditText) findViewById(R.id.editEmail);
        listView = (ListView) findViewById(R.id.listView1);

        inicializarFirebase();

        eventoDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa) parent.getItemAtPosition(position);
                edNome.setText(pessoaSelecionada.getNome());
                edEmail.setText(pessoaSelecionada.getEmail());

            }
        });
    }

    private void eventoDatabase() {
        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    listPessoa.clear();

                    for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                        Pessoa p = objSnapshot.getValue(Pessoa.class);
                        listPessoa.add(p);
                    }

                    pessoaArrayAdapter = new ArrayAdapter<Pessoa>(MainActivity.this,
                            android.R.layout.simple_list_item_1, listPessoa);
                    listView.setAdapter(pessoaArrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
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
        if (id == R.id.menu_atualiza) {
            Pessoa p = new Pessoa();
            p.setId(pessoaSelecionada.getId());
            p.setNome(edNome.getText().toString());
            p.setEmail(edEmail.getText().toString());

            databaseReference.child("Pessoa").child(p.getId()).setValue(p);

            limparCampos();
        }
        if (id == R.id.menu_deleta) {
            Pessoa p = new Pessoa();
            p.setId(pessoaSelecionada.getId());

            databaseReference.child("Pessoa").child(p.getId()).removeValue();
        }
        if (id == R.id.menu_pesquisa) {
            Intent intent = new Intent(MainActivity.this, Pesquisa.class);
            startActivity(intent);
        }


        return true;
    }

    private void limparCampos() {
        edNome.setText("");
        edEmail.setText("");
    }



}
