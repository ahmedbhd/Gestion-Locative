package com.example.ala.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ala.myapplication.database.AppDatabase;
import com.example.ala.myapplication.database.LocataireDataSource;
import com.example.ala.myapplication.database.LocataireRepository;
import com.example.ala.myapplication.database.LocationDataSource;
import com.example.ala.myapplication.database.LocationRepository;
import com.example.ala.myapplication.entites.Locataire;
import com.example.ala.myapplication.entites.Location;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainTabView = inflater.inflate(R.layout.fragment_calendar, null);
        CompactCalendarView compactCalendarView = mainTabView.findViewById(R.id.compactcalendar_view);
        final TextView currentMonth = mainTabView.findViewById(R.id.currentMonth);
        final TextView selectedEvent = mainTabView.findViewById(R.id.locataire);
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
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        LocationRepository locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        LocataireRepository locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
        List<Location> locations = locationRepository.getAllasList();
        //TODO: events
        for(Location location : locations){
            Locataire locataire = locataireRepository.findById(location.getLocataire()).blockingFirst();
            Event event = new Event(Color.RED, location.getDateDebut().getTime(),locataire.getNom());
            compactCalendarView.addEvent(event);
        }
        //        Date d = new Date(1530276859000L);
//        Event ev1 = new Event(Color.RED, 1530276859000L, "Some extra data that I want to store.");
//        compactCalendarView.addEvent(ev1);
        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectedEvent.setText("");
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                if(!events.isEmpty()){
                    for (Event e : events) selectedEvent.setText(selectedEvent.getText()+e.getData().toString()+"\n");
                }
                else{
                    Log.e("tag", "Diponible");
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
