package com.example.mycatapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CatRecyclerFragment extends Fragment {
    private RecyclerView recyclerView;
    private CatDatabase db;

    public CatRecyclerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cat_recycler, container, false);
        recyclerView = view.findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        final CatAdapter catAdapter = new CatAdapter();

        final RequestQueue requestQueue =  Volley.newRequestQueue(getActivity());

        final RequestQueue imageRequestQueue = Volley.newRequestQueue(getActivity());

        final String url = "https://api.thecatapi.com/v1/breeds?api_key=fc65e727-8864-42ea-878a-0eac5f480ab6";
        final String searchUrl = "https://api.thecatapi.com/v1/images/search?api_key=fc65e727-8864-42ea-878a-0eac5f480ab6";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type collectionType = new TypeToken<List<Cat>>(){}.getType();
                List<Cat> catList =  new Gson().fromJson(response, collectionType);

                db = CatDatabase.getInstance(getContext());
                db.catDao().insert(catList);

                catAdapter.CatAdapter(db.catDao().getAll(), getContext());
                recyclerView.setAdapter(catAdapter);

                requestQueue.stop();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
            }
        };



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                errorListener);

        requestQueue.add(stringRequest);

        return view;


    }

}
