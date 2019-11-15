package com.example.mycatapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

    @Dao
    public interface CatImageDao {

        @Query("SELECT * FROM cat")
        List<CatImage> getAll();

        @Query("SELECT * FROM cat WHERE id = :id LIMIT 1")
        public abstract CatImage findCatById(String id);

        @Query("SELECT url From cat WHERE id = :id LIMIT 1")
        CatImage getImageUrlById (String id);


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(List<CatImage> catImage);

        @Insert
        void insertAll(CatImage... catImages);

        @Delete
        void delete(CatImage catImage);

        @Delete
        void deleteAll(CatImage... catImages);
    }


