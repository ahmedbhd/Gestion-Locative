package com.example.ala.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.ala.myapplication.entites.Paiement;

import java.util.List;

import co.dift.ui.SwipeToAction;

public class PaiementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Paiement> items;
    private Context context;

    public class BookViewHolder extends SwipeToAction.ViewHolder<Paiement> {
        public TextView montantTextView;
        public TextView typeTextView;

        public BookViewHolder(View v) {
            super(v);

            montantTextView = v.findViewById(R.id.montantTextView);
            typeTextView = v.findViewById(R.id.typeTextView);
        }
    }

    /** Constructor **/
    public PaiementAdapter(List<Paiement> items,Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paiement_item_view, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        LocationRepository locationRepository = LocationRepository.getInstance(LocationDataSource.getInstance(appDatabase.locationDAO()));
        LocataireRepository locataireRepository = LocataireRepository.getInstance(LocataireDataSource.getInstance(appDatabase.locataireDAO()));
        Paiement item = items.get(position);
        Location location = locationRepository.findById(item.getLocationID()).blockingFirst(new Location());
        Locataire locataire = locataireRepository.findById(location.getLocataire()).blockingFirst(new Locataire());
        BookViewHolder vh = (BookViewHolder) holder;
        vh.montantTextView.setText(String.valueOf(item.getMontant()) + " DT :   "+locataire.getNom());
        vh.typeTextView.setText(String.valueOf(item.getType())+" : "+location.toString());
        vh.data = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
