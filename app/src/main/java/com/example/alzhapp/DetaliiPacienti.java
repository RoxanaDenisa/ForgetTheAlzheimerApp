package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DetaliiPacienti extends AppCompatActivity {
    private EditText numeP;
    private EditText telefonP;
    private EditText emailP;
    private EditText numeS;
    private EditText telefonS;
    private DatabaseReference db;
    private Button editeaza_p;
    private Button editeaza_s;
    private Button medicamentatie;
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
        editeaza_p=findViewById(R.id.editeaza_pacient);
        editeaza_s=findViewById(R.id.editeaza_supraveghetor);
        medicamentatie=findViewById(R.id.medicamentatie);
        db= FirebaseDatabase.getInstance().getReference().child("users");

        medicamentatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(DetaliiPacienti.this, Medicatie.class);
                i.putExtra("uid",uid);
                startActivity(i);
            }
        });

        editeaza_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map=new HashMap<>();
                map.put("numeComplet",numeP.getText().toString());
                map.put("telefon",telefonP.getText().toString());
                db.child(uid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetaliiPacienti.this, "Editare cu succes", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetaliiPacienti.this, "Editare esuata", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        editeaza_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map=new HashMap<>();
                map.put("numeSupraveghetor",numeS.getText().toString());
                map.put("telefonSupraveghetor",telefonS.getText().toString());
                db.child(uid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetaliiPacienti.this, "Editare cu succes", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetaliiPacienti.this, "Editare esuata", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

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