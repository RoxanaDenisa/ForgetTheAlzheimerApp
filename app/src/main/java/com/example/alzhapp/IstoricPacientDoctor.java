package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class IstoricPacientDoctor extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference db;
    IstoricAdapter istoricAdapter;
    ArrayList<Istoric> list;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid=getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istoric_pacient_doctor);


        recyclerView = findViewById(R.id.myBoxIstoricP);
        database = FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Istoric>();
        istoricAdapter = new IstoricAdapter(this, list,uid);
        recyclerView.setAdapter(istoricAdapter);
        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(IstoricPacientDoctor.this, DetaliiPacienti.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();

            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    String n = m.getNume();
                    String o = m.getOra();
                    String i = m.getInterv();
                    int oi = Integer.valueOf(o);
                    int ii = Integer.valueOf(i);
                    int poz = 0;
                    while ((oi + ii * poz) < 24) {


                        //Aadugarea in lista istoric pt adapter
                        String ora = String.valueOf(oi + ii * poz);
                        Istoric istoric = new Istoric(n, ora);
                        poz++;
                        add(list,istoric);
                        Collections.sort(list, Istoric.ordonare);

                    }

                }

                istoricAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void add(ArrayList<Istoric> l, Istoric i){
        for (Istoric ist: l){
            if (ist.getNume().equals(i.getNume())&& ist.getOraInt()==i.getOraInt()){
                l.remove(ist);
                list.add(i);
                return;
            }
        }
        list.add(i);
    }
}