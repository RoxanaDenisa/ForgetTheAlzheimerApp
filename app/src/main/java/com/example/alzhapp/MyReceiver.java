package com.example.alzhapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.Channel;
import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {
    private Context c;
    public Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    public static Ringtone ringtone;
    private DatabaseReference db1;
    private DatabaseReference db2;
    private static int ok=0;
    private static boolean shutdown=false;
    private Handler handler;
    private static int oraApel;
    public static String nume;
    @Override
    public void onReceive(Context context, Intent intent) {
        c=context;
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        PaginaPrincipalaPacient.sw=0;
        nume=intent.getStringExtra("nume");
        notificare(nume);
        Calendar c=Calendar.getInstance();
        oraApel=c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);;
        shutdown=false;
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (shutdown==false) {
                    verificare();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        if (shutdown==false) {
            handler.postDelayed(r, 1000);
        }

    }

    public void verificare(){

        db1= FirebaseDatabase.getInstance().getReference().child("cutie");
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cutie c=snapshot.getValue(Cutie.class);
                if(c.getMiercuri()==1 && ok==0){
                    ok++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db2= FirebaseDatabase.getInstance().getReference().child("bratara");
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bratara b=snapshot.getValue(Bratara.class);
                if(b.getRidicat()==1 && ok==1){
                    ok++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Calendar calend=Calendar.getInstance();
        if (calend.get(Calendar.HOUR_OF_DAY)*60+ calend.get(Calendar.MINUTE)==oraApel+2){

            shutdown=true;

        }
        if(ok==2 ){

            shutdown=true;
        }

    }
    public static int getOraApel(){
        return oraApel;
    }
    public static int getOk(){
        return ok;
    }



    public static void setOk(int sw){
        ok=sw;
    }
    public void notificare( String n){
        createNotificationChannel();
        NotificationCompat.Builder builder= new NotificationCompat.Builder(c, "lemubitA").setSmallIcon(R.drawable.ic_action_exclam).setContentTitle("Reminder medicament").setContentText("Luati medicamentul "+ n).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(c);
        notificationManager.notify(100, builder.build());
        Toast.makeText(c, "Luati medicamentul "+n, Toast.LENGTH_LONG).show();
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name= "canal";
            String description= "canal pentru notificari medicamente";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("lemubitA", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager=c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
