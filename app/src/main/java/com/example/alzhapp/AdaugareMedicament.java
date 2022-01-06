package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdaugareMedicament extends AppCompatActivity {
    private EditText numeMedicament;
    private EditText oraReferinta;
    private EditText interval;
    private Button salveaza;
    private DatabaseReference db;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String uid=getIntent().getStringExtra("uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_medicament);

        numeMedicament=findViewById(R.id.Nume_medicament);
        oraReferinta=findViewById(R.id.ora_referinta);
        interval=findViewById(R.id.interval);
        salveaza=findViewById(R.id.salvare);
        db= FirebaseDatabase.getInstance().getReference();
        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdaugareMedicament.this, Medicatie.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();

            }
        });
        salveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String numeMedicament1=numeMedicament.getText().toString();
               String oraReferinta1=oraReferinta.getText().toString();
               String interval1=interval.getText().toString();
               //verificare daca toate campurile sunt completate
                if (numeMedicament1.equals("") || oraReferinta1.equals("") || interval1.equals("")){
                    Toast.makeText(AdaugareMedicament.this,"Toate câmpurile trebuie completate", Toast.LENGTH_SHORT).show();
                }
                else{
                    Medicament m=new Medicament(numeMedicament1,oraReferinta1,interval1,uid,0);
                    db.child("medicament").child(uid).child(numeMedicament1).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdaugareMedicament.this, "Medicament adaugat", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(AdaugareMedicament.this, Medicatie.class);
                                i.putExtra("uid",uid);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(AdaugareMedicament.this, "Nu s-a putut adauga medicamentul", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


    }
}