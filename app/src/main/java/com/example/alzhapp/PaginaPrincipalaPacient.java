package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PaginaPrincipalaPacient extends AppCompatActivity {
    private Button apeleaza_supraveghetor;
    private Button apeleaza_doctor;
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference db;
    IstoricAdapter istoricAdapter;
    ArrayList<Istoric> list;

    private  AlarmManager alarmManager;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principala_pacient);


        recyclerView = findViewById(R.id.myBoxIstoric);
        database = FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Istoric>();
        istoricAdapter = new IstoricAdapter(this,list);
        recyclerView.setAdapter(istoricAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    String n=m.getNume();
                    String o=m.getOra();
                    String i=m.getInterv();
                    int oi=Integer.valueOf(o);
                    int ii=Integer.valueOf(i);
                    int poz=0;
                    while((oi+ii*poz)<24){
                        String ora=String.valueOf(oi+ii*poz);

                        Istoric istoric=new Istoric(n,ora);
                        //if(!checkAlarm(oi+ii*poz))
                            setAlarm(oi+ii*poz,n);
                            System.out.println(oi+ii*poz);
                        poz++;
                        list.add(istoric);
                        Collections.sort(list, Istoric.ordonare);

                    }

                }

                istoricAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        apeleaza_supraveghetor=findViewById(R.id.apeleaza_supraveghetor);
        apeleaza_doctor=findViewById(R.id.apeleaza_doctor);


        apeleaza_supraveghetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users u=snapshot.getValue(Users.class);
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+u.getTelefonSupraveghetor()));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        apeleaza_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference().child("users");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String mail="";
                        for(DataSnapshot ds:snapshot.getChildren()) {
                            Users u = ds.getValue(Users.class);
                            if (u.getTipUtilizator().equals("pacient")) {
                                if (u.getUid().equals(uid)) {
                                    mail = u.getMailDoctor();
                                    System.out.println(mail+"aiiiiiiiiiiiiiiiiiiiiiiici");
                                    System.out.println(uid+"aiiiiiiiiiiiiiiiiiiiiiiici");
                                }
                            }

                        }
                        for (DataSnapshot ds:snapshot.getChildren()){
                            Users u=ds.getValue(Users.class);
                            if (u.getAdresaMail().equals(mail)){
                                Intent intent=new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+u.getTelefon()));
                                startActivity(intent);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    /*public void setAlarm(int ora){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 21);
        calendar.set(Calendar.SECOND, 0);
        System.out.println(ora+"AICIIIIIIIIIIIIIIIIIIIII" + calendar.get(Calendar.DAY_OF_YEAR) );
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    private boolean checkAlarm(int ora) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_RECEIVER);
        boolean isSet = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_NO_CREATE) != null;
        Log.e("MainActivity", isSet + " :Alarm is set");
        return isSet;
    }*/
    private void setAlarm(int ora,String nume){
        Intent intent=new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,ora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES,0);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE,nume);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else {
            Toast.makeText(PaginaPrincipalaPacient.this, "nu e bine",Toast.LENGTH_SHORT).show();
        }


    }
}