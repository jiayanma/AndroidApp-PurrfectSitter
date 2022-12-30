package com.example.team21.PurrfectSitter.Util.EditPetRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.team21.PurrfectSitter.FirebaseClass.Pet;

public class EditPetItem implements Parcelable {
    private String name;
    private String species;
    private int age;
    private String avatar;
    private String key;

    public EditPetItem(String name, String species, int age, String avatar, String key) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.avatar = avatar;
        this.key = key;
    }

    public EditPetItem(Pet pet, String key) {
        this.name = pet.petName;
        this.species = pet.species;
        this.age = pet.age;
        this.avatar = pet.image;
        this.key = key;
    }

    protected EditPetItem(Parcel in) {
        name = in.readString();
        species = in.readString();
        age = in.readInt();
        avatar = in.readString();
        key = in.readString();
    }

    public static final Creator<EditPetItem> CREATOR = new Creator<EditPetItem>() {
        @Override
        public EditPetItem createFromParcel(Parcel in) {
            return new EditPetItem(in);
        }

        @Override
        public EditPetItem[] newArray(int size) {
            return new EditPetItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getAge() {
        return age;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(species);
        dest.writeInt(age);
        dest.writeString(avatar);
        dest.writeString(key);
    }
}
