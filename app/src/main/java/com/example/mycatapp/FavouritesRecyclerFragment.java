package com.example.mycatapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FavouritesRecyclerFragment extends Fragment {
    private RecyclerView favouritesRecyclerView;
    private FavouritesAdapter favouritesAdapter;
    private RecyclerView.LayoutManager favouritesLayoutManager;
    private ArrayList<Cat> favouritesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_recycler, container, false);

        favouritesList = load("favourites", favouritesList);
        view = buildRecyclerView(view);

        return view;
    }

    public void removeItem(int position) {
        favouritesList.remove(position);
        favouritesAdapter.notifyItemRemoved(position);
        delete("favourites", favouritesList);
    }

    private ArrayList<Cat> load(String key, ArrayList<Cat> list) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type menuJson = new TypeToken<ArrayList<Cat>>() {
        }.getType();
        list = gson.fromJson(json, menuJson);

        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }

    private void delete(String key, ArrayList<Cat> list) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favourites", gson.toJson(list));
        editor.apply();
    }

    public View buildRecyclerView(View view) {
        favouritesRecyclerView = view.findViewById(R.id.rv_fav);
        favouritesRecyclerView.setHasFixedSize(true);
        favouritesLayoutManager = new LinearLayoutManager(view.getContext());
        favouritesAdapter = new FavouritesAdapter(view.getContext(), favouritesList);

        favouritesRecyclerView.setLayoutManager(favouritesLayoutManager);
        favouritesRecyclerView.setAdapter(favouritesAdapter);

        favouritesAdapter.setOnItemClickListener(new FavouritesAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
        return view;
    }
}

