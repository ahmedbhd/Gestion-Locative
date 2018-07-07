package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Location;

import java.util.List;

import io.reactivex.Flowable;

public class LocationRepository implements ILocationDataSource {

    private static LocationRepository mInstance;

    private ILocationDataSource mLocalDataSource;

    public LocationRepository(ILocationDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static LocationRepository getInstance(ILocationDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance = new LocationRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Location>> getAll() {
        return mLocalDataSource.getAll();
    }

    @Override
    public List<Location> getAllasList() {
        return mLocalDataSource.getAllasList();
    }

    @Override
    public Flowable<Location> findById(int locationid) {
        return mLocalDataSource.findById(locationid);
    }

    @Override
    public void insert(Location... locations) {
        mLocalDataSource.insert(locations);
    }

    @Override
    public void update(Location... locations) {
        mLocalDataSource.update(locations);
    }

    @Override
    public void delete(Location location) {
        mLocalDataSource.delete(location);
    }

    @Override
    public void deleteAll() {
        mLocalDataSource.deleteAll();
    }
}
