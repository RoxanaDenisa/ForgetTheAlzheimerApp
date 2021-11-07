package com.example.alzhapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Autentificare extends AppCompatActivity {
    private Button moveToInregistrare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificare);
        moveToInregistrare=findViewById(R.id.buton_inregistrare);

        moveToInregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Autentificare.this, com.example.alzhapp.MainActivity.class);
                startActivity(i);
            }
        });
    }
}