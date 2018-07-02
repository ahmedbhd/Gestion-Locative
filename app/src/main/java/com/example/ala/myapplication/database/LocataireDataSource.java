package com.example.ala.myapplication.database;

import com.example.ala.myapplication.dao.LocataireDAO;
import com.example.ala.myapplication.entites.Locataire;

import java.util.List;

import io.reactivex.Flowable;

public class LocataireDataSource implements ILocataireDataSource {

    private static LocataireDataSource mInstance;

    private LocataireDAO locataireDAO;

    public LocataireDataSource(LocataireDAO locataireDAO){
        this.locataireDAO = locataireDAO;
    }

    public static LocataireDataSource getInstance(LocataireDAO locataireDAO){
        if (mInstance == null){
            mInstance = new LocataireDataSource(locataireDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Locataire>> getAll() {
        return locataireDAO.getAll();
    }

    @Override
    public Flowable<Locataire> findById(int locataireid) {
        return locataireDAO.findById(locataireid);
    }

    @Override
    public Flowable<Locataire> findByCIN(String locataireCIN) {
        return locataireDAO.findByCIN(locataireCIN);
    }

    @Override
    public void insert(Locataire... locataires) {
        locataireDAO.insert(locataires);
    }

    @Override
    public void update(Locataire... locataires) {
        locataireDAO.update(locataires);
    }

    @Override
    public void delete(Locataire locataire) {
        locataireDAO.delete(locataire);
    }

    @Override
    public void deleteAll() {
        locataireDAO.deleteAll();
    }
}
