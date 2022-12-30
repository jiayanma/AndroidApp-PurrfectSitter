package com.example.team21.ServerCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.team21.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PlanetResultActivity extends AppCompatActivity {
    private ArrayList<Planet> linkList;
    private RecyclerView planetRecyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_result);

        linkList = new ArrayList<>();
//        if (savedInstanceState != null && savedInstanceState.containsKey("planetList")) {
//            planetList = savedInstanceState.getParcelableArrayList("planetList");
//        }

        Intent intent = getIntent();
        String rsp = intent.getStringExtra("rsp");
        JSONArray arr = null;
        try {
            arr = new JSONArray(rsp);
            for (int i = 0; i < arr.length(); i++) {
//          Log.v("SearchResultActivity", arr.getString(i));
                linkList.add(new Planet(arr.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        planetRecyclerView = findViewById(R.id.rv_planet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        planetRecyclerView.setLayoutManager(layoutManager);
        adapter = new PlanetAdapter(linkList, this);
        planetRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate) {
        outstate.putParcelableArrayList("planet", linkList );
        super.onSaveInstanceState(outstate);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}