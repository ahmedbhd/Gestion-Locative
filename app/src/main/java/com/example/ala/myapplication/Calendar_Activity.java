package com.example.ala.myapplication;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ala.myapplication.database.AppDatabase;

public class Calendar_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_);
        loadFragment(new CalendarFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).allowMainThreadQueries().build();
//        db.locationDAO().getAll();
//        db.locationDAO().insert(new Location(new Date(),new Date(),"ala"));

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_calendar:
                fragment = new CalendarFragment();
                break;

            case R.id.navigation_location:
                fragment = new LocationFragment();
                break;

            case R.id.navigation_paiement:
                fragment = new PaiementFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
