package com.example.ala.myapplication.database;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.ala.myapplication.converters.DateConverter;
import com.example.ala.myapplication.dao.LocataireDAO;
import com.example.ala.myapplication.dao.LocationDAO;
import com.example.ala.myapplication.entites.Locataire;
import com.example.ala.myapplication.entites.Location;

import static com.example.ala.myapplication.database.AppDatabase.DATABASE_VERSION;

@Database(entities = {Location.class, Locataire.class}, version = DATABASE_VERSION)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "location-db";
    public static final int DATABASE_VERSION = 4;

    public abstract LocationDAO locationDAO();
    public abstract LocataireDAO locataireDAO();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context,AppDatabase.class,DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }
}

