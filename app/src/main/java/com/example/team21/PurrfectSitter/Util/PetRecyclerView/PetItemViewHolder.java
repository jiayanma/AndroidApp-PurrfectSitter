package com.example.team21.PurrfectSitter.Util.PetRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class PetItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView photo;
    public TextView name;
    public TextView species;
    public TextView age;

    public PetItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.textView_petName);
        this.photo = itemView.findViewById(R.id.imageView_ownerPhoto);
        this.age = itemView.findViewById(R.id.textView_petAge);
        this.species = itemView.findViewById(R.id.textView_petSpecies);
    }
}

