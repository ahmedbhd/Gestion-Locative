package com.example.ala.myapplication;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.PaiementDataSource;
import com.example.ala.myapplication.database.PaiementRepository;

public class Calendar_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_);
        loadFragment(new CalendarFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).allowMainThreadQueries().build();
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        PaiementRepository paiementRepository = PaiementRepository
                .getInstance(PaiementDataSource.getInstance(appDatabase.paiementDAO()));
//        Paiement p = new Paiement();
//        p.setMontant(500);
//        p.setType("Avance");
//        paiementRepository.insert(p);
//        Log.e("Calendar Act ", "Paiement Crée");
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
