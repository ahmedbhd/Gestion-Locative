package com.example.ala.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocataireDataSource;
import com.example.ala.myapplication.database.LocataireRepository;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.entites.Locataire;
import com.example.ala.myapplication.entites.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    FloatingActionButton fab;

    ListView listviewlocation;

    List<Location> locations = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;

    private LocationRepository locationRepository;
    private LocataireRepository locataireRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        compositeDisposable = new CompositeDisposable();
        listviewlocation = findViewById(R.id.listviewlocation);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locations);
        registerForContextMenu(listviewlocation);
        listviewlocation.setAdapter(adapter);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));

        listviewlocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location entry= (Location) adapterView.getAdapter().getItem(i);
                Log.e("ENTRY : ",entry.toString());
                Toast.makeText(TestActivity.this, entry.toString(), Toast.LENGTH_LONG).show();
                Locataire locataire = locataireRepository.findById(entry.getLocataire()).blockingFirst();
                String phone = locataire.getTelephone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        loadData();
        fab.setOnClickListener(view -> {
            Disposable disposable = Observable.create(emitter -> {
                Locataire locataire = new Locataire(UUID.randomUUID().toString().substring(0,5),"22366303",UUID.randomUUID().toString().substring(0,5));
                locataireRepository.insert(locataire);
                locataire = locataireRepository.findByCIN(locataire.getCin()).blockingFirst();
                Log.e("Locataire : ",locataire.toString());
                Location location = new Location(new Date(),new Date(),locataire.getLocataire_id());
                locationRepository.insert(location);
                emitter.onComplete();
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(o ->{
                        Toast.makeText(TestActivity.this, "Location Ajouté!", Toast.LENGTH_LONG).show();
                    },
                            throwable -> {
                                Log.e("throwable_insert : ", throwable.toString());
                                Toast.makeText(TestActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            , () -> loadData());
    });
    }

    private void loadData() {
        //RxJAVA

        Disposable disposable = locationRepository.getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(locations ->
                                onGetAllLocationSuccess(locations),
                        throwable -> {
                            Log.e("throwable : ", throwable.toString());
                            Toast.makeText(TestActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllLocationSuccess(List<Location> loc) {
        locations.clear();
        locations.addAll(loc);
        adapter.notifyDataSetChanged();
    }

}
