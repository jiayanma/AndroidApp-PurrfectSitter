package com.example.team21.ServerCall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

import java.util.List;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetViewHolder> {
    private final List<Planet> linkList;
    private final Context context;

    public PlanetAdapter(List<Planet> linkList, Context context) {
        this.linkList = linkList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanetViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_result_item_planet, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {
        holder.tv_planet_name.setText(linkList.get(position).getName());
        holder.tv_planet_population.setText(linkList.get(position).getPopulation());

        String surface_water = linkList.get(position).getSurface_water();
        holder.tv_planet_surface_water.setText(surface_water + (surface_water.equals("unknown") ? "" : "%"));
        String orbital_period = linkList.get(position).getOrbital_period();
        holder.tv_planet_orbital_period.setText(orbital_period + (orbital_period.equals("unknown") ? "" : " standard days"));
        holder.image_planet.setImageResource(R.drawable.planet);
        holder.image_population.setImageResource(R.drawable.population);
        holder.image_water.setImageResource(R.drawable.water);
        holder.image_orbit.setImageResource(R.drawable.orbit);
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }
}
