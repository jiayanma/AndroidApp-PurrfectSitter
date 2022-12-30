package com.example.team21.PurrfectSitter.Util.MyAppliedRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class MyAppliedViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView date;
    public TextView host;
    public TextView status;
    public TextView writeReview;

    public MyAppliedViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.titleContentTV);
        this.date = itemView.findViewById(R.id.dateContentTV);
        this.host = itemView.findViewById(R.id.hostContentTV);
        this.status = itemView.findViewById(R.id.statusTV);
        this.writeReview = itemView.findViewById(R.id.reviewTV);
    }
}
