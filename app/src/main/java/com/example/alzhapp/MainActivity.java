package com.example.alzhapp;
import static com.example.alzhapp.AESCrypt.encrypt;

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
    private EditText mailDoctor;
    private Button sendData;
    private DatabaseReference dbRef;
    private FirebaseAuth fauth;

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
        mailDoctor=findViewById(R.id.Mail_doctor);
        sendData=findViewById(R.id.buton_send);
        dbRef= FirebaseDatabase.getInstance().getReference();
        fauth=FirebaseAuth.getInstance();
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
                String confirmareParola1=confirmareParola.getText().toString();
                String mailDoctor1=mailDoctor.getText().toString();

                //verificare daca sunt completate toate campurile
                if (numeComplet1.equals("") || adresa1.equals("") || telefon1.equals("")||numeSupraveghetor1.equals("")|| telefonSupraveghetor1.equals("")||adresaMail1.equals("")||parola1.equals("")||confirmareParola1.equals("")||mailDoctor1.equals("")){
                    Toast.makeText(MainActivity.this,"Toate câmpurile trebuie completate", Toast.LENGTH_SHORT).show();

                }
                //verificare mail valid
                else if(!Patterns.EMAIL_ADDRESS.matcher(adresaMail1).matches())
                {
                    Toast.makeText(MainActivity.this,"Adaugati o adresa de mail valida!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //verificare daca parolele corespund
                    if (parola1.equals(confirmareParola1)) {

                       //adaugare date in auth si real time db
                        fauth.createUserWithEmailAndPassword(adresaMail1,parola1).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            try{
                                                String newpas = encrypt(parola1);
                                                Users pacient = new Users(FirebaseAuth.getInstance().getCurrentUser().getUid(),numeComplet1, adresa1, telefon1, numeSupraveghetor1, telefonSupraveghetor1, adresaMail1, newpas, mailDoctor1, "pacient");
                                                dbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(pacient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(MainActivity.this, "Cont creat", Toast.LENGTH_SHORT).show();
                                                            Intent i=new Intent(MainActivity.this, com.example.alzhapp.Autentificare.class);
                                                            startActivity(i);
                                                        }
                                                          else
                                                              {
                                                                  Toast.makeText(MainActivity.this, "eroare creare cont", Toast.LENGTH_SHORT).show();
                                                              }
                                                    }
                                                });
                                            }catch(Exception e){
                                                Toast.makeText(MainActivity.this, "Eroare creare cont!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });

                    } else {

                        Toast.makeText(MainActivity.this, "Introduceți aceeași parolă", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

}