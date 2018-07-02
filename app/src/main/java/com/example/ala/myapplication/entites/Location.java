package com.example.ala.myapplication.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "location",foreignKeys =
@ForeignKey(entity=Locataire.class,parentColumns = "locataire_id",childColumns = "locataire"))
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int lid;

    @ColumnInfo(name = "date_debut")
    private Date dateDebut;

    @ColumnInfo(name = "date_fin")
    private Date dateFin;
    private int locataire;

    public Location() {
    }
    @Ignore
    public Location(Date dateDebut, Date dateFin, int locataire) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.locataire = locataire;
    }
    @Ignore
    public Location(Date dateDebut, Date dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getLocataire() {
        return locataire;
    }

    public void setLocataire(int locataire) {
        this.locataire = locataire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return lid == location.lid &&
                Objects.equals(dateDebut, location.dateDebut) &&
                Objects.equals(dateFin, location.dateFin) &&
                locataire == location.locataire;
    }

    @Override
    public int hashCode() {

        return Objects.hash(lid, dateDebut, dateFin, locataire);
    }

    @Override
    public String toString() {
        return "Location{" +
                "lid=" + lid +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", locataire='" + locataire + '\'' +
                '}';
    }
}
