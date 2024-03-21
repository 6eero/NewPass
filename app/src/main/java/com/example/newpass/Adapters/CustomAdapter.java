package com.example.newpass.Adapters;

import android.annotation.SuppressLint;
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

import com.example.newpass.R;
import com.example.newpass.Activities.UpdateActivity;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<String> row_id;
    private final ArrayList<String> row_name;
    private final ArrayList<String> row_email;
    private final ArrayList<String> row_password;
    private final Activity activity;

    public CustomAdapter(Activity activity, Context context, ArrayList<String> row_id, ArrayList<String> row_name, ArrayList<String> row_email, ArrayList<String> row_password) {
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

    /**
     * Populate every row of the recyclerView in the main activity
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String name = String.valueOf(row_name.get(holder.getAdapterPosition()));
        String email = String.valueOf(row_email.get(holder.getAdapterPosition()));

        String tw;
        if (name.length() > 2) {
            tw = name.substring(0, 2);
        } else {
            tw = name;
        }

        holder.row_tw_txt.setText(tw);
        holder.row_name_txt.setText(name);
        holder.row_email_txt.setText(email);

        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("entry", String.valueOf(row_id.get(position)));
            intent.putExtra("name", String.valueOf(row_name.get(position)));
            intent.putExtra("email", String.valueOf(row_email.get(position)));
            intent.putExtra("password", String.valueOf(row_password.get(position)));   // change this to change the displayed password in the update activity
            activity.startActivityForResult(intent, 1);
        });
    }

    /**
     * Get the number of password saved in the database
     * @return the number of password saved in the database
     */
    @Override
    public int getItemCount() {
        return row_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView row_name_txt, row_email_txt, row_tw_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            row_name_txt = itemView.findViewById(R.id.row_name_txt);
            row_tw_txt = itemView.findViewById(R.id.row_tw_txt);
            row_email_txt = itemView.findViewById(R.id.row_email_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
