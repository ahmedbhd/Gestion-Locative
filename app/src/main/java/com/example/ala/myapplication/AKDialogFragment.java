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
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AKDialogFragment extends DialogFragment {
    private static final String TAG = "AKDialogFragment";
    private int hours = 25,minutes,day,month=13,year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            getActivity().getFragmentManager().beginTransaction().add(this.getTargetFragment(), "dialog").commit();
//        else
//            getChildFragmentManager().beginTransaction().add(dialogFrag,"dialog").commit();
        View rootView = inflater.inflate(R.layout.dialog_ak, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        Button timepickerbtn = rootView.findViewById(R.id.timepicker);
        Button datepickerbtn = rootView.findViewById(R.id.datepicker);

        timepickerbtn.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            new android.app.TimePickerDialog(
                    getActivity(),
                    (view1, hour, minute) -> {
                        hours=hour;minutes=minute;
                        timepickerbtn.setText(new StringBuilder().append(hours).append(":").append(minutes).toString());
                        if(month!=13)
                                Log.e("Date : ",""+day+"/"+month+"/"+year+" "+hours+":"+minutes);
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
                        this.year=year;this.month=month;this.day=dayOfMonth;
                        datepickerbtn.setText(new StringBuilder().append(dayOfMonth).append("/").append(month+1).append("/").append(year).toString());
                        if(hours!=25) {
                            Date selectedDate = new GregorianCalendar(this.year, this.month, this.day, this.hours, this.minutes).getTime();
                            Log.e("Date : ", selectedDate.toString());
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
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
            Toast.makeText(getContext(), "Save Clicked!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            Toast.makeText(getContext(), "Closed !", Toast.LENGTH_SHORT).show();
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
