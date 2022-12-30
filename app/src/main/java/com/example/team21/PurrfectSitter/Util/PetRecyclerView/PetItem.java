package com.example.team21.PurrfectSitter.Util.PetRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.team21.PurrfectSitter.FirebaseClass.Pet;

public class PetItem implements Parcelable {
    private final String name;
    private final String species;
    private final int age;
    private final String photo;

    protected PetItem(Parcel in, String name, String species, int age, String photo) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.photo = photo;
    }

    public PetItem(Pet pet) {
        this.name = pet.petName;
        this.species = pet.species;
        this.age = pet.age;
        this.photo = pet.image;
    }
    public PetItem(Parcel in) {
        name = in.readString();
        species = in.readString();
        age = in.readInt();
        photo = in.readString();
    }

    public static final Creator<PetItem> CREATOR = new Creator<PetItem>() {
        @Override
        public PetItem createFromParcel(Parcel in) {
            return new PetItem(in);
        }

        @Override
        public PetItem[] newArray(int size) {
            return new PetItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getAge() {
        return String.valueOf(age);
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(species);
        parcel.writeInt(age);
        parcel.writeString(photo);

    }
}
