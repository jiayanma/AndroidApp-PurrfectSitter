package com.example.team21.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class People implements Parcelable {
    String name;
    String hair_color;
    String eye_color;
    String gender;

    public People(String name, String hair_color, String eye_color, String gender) {
        this.name = name;
        this.eye_color = eye_color;
        this.hair_color = hair_color;
        this.gender = gender;
    }

    public People(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        this.name = obj.getString("name");
        this.hair_color = obj.getString("hair_color");
        this.eye_color = obj.getString("eye_color");
        this.gender = obj.getString("gender");
    }

    public String toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("hair_color", hair_color);
        obj.put("eye_color", eye_color);
        obj.put("gender", gender);
        return obj.toString();
    }

    protected People(Parcel in) {
        name = in.readString();
        hair_color = in.readString();
        eye_color = in.readString();
        gender = in.readString();
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getHair_color() {
        return hair_color;
    }

    public String getEye_color() {
        return eye_color;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(hair_color);
        parcel.writeString(eye_color);
        parcel.writeString(gender);
    }
}
