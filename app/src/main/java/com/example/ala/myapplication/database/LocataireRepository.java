package com.example.ala.myapplication.database;

import com.example.ala.myapplication.entites.Locataire;

import java.util.List;

import io.reactivex.Flowable;

public class LocataireRepository implements ILocataireDataSource {

    private static LocataireRepository mInstance;

    private ILocataireDataSource mLocalDataSource;

    public LocataireRepository(ILocataireDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static LocataireRepository getInstance(ILocataireDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance = new LocataireRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Locataire>> getAll() {
        return mLocalDataSource.getAll();
    }

    @Override
    public Flowable<Locataire> findById(int locataireid) {
        return mLocalDataSource.findById(locataireid);
    }

    @Override
    public Flowable<Locataire> findByCIN(String locataireCIN) {
        return mLocalDataSource.findByCIN(locataireCIN);
    }

    @Override
    public void insert(Locataire... locataires) {
        mLocalDataSource.insert(locataires);
    }

    @Override
    public void update(Locataire... locataires) {
        mLocalDataSource.update(locataires);
    }

    @Override
    public void delete(Locataire locataire) {
        mLocalDataSource.delete(locataire);
    }

    @Override
    public void deleteAll() {
        mLocalDataSource.deleteAll();
    }
}
