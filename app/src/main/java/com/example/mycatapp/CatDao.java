package com.example.mycatapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CatDao {

    @Query("SELECT * FROM cat")
    List<Cat> getAll();

    @Query("SELECT * FROM cat WHERE name = :name LIMIT 1")
    Cat findCatByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Cat> cat);

    @Insert
    void insertAll(Cat... cats);

    @Delete
    void delete(Cat cat);

    @Delete
    void deleteAll(Cat... cat);
}
