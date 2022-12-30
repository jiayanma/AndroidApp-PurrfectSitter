package com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;

public class PostBriefViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView requestDate;
    public TextView location;
    public TextView pets;
    public ImageView coverPhoto;

    private ItemClickListenerInterface clickListener;

    public PostBriefViewHolder(@NonNull View itemView, ItemClickListenerInterface clickListener) {
        super(itemView);
        this.requestDate = itemView.findViewById(R.id.requestDateTV);
        this.location = itemView.findViewById(R.id.locationTV);
        this.pets = itemView.findViewById(R.id.petsTV);
        this.coverPhoto = itemView.findViewById(R.id.reviewerPhotoIV);
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onItemClick(view, getLayoutPosition());
        }
    }
}
