package com.example.ala.myapplication;

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

public class UpdateDialogFragment extends DialogFragment  {
    private static final String TAG = "AKDialogFragment";
    private int hours = 25,minutes,day,month=13,year;
    private int today,tomonth=13,toyear;
    Date toselectedDate,selectedDate;
    private DatePickerDialog dpd;
    Button todatepickerbtn;
    Button modifierLocation;
    int location_id = 0;
    int locataire_id = 0;
    Locataire locataire;
    Location location;
    EditText cin,nom,telephone;
    private LocataireRepository locataireRepository;
    private LocationRepository locationRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            getActivity().getFragmentManager().beginTransaction().add(this.getTargetFragment(), "dialog").commit();
//        else
//            getChildFragmentManager().beginTransaction().add(dialogFrag,"dialog").commit();
        View rootView = inflater.inflate(R.layout.dialog_ak, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            location_id = bundle.getInt("location_id");
            locataire_id = bundle.getInt("locataire_id");
        }

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
        locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        locataire = locataireRepository.findById(locataire_id).blockingFirst();
        location = locationRepository.findById(location_id).blockingFirst();

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        Button timepickerbtn = rootView.findViewById(R.id.timepicker);
        Button datepickerbtn = rootView.findViewById(R.id.datepicker);
        todatepickerbtn = rootView.findViewById(R.id.todatepicker);
        modifierLocation = rootView.findViewById(R.id.add_location);
        modifierLocation.setVisibility(View.GONE);
        cin = rootView.findViewById(R.id.cin);
        nom = rootView.findViewById(R.id.nom);
        telephone = rootView.findViewById(R.id.telephone);

        cin.setText(locataire.getCin());
        nom.setText(locataire.getNom());
        telephone.setText(locataire.getTelephone());
        selectedDate = location.getDateDebut();
        toselectedDate = location.getDateFin();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(location.getDateDebut());
        hours=calendar.get(Calendar.HOUR);
        minutes=calendar.get(Calendar.MINUTE);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        year=calendar.get(Calendar.YEAR);
        timepickerbtn.setText(
                new StringBuilder()
                        .append(hours)
                        .append(":").append(minutes)
                        .toString()
        );
        datepickerbtn.setText(
                new StringBuilder()
                        .append(day)
                        .append("/").append(month)
                        .append("/").append(year)
                        .toString()
        );
        calendar.setTime(location.getDateFin());
        tomonth=calendar.get(Calendar.MONTH);
        today=calendar.get(Calendar.DAY_OF_MONTH);
        toyear=calendar.get(Calendar.YEAR);
        todatepickerbtn.setText(
                new StringBuilder()
                        .append(today)
                        .append("/").append(tomonth)
                        .append("/").append(toyear)
                        .toString()
        );

        timepickerbtn.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            new android.app.TimePickerDialog(
                    getActivity(),
                    (view1, hour, minute) -> {
                        hours=hour;minutes=minute;
                        timepickerbtn.setText(new StringBuilder().append(hours).append(":").append(minutes).toString());
                        if(month!=13) {
                            selectedDate = new GregorianCalendar(year, month, day, hours, minutes).getTime();
                            toselectedDate = new GregorianCalendar(toyear, tomonth, today, hours, minutes).getTime();
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

        toolbar.setTitle("Modifier Location");

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

    public void modifierLocation(){
        locataire.setCin(cin.getText().toString());
        locataire.setNom(nom.getText().toString());
        locataire.setTelephone(telephone.getText().toString());
        locataireRepository.update(locataire);

        location.setDateDebut(new Date(selectedDate.getTime()));
        location.setDateFin(new Date (toselectedDate.getTime()));
        locationRepository.update(location);

        Toast.makeText(getContext(), "Location Modifiée !", Toast.LENGTH_LONG).show();
        dismiss();
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
            modifierLocation();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
