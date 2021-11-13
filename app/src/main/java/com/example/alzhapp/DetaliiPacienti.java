package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetaliiPacienti extends AppCompatActivity {
    private TextView numeP;
    private TextView telefonP;
    private TextView emailP;
    private TextView numeS;
    private TextView telefonS;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid=getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalii_pacienti);
        numeP=findViewById(R.id.numeP);
        telefonP=findViewById(R.id.telefonP);
        emailP=findViewById(R.id.EmailP);
        numeS=findViewById(R.id.numeS);
        telefonS=findViewById(R.id.telefonS);
        db= FirebaseDatabase.getInstance().getReference().child("users");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Users u=ds.getValue(Users.class);
                    if (u.getTipUtilizator().equals("pacient")){
                        if(u.getUid().equals(uid)) {
                            numeP.setText(u.getNumeComplet());
                            telefonP.setText(u.getTelefon());
                            emailP.setText(u.getAdresaMail());
                            numeS.setText(u.getNumeSupraveghetor());
                            telefonS.setText(u.getTelefonSupraveghetor());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}