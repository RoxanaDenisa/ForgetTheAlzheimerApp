package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
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
    private Button medicatie;
    private Button istoricpac;
    private Button localizeaza;
    private ImageButton back;
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
        medicatie=findViewById(R.id.medicatie);
        istoricpac=findViewById(R.id.istoricpac);
        localizeaza=findViewById(R.id.localizeaza);
        db= FirebaseDatabase.getInstance().getReference().child("users");
        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DetaliiPacienti.this, PaginaPrincipalaDoctor.class);
                startActivity(i);
                finish();

            }
        });
        medicatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(DetaliiPacienti.this, Medicatie.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
        istoricpac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(DetaliiPacienti.this, IstoricPacientDoctor.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
        localizeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("locatie").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Coordonate u=snapshot.getValue(Coordonate.class);
                            double lat,lg;
                            lat=u.getLat();
                            lg=u.getLgn();
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+lat+","+lg);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
