package com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class ReceivedApplyViewHolder extends RecyclerView.ViewHolder {
    public TextView post_title;
    public TextView applicant_name;
    public TextView applicant_email;
    public TextView message;
    public TextView time;
    public TextView status;
    public Button accept;
    public ImageView photo;
    public TextView review;

    public ReceivedApplyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.post_title = itemView.findViewById(R.id.tv_posttitle);
        this.applicant_name = itemView.findViewById(R.id.tv_applyName);
        this.applicant_email = itemView.findViewById(R.id.tv_applyEmail);
        this.message = itemView.findViewById(R.id.tv_message);
        this.time = itemView.findViewById(R.id.tv_time);
        this.status =itemView.findViewById(R.id.tv_status);
        this.accept = itemView.findViewById(R.id.button_accept);
        this.photo = itemView.findViewById(R.id.imageView_appPhoto);
        this.review = itemView.findViewById(R.id.tv_review);
    }
}
