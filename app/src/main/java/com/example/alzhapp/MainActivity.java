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


public class MainActivity extends AppCompatActivity {
    private Button moveToDoctor;
    private Button moveToAutentificare;
    private EditText numeComplet;
    private EditText adresa;
    private EditText telefon;
    private EditText numeSupraveghetor;
    private EditText telefonSupraveghetor;
    private EditText adresaMail;
    private EditText parola;
    private EditText confirmareParola;
    private EditText codDoctor;
    private Button sendData;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveToDoctor=findViewById(R.id.bDoctor);
        moveToAutentificare=findViewById(R.id.buton_autentif);
        numeComplet=findViewById(R.id.Nume);
        adresa=findViewById(R.id.Adresa);
        telefon=findViewById(R.id.Telefon);
        numeSupraveghetor=findViewById(R.id.Nume_supraveghetor);
        telefonSupraveghetor=findViewById(R.id.Telefon_supraveghetor);
        adresaMail=findViewById(R.id.Adresa_mail);
        parola=findViewById(R.id.Parola);
        confirmareParola=findViewById(R.id.Confirmare_parola);
        codDoctor=findViewById(R.id.Cod_doctor);
        sendData=findViewById(R.id.buton_send);
        dbRef= FirebaseDatabase.getInstance().getReference();

        moveToDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, com.example.alzhapp.InregistrareDoctor.class);
                startActivity(i);
            }
        });
        moveToAutentificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, com.example.alzhapp.Autentificare.class);
                startActivity(i);
            }
        });
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeComplet1=numeComplet.getText().toString();
                String adresa1=adresa.getText().toString();
                String telefon1=telefon.getText().toString();
                String numeSupraveghetor1=numeSupraveghetor.getText().toString();
                String telefonSupraveghetor1=telefonSupraveghetor.getText().toString();
                String adresaMail1=adresaMail.getText().toString();
                String parola1=parola.getText().toString();
                //String confirmareParola1=confirmareParola.getText().toString();
                String codDoctor1=codDoctor.getText().toString();
                //System.out.println(numeComplet1);
                Users pacient=new Users(numeComplet1,adresa1,telefon1,numeSupraveghetor1,telefonSupraveghetor1,adresaMail1,parola1,codDoctor1, "pacient");
                //dbRef.child("users").child("telefon1").setValue(users);
                dbRef.child("users").push().setValue(pacient);
                Toast.makeText(MainActivity.this,"Cont creat", Toast.LENGTH_SHORT).show();
            }
        });


    }

}