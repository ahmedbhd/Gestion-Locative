package com.example.ala.myapplication;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocataireDataSource;
import com.example.ala.myapplication.database.LocataireRepository;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.database.PaiementDataSource;
import com.example.ala.myapplication.database.PaiementRepository;
import com.example.ala.myapplication.entites.Locataire;
import com.example.ala.myapplication.entites.Location;
import com.example.ala.myapplication.entites.Paiement;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    ListView listLocations;

    FloatingActionButton ajouterLocation;

    List<Location> locations = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;

    private LocationRepository locationRepository;
    private LocataireRepository locataireRepository;
    private PaiementRepository paiementRepository;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_location, container, false);
        compositeDisposable = new CompositeDisposable();
        listLocations = mainView.findViewById(R.id.listLocations);
        ajouterLocation = mainView.findViewById(R.id.addLocation);

        ajouterLocation.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AKDialogFragment newFragment = new AKDialogFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });

        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, locations);
        registerForContextMenu(listLocations);
        listLocations.setAdapter(adapter);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
        paiementRepository = PaiementRepository.getInstance(PaiementDataSource.getInstance(appDatabase.paiementDAO()));
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

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Set Message
        TextView msg = new TextView(getContext());
        // Message Properties
        msg.setTypeface(null,1);
        msg.setText("Nom : "+nom+"\nCIN : "+cin+"\n");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);

        linearLayout.addView(msg);

        alertDialog.setView(linearLayout);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Modifier", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
//                TODO: Dialog de mofication
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                UpdateDialogFragment newFragment = new UpdateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("location_id", location.getLid());
                bundle.putInt("locataire_id", location.getLocataire());
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Appeler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL," ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
//                locationRepository.delete(location);
                // Paiement Dialog
                AlertDialog paiementDialog = new AlertDialog.Builder(getContext()).create();
                TextView title = new TextView(getContext());
                title.setText("Ajouter Paiement");
                title.setPadding(10, 25, 10, 10);   // Set Position
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.BLACK);
                title.setTextSize(20);
                paiementDialog.setCustomTitle(title);
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                EditText montantEditText=new EditText(getContext());
                montantEditText.setHint("Montant");
                montantEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayout.addView(montantEditText);

                ArrayList<String> spinnerArray = new ArrayList<>();
                spinnerArray.add("Avance");
                spinnerArray.add("Paiement");

                Spinner spinner = new Spinner(getContext());
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                linearLayout.addView(spinner);
                paiementDialog.setView(linearLayout);

                paiementDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Payer", (dialogInterface, i) -> {
                    paiementDialog.dismiss();
                    Paiement paiement = new Paiement();
                    paiement.setMontant(Integer.valueOf(montantEditText.getText().toString()));
                    paiement.setType(spinner.getSelectedItem().toString());
                    paiement.setLocationID(location.getLid());
                    paiementRepository.insert(paiement);
//                    Toast.makeText(getContext(), "Paiement Ajouté", Toast.LENGTH_SHORT).show();
                    Toasty.success(getContext(), "Paiement Ajouté!", Toast.LENGTH_SHORT, true).show();
                    paiementDialog.dismiss();
                });
                paiementDialog.show();
            }
        });


        new Dialog(getContext());
        alertDialog.setOnShowListener( (arg0) ->{
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_local_phone_black_24dp,0,0,0);
//            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_forever_black_24dp,0,0,0);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_money_black_24dp,0,0,0);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorGreen));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorDelete));
            });
        alertDialog.show();
        Button app_btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button p_btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        Button up_btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        int height = getResources().getDimensionPixelSize(R.dimen.alertdialog_button_height);
        int width  = getResources().getDimensionPixelSize(R.dimen.alertdialog_button_width);
        int width_up  = getResources().getDimensionPixelSize(R.dimen.alertdialog_button_width_modifier);
        int width_p  = getResources().getDimensionPixelSize(R.dimen.alertdialog_button_width_p);

        app_btn.setHeight(height);
        up_btn.setHeight(height);
        p_btn.setHeight(height);
        app_btn.setWidth(width);
        up_btn.setWidth(width_up);
        p_btn.setWidth(width_p);
    }

    private void loadData() {
        //RxJAVA

        Disposable disposable = locationRepository.getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onGetAllLocationSuccess,
                        throwable -> {
                            Toasty.error(getContext(), ""+throwable.getMessage(), Toast.LENGTH_LONG, true).show();
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
