package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Medicatie extends AppCompatActivity {
    private ImageButton adaugare;
    RecyclerView recyclerView;
    DatabaseReference database;
    MedicamentAdapter medicamentAdapter;
    ArrayList<Medicament> list;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid=getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicatie);

        adaugare=findViewById(R.id.adaugare);
        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Medicatie.this, DetaliiPacienti.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();

            }
        });
        adaugare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Medicatie.this,AdaugareMedicament.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
        recyclerView = findViewById(R.id.myBoxMedicament);
        database = FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        medicamentAdapter = new MedicamentAdapter(this,list);
        recyclerView.setAdapter(medicamentAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    list.add(m);
                }
                medicamentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}