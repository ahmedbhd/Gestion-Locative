package com.example.ala.myapplication.entites;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "locataire")
public class Locataire {
    @PrimaryKey(autoGenerate = true)
    private int locataire_id;
    private String nom;
    private String telephone;
    private String cin;

    public Locataire (){
    }
    @Ignore
    public Locataire(String nom, String telephone, String cin) {
        this.nom = nom;
        this.telephone = telephone;
        this.cin = cin;
    }

    public int getLocataire_id() {
        return locataire_id;
    }

    public void setLocataire_id(int locataire_id) {
        this.locataire_id = locataire_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locataire locataire = (Locataire) o;
        return locataire_id == locataire.locataire_id &&
                Objects.equals(nom, locataire.nom) &&
                Objects.equals(telephone, locataire.telephone) &&
                Objects.equals(cin, locataire.cin) ;}

    @Override
    public int hashCode() {

        return Objects.hash(locataire_id, nom, telephone, cin);
    }

    @Override
    public String toString() {
        return "Locataire{" +
                "locataire_id=" + locataire_id +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", cin='" + cin + '\'' +
                '}';
    }
}
