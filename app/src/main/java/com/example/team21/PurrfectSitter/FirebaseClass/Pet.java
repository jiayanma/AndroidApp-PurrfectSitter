package com.example.team21.PurrfectSitter.FirebaseClass;

public class Pet {
    public String petName;
    public String species;
    public int age;
    public String image;

    public Pet() {

    }

    public Pet(String petName, String species, int age, String image) {
        this.petName = petName;
        this.species = species;
        this.age = age;
        this.image = image;
    }
}
