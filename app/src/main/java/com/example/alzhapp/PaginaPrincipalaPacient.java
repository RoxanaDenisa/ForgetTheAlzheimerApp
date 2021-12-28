package com.example.alzhapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button apeleaza_supraveghetor;
    private Button apeleaza_doctor;
    private LocationRequest lr;
    private FusedLocationProviderClient locatie;
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference db;
    IstoricAdapter istoricAdapter;
    ArrayList<Istoric> list;
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
//aici
                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    String n=m.getNume();
                    String o=m.getOra();
                    String i=m.getInterv();
                    int oi=Integer.valueOf(o);
                    int ii=Integer.valueOf(i);
                    int poz=0;
                    while((oi+ii*poz)<24){

                        // apelarea functiei de setare a alarmei ce are ca si parametru ora la care setez alarma
                        setAlarm(oi+ii*poz);

                        //Aadugarea in lista istoric pt adapter
                        String ora=String.valueOf(oi+ii*poz);
                        Istoric istoric=new Istoric(n,ora);
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
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }
            else{
                Toast.makeText(this, "Aplicatia necesita permisiuni", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        }
    }

    //alarma trebuie sa tina maxim jumatate de ora si minim pana isi ia pastila (ceva venit de la bratara)
    //daca dupa 30min nu si.a luat pastila va aparea alerta la doctor pt pacientul respectiv
    private void setAlarm(int ora) {
        Calendar cal= Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                ora,
                0,
                0);
        if (cal.getTime().after(Calendar.getInstance().getTime())){
                AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent= new Intent(this, MyReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ora, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        }
    }
    private void Alarm_cancel(int ora) {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ora, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(this,"Your Alarm is Cancel",Toast.LENGTH_LONG).show();
        MyReceiver.ringtone.stop();
    }
    private void updateGPS(){
        locatie= LocationServices.getFusedLocationProviderClient(PaginaPrincipalaPacient.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            locatie.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                         updateUIValues(location);
                }
            });
        }
        else{
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }
    private void updateUIValues(Location location){
        System.out.println(String.valueOf(location.getLatitude()));
        System.out.println(String.valueOf(location.getLongitude()));
        //locatie= LocationServices.getFusedLocationProviderClient(DetaliiPacienti.this);
    }
    }
