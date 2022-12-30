package com.example.team21.ServerCall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team21.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText editText;
    private String searchCategory;
    private String searchInput;
    private final String API_URL = "http://swapi.dev/api/";
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        radioGroup = findViewById(R.id.categoryRG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                searchCategory = radioButton.getText().toString().toLowerCase();
            }
        });

        editText = findViewById(R.id.searchET);
        searchCategory = "";
        searchInput = "";

        loading = findViewById(R.id.pb_loading);
    }

    public void requestListener(View view) {
        if (searchCategory.isEmpty()) {
            Toast.makeText(this, "Please choose a search category", Toast.LENGTH_LONG).show();
            return;
        }
        searchInput = editText.getText().toString();
        if (searchInput.isEmpty()) {
            Toast.makeText(this, "Please enter a search input to start", Toast.LENGTH_LONG).show();
            return;
        }
        Log.v("MainActivity2", "try request");

        loading.setVisibility(View.VISIBLE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        StarWarService service = retrofit.create(StarWarService.class);
        if (searchCategory.equals("people")) {
            Call<PeopleResponse> call = service.getPeople(searchInput);
            call.enqueue(new Callback<PeopleResponse>() {
                @Override
                public void onResponse(@NonNull Call<PeopleResponse> call, @NonNull Response<PeopleResponse> response) {
                    if (response.body() == null) {
                        Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                        return;
                    }
                    List<People> peopleList = response.body().getResults();
                    try {
                        JSONArray arr = new JSONArray();
                        for (People pp : peopleList) {
                            arr.put(pp.toJSON());
                        }
                        if (peopleList.isEmpty()) {
                            Intent intent = new Intent(getBaseContext(), NilSearchResult.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getBaseContext(), PeopleResultActivity.class);
                            intent.putExtra("category", searchCategory);
                            intent.putExtra("rsp", arr.toString());
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                    }
                    loading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<PeopleResponse> call, @NonNull Throwable t) {
                    Log.v("MainActivity2", "people request failed");
                    Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.INVISIBLE);
                }
            });
        } else if (searchCategory.equals("planets")) {
            Call<PlanetsResponse> call = service.getPlanets(searchInput);
            call.enqueue(new Callback<PlanetsResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlanetsResponse> call, @NonNull Response<PlanetsResponse> response) {
                    if (response.body() == null) {
                        Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                        return;
                    }
                    List<Planet> planetList = response.body().getResults();
                    try {
                        JSONArray arr = new JSONArray();
                        for (Planet pl : planetList) {
                            arr.put(pl.toJSON());
                        }
                        if (planetList.isEmpty()) {
                            Intent intent = new Intent(getBaseContext(), NilSearchResult.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getBaseContext(), PlanetResultActivity.class);
                            intent.putExtra("category", searchCategory);
                            intent.putExtra("rsp", arr.toString());
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                    }
                    loading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<PlanetsResponse> call, @NonNull Throwable t) {
                    Log.v("MainActivity2", "people request failed");
                    Toast.makeText(view.getContext(), "Error when calling webserver", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}