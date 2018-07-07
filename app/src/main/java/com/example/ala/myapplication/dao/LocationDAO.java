package com.example.ala.myapplication.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ala.myapplication.entites.Location;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LocationDAO {

    @Query("SELECT * FROM location")
    Flowable<List<Location>> getAll();

    @Query("SELECT * FROM location")
    List<Location> getAllasList();

    @Query("SELECT * FROM location WHERE lid = :locationid")
    Flowable<Location> findById(int locationid);

    @Insert
    void insert(Location... locations);

    @Update
    void update(Location... locations);

    @Delete
    void delete(Location location);

    @Query("DELETE FROM location")
    void deleteAll();

}
