package com.example.ala.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
//    Button ajouterLocation;
//    Button ajouterPaiement;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainTabView = inflater.inflate(R.layout.fragment_calendar, null);
        final CompactCalendarView compactCalendarView = (CompactCalendarView) mainTabView.findViewById(R.id.compactcalendar_view);
        final TextView currentMonth = (TextView) mainTabView.findViewById(R.id.currentMonth);
        final TextView selectedEvent = (TextView) mainTabView.findViewById(R.id.locataire);
//        ajouterLocation = mainTabView.findViewById(R.id.add_location);
//        ajouterPaiement = mainTabView.findViewById(R.id.add_paiement);
        Button ajouterLocation = mainTabView.findViewById(R.id.add_location);
        ajouterLocation.setOnClickListener((view) -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AKDialogFragment newFragment = new AKDialogFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //int month = cal.get(Calendar.MONTH);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("fr"));
        String mois = Character.toUpperCase(month.charAt(0)) + month.substring(1);
        int year = cal.get(Calendar.YEAR);
        String thisMonthsDate = mois + " " + year;
        currentMonth.setText(thisMonthsDate);

        compactCalendarView.setUseThreeLetterAbbreviation(false);
        Date d = new Date(1530276859000L);
        Log.e("Date : ",d.toString());
        Event ev1 = new Event(Color.RED, 1530276859000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);
        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                if(!events.isEmpty()){
                    selectedEvent.setText(events.get(0).getData().toString());
                }
                else{
                    Log.e("tag", "Empty Day");
                    selectedEvent.setText("");
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("TAG", "Month was scrolled to: " + firstDayOfNewMonth);
                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("fr"));
                String mois = Character.toUpperCase(month.charAt(0)) + month.substring(1);
                int year = cal.get(Calendar.YEAR);
                String date = mois + " " + year;
                currentMonth.setText(date);
            }
        });
        return mainTabView;
    }
}
