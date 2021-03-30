package com.niall.electronicsstore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.CatalogueItemAdapter;
import com.niall.electronicsstore.entities.Item;

import java.util.ArrayList;

public class CatalogueFragment extends Fragment implements CatalogueItemAdapter.ViewHolder.OnItemListener{


    private static final String TAG = "CatalogueTAG";
    private RecyclerView recyclerView;
    private CatalogueItemAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRCV();

        items.add(new Item("Magic Keyboard with Numeric Keypad ",
                "Apple",
                149.99,
                "Keyboards",
                "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MRMH2B?wid=1144&hei=1144&fmt=jpeg&qlt=95&.v=1520717406876" ));

        adapter.notifyDataSetChanged();
    }


    public void setUpRCV(){

        recyclerView = getView().findViewById(R.id.catalogue_rcv);
        adapter = new CatalogueItemAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: clicked! " + items.get(position));
    }
}