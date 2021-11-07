package com.example.alzhapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InregistrareDoctor extends AppCompatActivity {
    private Button moveToPacient;
    private Button moveToAutentificare;
    private Button test1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inregistrare_doctor);
        moveToPacient=findViewById(R.id.bPacient);
        moveToAutentificare=findViewById(R.id.buton_autentif_d);
        moveToPacient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(InregistrareDoctor.this, com.example.alzhapp.MainActivity.class);
                startActivity(i);
            }
        });
        moveToAutentificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(InregistrareDoctor.this, com.example.alzhapp.Autentificare.class);
                startActivity(i);
            }
        });
    }

}