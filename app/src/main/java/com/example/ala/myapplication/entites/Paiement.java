package com.example.ala.myapplication.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(foreignKeys = {
        @ForeignKey(entity = Location.class,
                parentColumns = "lid",
                childColumns = "locationID",
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "locationID")
        })
public class Paiement {
    @PrimaryKey(autoGenerate = true)
    private int pid;

    @ColumnInfo(name = "montant")
    private int montant;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "location_id")
    private int locationID;

    public Paiement() {
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paiement paiement = (Paiement) o;
        return pid == paiement.pid &&
                montant == paiement.montant &&
                Objects.equals(type, paiement.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, montant, type);
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "pid=" + pid +
                ", montant=" + montant +
                ", type='" + type + '\'' +
                '}';
    }
}
