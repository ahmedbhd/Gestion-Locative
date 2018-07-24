package com.example.ala.myapplication.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ala.myapplication.entites.Paiement;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PaiementDAO {

    @Query("SELECT * FROM paiement")
    Flowable<List<Paiement>> getAll();

    @Query("SELECT * FROM paiement WHERE pid = :paiementid")
    Flowable<Paiement> findById(int paiementid);

    @Query("SELECT * FROM paiement WHERE locationID = :locationid")
    Flowable<Paiement> findBylocationID(int locationid);

    @Insert
    void insert(Paiement... paiements);

    @Update
    void update(Paiement... paiements);

    @Delete
    void delete(Paiement paiement);

    @Query("DELETE FROM paiement")
    void deleteAll();

}
