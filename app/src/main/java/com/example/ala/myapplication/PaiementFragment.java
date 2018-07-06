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
import com.example.ala.myapplication.database.PaiementDataSource;
import com.example.ala.myapplication.database.PaiementRepository;
import com.example.ala.myapplication.entites.Paiement;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaiementFragment extends Fragment {

    ListView listPaiements;
    List<Paiement> paiements = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private PaiementRepository paiementRepository;


    public PaiementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_paiement, container, false);
        compositeDisposable = new CompositeDisposable();
        listPaiements = mainView.findViewById(R.id.listPaiements);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, paiements);
        registerForContextMenu(listPaiements);
        listPaiements.setAdapter(adapter);
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        paiementRepository = PaiementRepository.getInstance(PaiementDataSource.getInstance(appDatabase.paiementDAO()));
        loadData();
        return mainView;
    }

    private void loadData() {
        Disposable disposable = paiementRepository.getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onGetAllPaiementSuccess,
                        throwable -> {
                            Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllPaiementSuccess(List<Paiement> listpaiements) {
        paiements.clear();
        paiements.addAll(listpaiements);
        adapter.notifyDataSetChanged();
    }

}
