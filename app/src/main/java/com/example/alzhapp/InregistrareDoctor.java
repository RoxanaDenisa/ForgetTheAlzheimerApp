package com.example.alzhapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InregistrareDoctor extends AppCompatActivity {
    private Button moveToPacient;
    private Button moveToAutentificare;
    private EditText numeComplet;
    private EditText adresa;
    private EditText telefon;
    private EditText adresaMail;
    private EditText parola;
    private EditText confirmareParola;
    private Button creare;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inregistrare_doctor);
        moveToPacient=findViewById(R.id.bPacient);
        moveToAutentificare=findViewById(R.id.buton_autentif_d);
        numeComplet=findViewById(R.id.Nume_doctor);
        adresa=findViewById(R.id.Adresa_doctor);
        telefon=findViewById(R.id.Telefon_doctor);
        adresaMail=findViewById(R.id.Adresa_mail_doctor);
        parola=findViewById(R.id.Parola_doctor);
        confirmareParola=findViewById(R.id.Confirmare_parola_doctor);
        creare=findViewById(R.id.creare);
        dbRef= FirebaseDatabase.getInstance().getReference();
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
        creare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeComplet1=numeComplet.getText().toString();
                String adresa1=adresa.getText().toString();
                String telefon1=telefon.getText().toString();
                String adresaMail1=adresaMail.getText().toString();
                String parola1=parola.getText().toString();
                Users doctor=new Users(numeComplet1,adresaMail1,parola1,telefon1,adresa1, "doctor");
                dbRef.child("users").child(telefon1).setValue(doctor);
                Toast.makeText(InregistrareDoctor.this,"Cont creat", Toast.LENGTH_SHORT).show();
            }
        });
    }

}