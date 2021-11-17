package com.example.alzhapp;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Medicament> list;

    public MedicamentAdapter(Context context, ArrayList<Medicament> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.medicamente_afisare,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Medicament m = list.get(position);
        holder.name.setText(m.getNume());
        holder.oraRef.setText(m.getOra());
        holder.interval.setText(m.getInterv());
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Sunteti sigur ca vreti sa stergeti medicamentul?");
                builder.setMessage("Datele sterse nu pot fi recuperate!");
                builder.setPositiveButton("Stergeti", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String uid=list.get(holder.getAdapterPosition()).getUidPacient();
                        //delete from realtime db
                        int x=holder.getAdapterPosition();
                        FirebaseDatabase.getInstance().getReference().child("medicament").child(list.get(x).getUidPacient()).child(list.get(x).getNume()).removeValue();
                        //remove from app
                        list.remove(x);
                        notifyDataSetChanged();
                        Toast.makeText(holder.name.getContext(),"Medicament sters",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context, Medicatie.class);
                        intent.putExtra("uid",uid);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Anulare", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.name.getContext(),"Anulat",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView oraRef;
        TextView interval;
        Button delBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.numeM);
            oraRef=itemView.findViewById(R.id.oraRef);
            interval=itemView.findViewById(R.id.Interval);
            delBtn=itemView.findViewById(R.id.delBtn);

        }
    }

}