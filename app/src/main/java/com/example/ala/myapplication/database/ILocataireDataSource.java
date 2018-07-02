package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Locataire;

import java.util.List;

import io.reactivex.Flowable;

public interface ILocataireDataSource {
    Flowable<List<Locataire>> getAll();
    Flowable<Locataire> findById(int Locataireid);
    Flowable<Locataire> findByCIN(String locataireCIN);
    void insert(Locataire... locations);
    void update(Locataire... locations);
    void delete(Locataire location);
    void deleteAll();
}
