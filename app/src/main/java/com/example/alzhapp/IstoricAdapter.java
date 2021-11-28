package com.example.alzhapp;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


public class IstoricAdapter extends RecyclerView.Adapter<IstoricAdapter.MyViewHolder> {

    Context context;
    ArrayList<Istoric> list;

    public IstoricAdapter(Context context, ArrayList<Istoric> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.istoric_pacient,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Istoric m = list.get(position);
        holder.name.setText(m.getNume());
        holder.ora.setText(m.getOra());
       /* Calendar calendar = Calendar.getInstance();
        if (calendar.before(Calendar.getInstance())) {
            holder.v1.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageButton v1;
        ImageButton v2;
        TextView name;
        TextView ora;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.numeMed);
            ora=itemView.findViewById(R.id.ora);
            v1=itemView.findViewById(R.id.verificare);
            v2=itemView.findViewById(R.id.verif);

        }
    }

}