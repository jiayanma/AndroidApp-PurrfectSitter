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

public class PeopleResultActivity extends AppCompatActivity {
    private ArrayList<People> linkList;
    private RecyclerView peopleRecyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_result);

        linkList = new ArrayList<>();
//        if (savedInstanceState != null) {
//            linkList = savedInstanceState.getParcelableArrayList("links");
//        }

        Intent intent = getIntent();
        String rsp = intent.getStringExtra("rsp");
        JSONArray arr = null;
        try {
            arr = new JSONArray(rsp);
            for (int i = 0; i < arr.length(); i++) {
//          Log.v("SearchResultActivity", arr.getString(i));
                linkList.add(new People(arr.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        peopleRecyclerView = findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        peopleRecyclerView.setLayoutManager(layoutManager);
        adapter = new PeopleAdapter(linkList, this);
        peopleRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("people", linkList);
        super.onSaveInstanceState(outState);
    }
}