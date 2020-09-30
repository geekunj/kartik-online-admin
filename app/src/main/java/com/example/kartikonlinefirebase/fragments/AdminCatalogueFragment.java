package com.example.kartikonlinefirebase.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.activities.CatalogueItemInfo;
import com.example.kartikonlinefirebase.activities.CatalogueMain;
import com.example.kartikonlinefirebase.activities.NewCatalogueActivity;
import com.example.kartikonlinefirebase.adapters.CatalogueAdapter;
import com.example.kartikonlinefirebase.models.Catalogue;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static com.example.kartikonlinefirebase.utils.Config.mStaticCatalogue;
import static com.example.kartikonlinefirebase.utils.Config.mStaticProduct;


public class AdminCatalogueFragment extends Fragment implements CatalogueAdapter.OnItemClickListener {

    private FloatingActionButton fab;
    private RecyclerView catalogueRecycler;
    private Query mQuery;
    private FirebaseFirestore mFirestore;
    private CatalogueAdapter adapter;


    public AdminCatalogueFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_catalogue, container, false);

        fab = view.findViewById(R.id.fab);
        catalogueRecycler = view.findViewById(R.id.rv_catalogue_list);

        mFirestore = FirebaseFirestore.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStaticProduct = new Product();
                mStaticCatalogue = new Catalogue();
                Config.mStaticProductList = new ArrayList<>();
                Config.mCatalogueList = new ArrayList<>();
                Intent intent = new Intent(getActivity(), NewCatalogueActivity.class);
                startActivity(intent);
            }
        });

        mQuery = mFirestore.collection("catalogues").orderBy("catalogueTitle").limit(50);

        adapter = new CatalogueAdapter(mQuery,this, getContext()){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    catalogueRecycler.setVisibility(View.GONE);
                    //mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    catalogueRecycler.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        catalogueRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        catalogueRecycler.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adapter.deleteCatalogue(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(catalogueRecycler);


        return view;
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        Catalogue catalogue = documentSnapshot.toObject(Catalogue.class);
        Intent intent = new Intent(getActivity(), CatalogueMain.class);
        intent.putExtra("catalogueId", documentSnapshot.getId());
        intent.putExtra("catalogueName", catalogue.getCatalogueTitle());
        intent.putExtra("catalogueVisitors", catalogue.getVisitors());
        intent.putExtra("catalogueType", catalogue.getCatalogueType());
        intent.putExtra("catalogoueImageUrl", catalogue.getCatalogueImageUrl());
        startActivity(intent);
    }
}
