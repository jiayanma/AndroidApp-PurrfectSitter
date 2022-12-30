package com.example.team21.ServerCall;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class PlanetViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_planet_name;
    public TextView tv_planet_population;
    public TextView tv_planet_surface_water;
    public TextView tv_planet_orbital_period;
    public ImageView image_planet;
    public ImageView image_population;
    public ImageView image_water;
    public ImageView image_orbit;

    public PlanetViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_planet_name = itemView.findViewById(R.id.tv_planet_name);
        this.tv_planet_population = itemView.findViewById(R.id.tv_planet_population);
        this.tv_planet_surface_water = itemView.findViewById(R.id.tv_planet_surface_water);
        this.tv_planet_orbital_period = itemView.findViewById(R.id.tv_planet_orbital_period);
        this.image_planet = itemView.findViewById(R.id.imageViewPlanet);
        this.image_population = itemView.findViewById(R.id.imageViewPopulation);
        this.image_water = itemView.findViewById(R.id.imageViewWater);
        this.image_orbit = itemView.findViewById(R.id.imageViewOrbit);
    }
}
