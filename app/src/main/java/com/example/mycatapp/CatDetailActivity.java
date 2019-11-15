package com.example.mycatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CatDetailActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView weightTextView;
    TextView dogTextView;
    TextView temperTextview;
    TextView originTextView;
    TextView lifespanTextView;
    TextView linkTextView;
    TextView descriptionTextView;
    ImageView catImageView;
    CatImage catImage;
    ArrayList<CatImage> catImageAll;
    CatDatabase db;
    CatDetailActivity activity;
    ToggleButton favouriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final RequestQueue requestQueue =Volley.newRequestQueue( this );
        String imageId = intent.getStringExtra("imageId");
        final String URL= "https://api.thecatapi.com/v1/images/search?breed_ids=" + imageId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type catImageType = new TypeToken<ArrayList<CatImage>>(){}.getType();
                catImageAll = gson.fromJson(response, catImageType);
                catImage = catImageAll.get(0);
                getImage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }  ) {
        };
        requestQueue.add(stringRequest);

        nameTextView = findViewById(R.id.name);
        dogTextView = findViewById(R.id.df_label);
        temperTextview = findViewById(R.id.temper);
        originTextView = findViewById(R.id.origin);
        linkTextView = findViewById(R.id.link);
        lifespanTextView = findViewById(R.id.lifespan);
        descriptionTextView = findViewById(R.id.description);
        catImageView = findViewById(R.id.photo);
        favouriteButton = findViewById(R.id.favourite_button);

        final CatDatabase db = CatDatabase.getInstance(this);
        Cat cat = db.catDao().findCatByName(name);
        System .out.println(cat.getName());
        nameTextView.setText(cat.getName());
        dogTextView.setText(cat.getDog_friendly());
        temperTextview.setText(cat.getTemperament());
        originTextView.setText(cat.getOrigin());
        linkTextView.setText(cat.getWikipedia_url());
        lifespanTextView.setText(cat.getLife_span());
        descriptionTextView.setText(cat.getDescription());

        favouriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add();
                Toast.makeText(getApplicationContext(),"Cat added to your Favourites!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void add() {
        ArrayList<Cat> favouritesList;
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final CatDatabase db = CatDatabase.getInstance(this);
        Cat cat = db.catDao().findCatByName(name);

        Cat favourite = cat;

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String json = sharedPreferences.getString("favourites", null);
        Gson gson = new Gson();

        Type favouritesJson = new TypeToken<ArrayList<Cat>>(){}.getType();
        favouritesList = gson.fromJson(json, favouritesJson);

        if (favouritesList == null) {
            favouritesList = new ArrayList<>();
        }

        favouritesList.add(favourite);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        json = gson.toJson(favouritesList);
        editor.putString("favourites", json);
        editor.apply();

        Toast.makeText(getApplicationContext(),"Added to Favourites.", Toast.LENGTH_SHORT).show();
    }

    public void getImage() {
        ImageView catImageView = findViewById(R.id.photo);
        Glide.with(this).load(catImage.getUrl()).into(catImageView);
    }
}
