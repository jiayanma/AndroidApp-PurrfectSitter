package com.example.team21.PurrfectSitter.Util.PetRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.R;

import java.util.List;

public class PetItemAdapter extends RecyclerView.Adapter<PetItemViewHolder> {
    private List<PetItem> pets;
    private Context context;

    public PetItemAdapter(List<PetItem> pets, Context context) {
        this.pets = pets;
        this.context = context;
    }

    @NonNull
    @Override
    public PetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PetItemViewHolder(LayoutInflater.from(context).inflate(R.layout.pet_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PetItemViewHolder holder, int position) {
        holder.name.setText("Name: " + pets.get(position).getName());
        holder.species.setText("Species: " +pets.get(position).getSpecies());
        holder.age.setText("Age: " + pets.get(position).getAge());
        String photoUrl = pets.get(position).getPhoto();
        Helper.loadImageViewFromURL(context, photoUrl, holder.photo);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }
}
