package com.example.ala.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import co.dift.ui.SwipeToAction;
import es.dmoral.toasty.Toasty;
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

    RecyclerView recyclerView;
    PaiementAdapter paiementAdapter;
    SwipeToAction swipeToAction;

    private static String TAG = "Paiement Fragment ";

//    private AppAdapter mAdapter;

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

        //TODO : TEST List Swipe
        View mainView1 = inflater.inflate(R.layout.fragment_paiement_swipelist, container, false);
        recyclerView = mainView1.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        paiementAdapter = new PaiementAdapter(paiements,getContext());
        recyclerView.setAdapter(paiementAdapter);

        //populate
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        paiementRepository = PaiementRepository.getInstance(PaiementDataSource.getInstance(appDatabase.paiementDAO()));
        loadData();

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Paiement>() {
            @Override
            public boolean swipeLeft(final Paiement itemData) {
                paiementRepository.delete(itemData);
                Toasty.normal(getActivity(), "Paiement Supprimé!", getResources().getDrawable(R.drawable.ic_delete_forever_black_24dp)).show();
                return true;
            }

            @Override
            public boolean swipeRight(Paiement itemData) {
                return true;
            }

            @Override
            public void onClick(Paiement itemData) {
            }

            @Override
            public void onLongClick(Paiement itemData) {
            }
        });

        //End Test

        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, paiements);
        registerForContextMenu(listPaiements);
        listPaiements.setAdapter(adapter);


        return mainView1;
//        return mainView;
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
        Log.e(TAG, listpaiements.toString());
        paiements.clear();
        paiements.addAll(listpaiements);
        paiementAdapter.notifyDataSetChanged();
//        adapter.notifyDataSetChanged();
    }

}
