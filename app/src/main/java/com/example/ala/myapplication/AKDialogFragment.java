package com.example.ala.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocataireDataSource;
import com.example.ala.myapplication.database.LocataireRepository;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.entites.Locataire;
import com.example.ala.myapplication.entites.Location;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.dmoral.toasty.Toasty;

public class AKDialogFragment extends DialogFragment  {
    private static final String TAG = "AKDialogFragment";
    private int hours = 25,minutes,day,month=13,year;
    private int today,tomonth=13,toyear;
    Date toselectedDate,selectedDate;
    private DatePickerDialog dpd;
    Button todatepickerbtn;
    Button ajouterLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_ak, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        Button timepickerbtn = rootView.findViewById(R.id.timepicker);
        Button datepickerbtn = rootView.findViewById(R.id.datepicker);
        todatepickerbtn = rootView.findViewById(R.id.todatepicker);
        ajouterLocation = rootView.findViewById(R.id.add_location);
        ajouterLocation.setVisibility(View.INVISIBLE); //
        EditText cin = rootView.findViewById(R.id.cin);
        cin.setError("CIN Invalide");
        EditText nom = rootView.findViewById(R.id.nom);
        nom.setError("Nom Invalide");
        EditText telephone = rootView.findViewById(R.id.telephone);
        cin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (cin.getText().toString().trim().length() < 8) {
                    cin.setError("CIN Invalide");
                } else {
                    cin.setError(null);
                }
            } else {
                if (cin.getText().toString().trim().length() < 8) {
                    cin.setError("CIN Invalide");
                } else {
                    // your code here
                    cin.setError(null);
                }
            }

        });

        nom.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (nom.getText().toString().trim().length() < 3) {
                    nom.setError("Nom Invalide");
                } else {
                    nom.setError(null);
                }
            } else {
                if (nom.getText().toString().trim().length() < 3) {
                    nom.setError("Nom Invalide");
                } else {
                    // your code here
                    nom.setError(null);
                }
            }

        });

        timepickerbtn.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            new android.app.TimePickerDialog(
                    getActivity(),
                    (view1, hour, minute) -> {
                        hours=hour;minutes=minute;
                        timepickerbtn.setText(new StringBuilder().append(hours).append(":").append(minutes).toString());
                        if(month!=13) {
                            Log.e("Date : ", "" + day + "/" + month + "/" + year + " " + hours + ":" + minutes);
                            selectedDate = new GregorianCalendar(year, month, day, hours, minutes).getTime();
                        }
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true

            ).show();
        });
        datepickerbtn.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(
                    getActivity(),
                    (view12, year, month, dayOfMonth) -> {
                        this.year=year;this.month=month;day=dayOfMonth;
                        datepickerbtn.setText(new StringBuilder().append(dayOfMonth).append("/").append(month+1).append("/").append(year).toString());
                        if(hours!=25) {
                            selectedDate = new GregorianCalendar(year, month, day, hours, minutes).getTime();
                            Log.e("Date : ", selectedDate.toString());
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        todatepickerbtn.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(
                    getActivity(),
                    (view12, year, month, dayOfMonth) -> {
                        toyear=year;tomonth=month;today=dayOfMonth;
                        todatepickerbtn.setText(new StringBuilder().append(dayOfMonth).append("/").append(month+1).append("/").append(year).toString());
                        toselectedDate = new GregorianCalendar(toyear, tomonth, today, hours, minutes).getTime();
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        ajouterLocation.setOnClickListener(view -> {
            if(cin.getError()!=null||nom.getError()!=null||telephone.getError()!=null)
                return ;
            if(selectedDate == null || toselectedDate == null){
                Toasty.error(getContext(),"Veuillez vérifié la date.",Toast.LENGTH_LONG,true).show();
                return ;
            }
            Location location = new Location();
            location.setDateDebut(new Date(selectedDate.getTime()));
            location.setDateFin(new Date (toselectedDate.getTime()));
            Locataire locataire = new Locataire();
            locataire.setCin(cin.getText().toString());
            locataire.setNom(nom.getText().toString());
            locataire.setTelephone(telephone.getText().toString());
            // persist locataire
            AppDatabase appDatabase = AppDatabase.getInstance(getContext());
            LocataireRepository locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
            locataireRepository.insert(locataire);
            locataire = locataireRepository.findByCIN(cin.getText().toString()).blockingFirst();
            //persist location
            location.setLocataire(locataire.getLocataire_id());
            LocationRepository locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
            locationRepository.insert(location);

            //Hide Keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            // TOAST
            Toasty.warning(getContext(), "Location Crée!", Toast.LENGTH_SHORT, true).show();
            dismiss();
        });

        toolbar.setTitle("Ajouter Location");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_ak, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here
            ajouterLocation.performClick();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
