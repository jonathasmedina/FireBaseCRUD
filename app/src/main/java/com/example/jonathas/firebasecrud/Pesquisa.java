package com.example.jonathas.firebasecrud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pesquisa extends AppCompatActivity {

    EditText edPesquisa;
    ListView listView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<Pessoa> pessoaList = new ArrayList<>();
    ArrayAdapter<Pessoa> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        edPesquisa = findViewById(R.id.edPesquisa);
        listView = findViewById(R.id.listViewPesquisa);

        inicializarFirebase();
        eventoEdit();
    }

    private void eventoEdit() {
        edPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra = edPesquisa.getText().toString().trim();
                pesquisarPalavra(palavra);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pesquisarPalavra("");
    }

    private void pesquisarPalavra(String palavra) {
        Query query;
        if (palavra.equals("")) {
            query = databaseReference.child("Pessoa").orderByChild("nome");
        } else {
            query = databaseReference.child("Pessoa").orderByChild("nome").startAt(palavra).endAt(palavra+"\uf8ff");
        }

        //limpar vetor
        pessoaList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                    Pessoa p = objSnapshot.getValue(Pessoa.class);
                    pessoaList.add(p);
                }

                arrayAdapter = new ArrayAdapter<Pessoa>(Pesquisa.this,
                        android.R.layout.simple_list_item_1, pessoaList);

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(Pesquisa.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
