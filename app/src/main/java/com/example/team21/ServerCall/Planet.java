package com.example.team21.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class Planet implements Parcelable {
    String name;
    String population;
    String surface_water;
    String orbital_period;

    public Planet(String name, String population, String surface_water, String orbital_period) {
        this.name = name;
        this.population = population;
        this.surface_water = surface_water;
        this.orbital_period = orbital_period;
    }

    public Planet(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        this.name = obj.getString("name");
        this.population = obj.getString("population");
        this.surface_water = obj.getString("surface_water");
        this.orbital_period = obj.getString("orbital_period");
    }

    public String toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("population", population);
        obj.put("surface_water", surface_water);
        obj.put("orbital_period", orbital_period);
        return obj.toString();
    }

    protected Planet(Parcel in) {
        name = in.readString();
        population = in.readString();
        surface_water = in.readString();
        orbital_period = in.readString();
    }

    public static final Creator<Planet> CREATOR = new Creator<Planet>() {
        @Override
        public Planet createFromParcel(Parcel in) {
            return new Planet(in);
        }

        @Override
        public Planet[] newArray(int size) {
            return new Planet[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPopulation() {
        return population;
    }

    public String getSurface_water() {
        return surface_water;
    }

    public String getOrbital_period() {
        return orbital_period;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(population);
        parcel.writeString((surface_water));
        parcel.writeString(orbital_period);
    }
}
