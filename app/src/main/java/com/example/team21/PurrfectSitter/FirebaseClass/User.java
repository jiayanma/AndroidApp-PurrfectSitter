package com.example.team21.PurrfectSitter.FirebaseClass;

import com.example.team21.PurrfectSitter.FirebaseClass.Pet;

import java.util.HashMap;

public class User {
    public String username;
    public String displayName;
    public String avatar;
    public HashMap<String, Pet> pets;
    public String email;

    public User() {}

    public User(String username, String displayName, String avatar, HashMap<String, Pet> pets,
                String email) {
        this.username = username;
        this.displayName = displayName;
        this.avatar = avatar;
        this.pets = pets;
        this.email = email;
    }
}
