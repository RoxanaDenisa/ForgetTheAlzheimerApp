package com.example.alzhapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private DatabaseReference dbRef;
    //pt localizare
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    public  static int sw=0;
    //pt repetitie
    private Handler mHandler = new Handler();
    private Handler mHandler2 = new Handler();
    private String uid;
    private int l=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principala_pacient);

        //permisiuni localizare
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        recyclerView = findViewById(R.id.myBoxIstoric);
        database = FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Istoric>();
        istoricAdapter = new IstoricAdapter(this, list, uid);
        recyclerView.setAdapter(istoricAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Medicament m = dataSnapshot.getValue(Medicament.class);
                    String n = m.getNume();
                    String o = m.getOra();
                    String i = m.getInterv();
                    int oi = Integer.valueOf(o);
                    int ii = Integer.valueOf(i);
                    int poz = 0;
                    while ((oi + ii * poz) < 24) {

                        // apelarea functiei de setare a alarmei ce are ca si parametru ora la care setez alarma
                        setAlarm(n,oi + ii * poz);

                        //Aadugarea in lista istoric pt adapter
                        String ora = String.valueOf(oi + ii * poz);
                        Istoric istoric = new Istoric(n, ora);
                        poz++;
                        add(list,istoric);
                        Collections.sort(list, Istoric.ordonare);

                    }

                }

                istoricAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        apeleaza_supraveghetor = findViewById(R.id.apeleaza_supraveghetor);
        apeleaza_doctor = findViewById(R.id.apeleaza_doctor);


        apeleaza_supraveghetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users u = snapshot.getValue(Users.class);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + u.getTelefonSupraveghetor()));
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
                        String mail = "";
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users u = ds.getValue(Users.class);
                            if (u.getTipUtilizator().equals("pacient")) {
                                if (u.getUid().equals(uid)) {
                                    mail = u.getMailDoctor();
                                }
                            }

                        }
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users u = ds.getValue(Users.class);
                            if (u.getAdresaMail().equals(mail)) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + u.getTelefon()));
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

        mToastRunnable.run();
        mToastRunnable1.run();

    }


    //alarma trebuie sa tina maxim jumatate de ora si minim pana isi ia pastila (ceva venit de la bratara)
    //daca dupa 30min nu si.a luat pastila va aparea alerta la doctor pt pacientul respectiv
    private void setAlarm(String nume,int ora) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                ora,
                8,
                0);
        if (cal.getTime().after(Calendar.getInstance().getTime())) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, MyReceiver.class);
            Bundle b = new Bundle();
            b.putString("nume", nume);
            intent.putExtras(b);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ora, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
    }

    public void Alarm_cancel(int ora) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ora, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(this, "Alarma a fost anulată", Toast.LENGTH_SHORT).show();
        MyReceiver.ringtone.stop();
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PaginaPrincipalaPacient.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    //locatia:
    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            getLocation();
            mHandler.postDelayed(this, 1000*10);
        }
    };
    //bratara si cutie:
    private Runnable mToastRunnable1 = new Runnable() {
        @Override
        public void run() {
            Calendar calend=Calendar.getInstance();

            if (calend.get(Calendar.HOUR_OF_DAY)*60+ calend.get(Calendar.MINUTE)==MyReceiver.getOraApel()+1){
                db= FirebaseDatabase.getInstance().getReference().child("users");
                Map<String, Object> map=new HashMap<>();
                map.put("medicament_neluat",true);
                db.child(uid).updateChildren(map);

                modificare_luat(MyReceiver.nume,calend.get(Calendar.HOUR_OF_DAY));

                Alarm_cancel(calend.get(Calendar.HOUR_OF_DAY));
                }
            if (calend.get(Calendar.HOUR_OF_DAY)*60+ calend.get(Calendar.MINUTE)==MyReceiver.getOraApel()+5){
                MyReceiver.setOk(0);}
            if(MyReceiver.getOk()==2) {
                Alarm_cancel(calend.get(Calendar.HOUR_OF_DAY));
                if (sw==0){
                recyclerView.invalidate();
                recyclerView.setAdapter(istoricAdapter);
                sw++;
                }
                //MyReceiver.setOk(0);
                //dbRef= FirebaseDatabase.getInstance().getReference();
                //Bratara b= new Bratara(0);
                //dbRef.child("bratara").setValue(b);

            }



            mHandler2.postDelayed(this, 1000);

        }
    };

    private void getLocation(){

        locationTrack = new LocationTrack(PaginaPrincipalaPacient.this);


        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            if(latitude!=0&&longitude!=0){
            dbRef= FirebaseDatabase.getInstance().getReference();
            Coordonate coord = new Coordonate(latitude, longitude);
            dbRef.child("locatie").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(coord);

        } }
        else {
            locationTrack.showSettingsAlert();
        }
    }
    private void modificare_luat(String nume, int ora){
        //citire luat din bd
        db= FirebaseDatabase.getInstance().getReference().child("medicament").child(uid);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Medicament u=ds.getValue(Medicament.class);

                        if(u.getNume().equals(nume)) {
                            l=u.getLuat();

                        }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println(MyReceiver.getOk() + "Afiseaza ok AIIIIIIIIIIIIIIIIIIIIIIIICI");
        System.out.println(l);
        //1 trebuie shiftat pe biti  la stanga cu ora
        if(l!=-1 ){
            if (MyReceiver.getOk()!=2)
                l=l|(1<<(ora-1));
            //modif in bd
            db= FirebaseDatabase.getInstance().getReference().child("medicament");
            Map<String, Object> map2=new HashMap<>();
            map2.put("luat",l);
            db.child(uid).child(nume).updateChildren(map2);
            System.out.println(ora);

        }


    }
    private void add(ArrayList<Istoric> l, Istoric i){
        for (Istoric ist: l){
            if (ist.getNume().equals(i.getNume())&& ist.getOraInt()==i.getOraInt()){
                l.remove(ist);
                list.add(i);
                return;
            }
        }
        list.add(i);
    }
    }
