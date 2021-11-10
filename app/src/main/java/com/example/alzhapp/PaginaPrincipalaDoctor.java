package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaginaPrincipalaDoctor extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference db;
    private String userId;
    private ImageButton logout;
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Users> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principala_doctor);
        logout=findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PaginaPrincipalaDoctor.this,Autentificare.class));
            }
        });
        user=FirebaseAuth.getInstance().getCurrentUser();
        db= FirebaseDatabase.getInstance().getReference().child("users");
        userId=user.getUid();
        final TextView tv=(TextView) findViewById(R.id.numeDr);
        db.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 Users userProfile=snapshot.getValue(Users.class);
                 if(userProfile!=null){
                     String name=userProfile.getNumeComplet();
                     tv.setText(name);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaginaPrincipalaDoctor.this,"Eroare",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView = findViewById(R.id.myBoxPacienti);
        database = FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user=FirebaseAuth.getInstance().getCurrentUser();
                String m=user.getEmail();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Users u = dataSnapshot.getValue(Users.class);
                    if(u.getTipUtilizator().equals("pacient"))
                        if(u.getMailDoctor().equals(m))
                           list.add(u);
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}