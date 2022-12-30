package com.example.team21.ServerCall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
//    List<People> peopleList = new ArrayList<>();
//    List<Planet> planetList = new ArrayList<>();
//    private String searchCategory;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_result);
//
//        Intent intent = getIntent();
//        searchCategory = intent.getStringExtra("category");
//        String rsp = intent.getStringExtra("rsp");
//        try {
//            JSONArray arr = new JSONArray(rsp);
//            if (searchCategory.equals("people")) {
//                for (int i = 0; i < arr.length(); i++) {
////                    Log.v("SearchResultActivity", arr.getString(i));
//                    peopleList.add(new People(arr.getString(i)));
//                }
//            } else if (searchCategory.equals("planets")) {
//                for (int i = 0; i < arr.length(); i++) {
////                    Log.v("SearchResultActivity", arr.getString(i));
//                    planetList.add(new Planet(arr.getString(i)));
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//    }
}