package com.example.ala.myapplication.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.example.ala.myapplication.entites.Locataire;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LocataireDAO {

    @Query("SELECT * FROM locataire")
    @Transaction
    Flowable<List<Locataire>> getAll();

    @Query("SELECT * FROM locataire WHERE locataire_id = :locataireid")
    @Transaction
    Flowable<Locataire> findById(int locataireid);

    @Query("SELECT * FROM locataire WHERE cin = :locataireCIN")
    @Transaction
    Flowable<Locataire> findByCIN(String locataireCIN);

    @Insert
    void insert(Locataire... locations);

    @Update
    void update(Locataire... locations);

    @Delete
    void delete(Locataire location);

    @Query("DELETE FROM locataire")
    void deleteAll();

}
