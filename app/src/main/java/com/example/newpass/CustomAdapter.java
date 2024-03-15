package com.example.newpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList row_id, row_name, row_email, row_password;
    private Activity activity;

    CustomAdapter(Activity activity, Context context, ArrayList row_id, ArrayList row_name, ArrayList row_email, ArrayList row_password) {
        this.activity = activity;
        this.context = context;
        this.row_id = row_id;
        this.row_name = row_name;
        this.row_email = row_email;
        this.row_password = row_password;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.row_id_txt.setText(String.valueOf(row_id.get(position)));
        holder.row_name_txt.setText(String.valueOf(row_name.get(position)));
        holder.row_email_txt.setText(String.valueOf(row_email.get(position)));
        //holder.row_password_txt.setText(String.valueOf(row_password.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("entry", String.valueOf(row_id.get(position)));
                intent.putExtra("name", String.valueOf(row_name.get(position)));
                intent.putExtra("email", String.valueOf(row_email.get(position)));
                intent.putExtra("password", String.valueOf(row_password.get(position)));   // change this to change the displayed password in the update activity
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return row_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView row_id_txt, row_name_txt, row_email_txt, row_password_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            row_id_txt = itemView.findViewById(R.id.row_id_txt);
            row_name_txt = itemView.findViewById(R.id.row_name_txt);
            row_email_txt = itemView.findViewById(R.id.row_email_txt);
            //row_password_txt = itemView.findViewById(R.id.row_password_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
