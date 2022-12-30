package com.example.team21.PurrfectSitter.Util.EditPetRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class EditPetItemViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView species;
    public TextView age;
    public ImageView avatar;
    public ImageView delete;


   public EditPetItemViewHolder(@NonNull View view) {
       super(view);
       this.name = view.findViewById(R.id.tv_pet_name);
       this.species = view.findViewById(R.id.tv_pet_species);
       this.age = view.findViewById(R.id.tv_pet_age);
       this.avatar = view.findViewById(R.id.iv_pet_avt);
       this.delete = view.findViewById(R.id.iv_delete);

   }


}
