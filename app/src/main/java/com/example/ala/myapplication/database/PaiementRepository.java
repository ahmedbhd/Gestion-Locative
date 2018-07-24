package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Paiement;

import java.util.List;

import io.reactivex.Flowable;

public class PaiementRepository implements IPaiementDataSource {

    private static PaiementRepository mInstance;

    private IPaiementDataSource mLocalDataSource;

    public PaiementRepository(IPaiementDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static PaiementRepository getInstance(IPaiementDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance = new PaiementRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Paiement>> getAll() {
        return mLocalDataSource.getAll();
    }

    @Override
    public Flowable<Paiement> findById(int paiementid) {
        return mLocalDataSource.findById(paiementid);
    }

    @Override
    public Flowable<Paiement> findBylocationID(int locationid) {
        return mLocalDataSource.findById(locationid);
    }

    @Override
    public void insert(Paiement... paiements) {
        mLocalDataSource.insert(paiements);
    }

    @Override
    public void update(Paiement... paiements) {
        mLocalDataSource.update(paiements);
    }

    @Override
    public void delete(Paiement paiement) {
        mLocalDataSource.delete(paiement);
    }

    @Override
    public void deleteAll() {
        mLocalDataSource.deleteAll();
    }
}
