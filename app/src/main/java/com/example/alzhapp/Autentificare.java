package com.example.alzhapp;

import static com.example.alzhapp.AESCrypt.decrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth fauth;

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
                String adresaMail1 = adresaMail.getText().toString();
                String parola1 = parola.getText().toString();
                fauth=FirebaseAuth.getInstance();
                if(adresaMail1.isEmpty()||parola1.isEmpty()){
                    Toast.makeText(Autentificare.this, "Completati ambele campuri", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(adresaMail1).matches())
                {
                    Toast.makeText(Autentificare.this,"Adaugati o adresa de mail valida!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    fauth.signInWithEmailAndPassword(adresaMail1,parola1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("users");
                                db.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String tip="";
                                        for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                            String s=snapshot1.child("adresaMail").getValue().toString();
                                            if (s.equals(adresaMail1)){
                                                tip=snapshot1.child("tipUtilizator").getValue().toString();
                                                System.out.println(tip);
                                                if (tip.equals("doctor")) {
                                                    Intent i = new Intent(Autentificare.this, com.example.alzhapp.PaginaPrincipalaDoctor.class);
                                                    startActivity(i);
                                                }
                                                else{
                                                    Intent i = new Intent(Autentificare.this, com.example.alzhapp.PaginaPrincipalaPacient.class);
                                                    startActivity(i);
                                                }
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(Autentificare.this,
                                        "Datele nu au fost introduse corect",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
    }}});
    }
}