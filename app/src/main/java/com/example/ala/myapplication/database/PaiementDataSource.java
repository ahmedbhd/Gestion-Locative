package com.example.ala.myapplication.database;

import com.example.ala.myapplication.dao.PaiementDAO;
import com.example.ala.myapplication.entites.Paiement;

import java.util.List;

import io.reactivex.Flowable;

public class PaiementDataSource implements IPaiementDataSource {

    private static PaiementDataSource mInstance;

    private PaiementDAO paiementDAO;

    public PaiementDataSource(PaiementDAO paiementDAO){
        this.paiementDAO = paiementDAO;
    }

    public static PaiementDataSource getInstance(PaiementDAO paiementDAO){
        if (mInstance == null){
            mInstance = new PaiementDataSource(paiementDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Paiement>> getAll() {
        return paiementDAO.getAll();
    }

    @Override
    public Flowable<Paiement> findById(int paiementid) {
        return paiementDAO.findById(paiementid);
    }

    @Override
    public Flowable<Paiement> findBylocationID(int locationid) {
        return paiementDAO.findBylocationID(locationid);
    }

    @Override
    public void insert(Paiement... paiements) {
        paiementDAO.insert(paiements);
    }

    @Override
    public void update(Paiement... paiements) {
        paiementDAO.update(paiements);
    }

    @Override
    public void delete(Paiement paiement) {
        paiementDAO.delete(paiement);
    }

    @Override
    public void deleteAll() {
        paiementDAO.deleteAll();
    }
}
