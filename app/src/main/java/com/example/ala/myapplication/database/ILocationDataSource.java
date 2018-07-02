package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Location;

import java.util.List;

import io.reactivex.Flowable;

public interface ILocationDataSource {
    Flowable<List<Location>> getAll();
    Flowable<Location> findById(int locationid);
    void insert(Location... locations);
    void update(Location... locations);
    void delete(Location location);
    void deleteAll();
}
