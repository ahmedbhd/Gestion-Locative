package com.example.ala.myapplication.database;

import com.example.ala.myapplication.dao.LocationDAO;
import com.example.ala.myapplication.entites.Location;

import java.util.List;

import io.reactivex.Flowable;

public class LocationDataSource implements ILocationDataSource {

    private static LocationDataSource mInstance;

    private LocationDAO locationDAO;

    public LocationDataSource (LocationDAO locationDAO){
        this.locationDAO = locationDAO;
    }

    public static LocationDataSource getInstance(LocationDAO locationDAO){
        if (mInstance == null){
            mInstance = new LocationDataSource(locationDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Location>> getAll() {
        return locationDAO.getAll();
    }

    @Override
    public List<Location> getAllasList() {
        return locationDAO.getAllasList();
    }

    @Override
    public Flowable<Location> findById(int locationid) {
        return locationDAO.findById(locationid);
    }

    @Override
    public void insert(Location... locations) {
        locationDAO.insert(locations);
    }

    @Override
    public void update(Location... locations) {
        locationDAO.update(locations);
    }

    @Override
    public void delete(Location location) {
        locationDAO.delete(location);
    }

    @Override
    public void deleteAll() {
        locationDAO.deleteAll();
    }
}
