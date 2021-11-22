package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    RecyclerView recyclerView;
    DatabaseReference database;
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
    }
    /*public void ordonare(ArrayList<Istoric> list){
        int i;
        int j;
        Istoric ist;
       for(i=0; i<list.size(); i++) {
           for(j=i+1; j<list.size();j++){
               int k=Integer.valueOf(list.get(i).getOra());
               int l=Integer.valueOf(list.get(j).getOra());
               if (k>l){
                   ist=list.get(i);
                   list.get(i)= list.get(j);
               }
           }

       }

    }*/
}