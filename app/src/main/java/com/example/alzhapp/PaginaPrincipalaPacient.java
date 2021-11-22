package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

public class PaginaPrincipalaPacient extends AppCompatActivity {
    private Button apeleaza_supraveghetor;
    private Button apeleaza_doctor;
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference db;
    IstoricAdapter istoricAdapter;
    ArrayList<Istoric> list;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principala_pacient);


        recyclerView = findViewById(R.id.myBoxIstoric);
        database = FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Istoric>();
        istoricAdapter = new IstoricAdapter(this,list);
        recyclerView.setAdapter(istoricAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    String n=m.getNume();
                    String o=m.getOra();
                    String i=m.getInterv();
                    int oi=Integer.valueOf(o);
                    int ii=Integer.valueOf(i);
                    int poz=0;
                    while((oi+ii*poz)<24){
                        String ora=String.valueOf(oi+ii*poz);
                        poz++;
                        Istoric istoric=new Istoric(n,ora);
                        list.add(istoric);
                        Collections.sort(list, Istoric.ordonare);

                    }

                }

                istoricAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        apeleaza_supraveghetor=findViewById(R.id.apeleaza_supraveghetor);
        apeleaza_doctor=findViewById(R.id.apeleaza_doctor);


        apeleaza_supraveghetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users u=snapshot.getValue(Users.class);
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+u.getTelefonSupraveghetor()));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

}