package com.example.alzhapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Medicamentatie extends AppCompatActivity {
    private ImageButton adaugare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid=getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentatie);

        adaugare=findViewById(R.id.adaugare);
        adaugare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(Medicamentatie.this,AdaugareMedicament.class);
                i.putExtra("uid",uid);
                startActivity(i);
            }
        });
    }
}