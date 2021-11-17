package com.example.alzhapp;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Users> list;

    public MyAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ppdoctor_pacienti,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Users user = list.get(position);
        holder.name.setText(user.getNumeComplet());

        //vizualizare informatii pacienti
        holder.showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=list.get(holder.getAdapterPosition()).getUid();
                Intent i=new Intent(context, com.example.alzhapp.DetaliiPacienti.class);
                i.putExtra("uid",uid);
                context.startActivity(i);

            }
        });
        //stergere din baza de date

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Sunteti sigur ca vreti sa stergeti pacientul?");
                builder.setMessage("Datele sterse nu pot fi recuperate!");
                builder.setPositiveButton("Stergeti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete from realtime db

                        FirebaseDatabase.getInstance().getReference().child("medicament").child(list.get(holder.getAdapterPosition()).getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("users").child(list.get(holder.getAdapterPosition()).getUid()).removeValue();
                        //remove from app
                        list.remove(list.get(holder.getAdapterPosition()));
                        notifyDataSetChanged();
                        System.out.println("Successfully deleted user.");
                        Toast.makeText(holder.name.getContext(),"Pacient sters",Toast.LENGTH_SHORT).show();
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
        ImageButton delBtn;
        ImageButton showInfo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            delBtn=itemView.findViewById(R.id.delete_btn);
            showInfo=itemView.findViewById(R.id.view_btn);

        }
    }

}