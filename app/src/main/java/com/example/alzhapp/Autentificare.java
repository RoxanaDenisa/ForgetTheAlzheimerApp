package com.example.alzhapp;

import static com.example.alzhapp.AESCrypt.decrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

public class Autentificare extends AppCompatActivity {
    private Button moveToInregistrare;
    private EditText adresaMail;
    private EditText parola;
    private Button autentificare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificare);
        moveToInregistrare=findViewById(R.id.buton_inregistrare);
        adresaMail=findViewById(R.id.Adresa_mail_utilizatori);
        parola=findViewById(R.id.Parola_utilizatori);
        autentificare=findViewById(R.id.autentificare);

        moveToInregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Autentificare.this, com.example.alzhapp.MainActivity.class);
                startActivity(i);
            }
        });
        autentificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adresaMail1=adresaMail.getText().toString();
                String parola1=parola.getText().toString();
                DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("users");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int ok=0;
                        String tip="";
                        for (DataSnapshot snapshot1:snapshot.getChildren()) {
                            String s=snapshot1.child("adresaMail").getValue().toString();
                            String p=snapshot1.child("parola").getValue().toString();

                            try {
                                String p1=decrypt(p);
                                if (s.equals(adresaMail1)&& p1.equals(parola1)){
                                    tip=snapshot1.child("tipUtilizator").getValue().toString();
                                    ok=1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        if (ok==1 ){
                            if (tip.equals("doctor")) {
                                Intent i = new Intent(Autentificare.this, com.example.alzhapp.PaginaPrincipalaDoctor.class);
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(Autentificare.this, com.example.alzhapp.PaginaPrincipalaPacient.class);
                                startActivity(i);
                            }
                        }
                        else{
                            Toast.makeText(Autentificare.this, "Date introduse greșit", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}