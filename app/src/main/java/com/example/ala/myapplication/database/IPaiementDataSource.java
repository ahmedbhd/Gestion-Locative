package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Paiement;

import java.util.List;

import io.reactivex.Flowable;

public interface IPaiementDataSource {
    Flowable<List<Paiement>> getAll();

    Flowable<Paiement> findById(int paiementid);

    Flowable<Paiement> findBylocationID(int locationid);

    void insert(Paiement... paiements);

    void update(Paiement... paiements);

    void delete(Paiement paiement);

    void deleteAll();

}
