package com.example.ala.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.entites.Location;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    ListView listLocations;

    List<Location> locations = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;

    private LocationRepository locationRepository;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_location, container, false);
        compositeDisposable = new CompositeDisposable();
        listLocations = mainView.findViewById(R.id.listLocations);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, locations);
        registerForContextMenu(listLocations);
        listLocations.setAdapter(adapter);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        loadData();
        // Inflate the layout for this fragment
        return mainView;
    }

    private void loadData() {
        //RxJAVA

        Disposable disposable = locationRepository.getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(locations ->
                        onGetAllLocationSuccess(locations),
                        throwable -> {
                            Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllLocationSuccess(List<Location> listloc) {
        locations.clear();
        locations.addAll(listloc);
        adapter.notifyDataSetChanged();
    }

}
