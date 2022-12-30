package com.example.team21.PurrfectSitter.FirebaseClass;

public class Location {
    public String streetAddress;
    public String city;
    public String state;
    public String zipcode;

    public Location() {}

    public Location (String streetAddress, String city, String state, String zipcode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }
}
