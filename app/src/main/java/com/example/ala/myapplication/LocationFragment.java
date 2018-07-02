package com.example.ala.myapplication;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocataireDataSource;
import com.example.ala.myapplication.database.LocataireRepository;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.entites.Locataire;
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
    private LocataireRepository locataireRepository;

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
        locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
        loadData();

        listLocations.setOnItemClickListener((adapterView, view, i, l) -> {
            Location entry= (Location) adapterView.getAdapter().getItem(i);
            Log.e("Location Selected : ",String.valueOf(entry.getLid()));
            openDialog(entry);
        });

        return mainView;
    }

    private void openDialog(Location location) {
        Locataire locataire = locataireRepository.findById(location.getLocataire()).blockingFirst();
        String phone = locataire.getTelephone();
        String nom = locataire.getNom();
        String cin = locataire.getCin();

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        TextView title = new TextView(getContext());
        title.setText("Information sur "+nom);
        title.setPadding(10, 25, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(getContext());
        // Message Properties
        msg.setText("Nom : "+nom+"\nCIN : "+cin+"\n");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Appeler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                alertDialog.dismiss();
            }
        });

        new Dialog(getContext());
        alertDialog.show();
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
