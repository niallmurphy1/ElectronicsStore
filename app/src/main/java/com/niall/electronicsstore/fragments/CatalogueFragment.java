package com.niall.electronicsstore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.activities.RegLogActivity;
import com.niall.electronicsstore.activities.RegisterActivity;
import com.niall.electronicsstore.adapters.CatalogueItemAdapter;
import com.niall.electronicsstore.entities.Item;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogueFragment extends Fragment implements CatalogueItemAdapter.ViewHolder.OnItemListener {


    private static final String TAG = "CatalogueTAG";
    private RecyclerView recyclerView;
    private CatalogueItemAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();

    private EditText searchBarEdit;


    public FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_items_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sort_by_name_AZ:
                Collections.sort(items, Item.itemComparatorAZName);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Sort A-Z", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.sort_by_name_ZA:
                Collections.sort(items, Item.itemComparatorZAName);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Sort Z-A", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sort_by_price_high_low:
                Collections.sort(items, Item.itemComparatorPriceHighLow);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Sort Price: Low-High", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.sort_by_price_low_high:
                Collections.sort(items, Item.itemComparatorPriceLowHigh);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Sort Price: High->Low", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sort_by_manufacturer:
                Collections.sort(items, Item.itemComparatorManufacturer);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Sort Price: Manufacturer", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.log_out:
                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(getContext(), RegLogActivity.class));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBarEdit = view.findViewById(R.id.serachbar_catalogue_edit);

        fillRCV();

        setUpRCV();



        adapter.addItems(items);
        adapter.notifyDataSetChanged();


        setUpFilter();


    }


    public void fillRCV() {

        items.clear();
        items.add(new Item("Magic Keyboard with Numeric Keypad ",
                "Apple",
                14999,
                "Keyboards",
                "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MRMH2B?wid=1144&hei=1144&fmt=jpeg&qlt=95&.v=1520717406876"));

        items.add(new Item("UE MEGABOOM",
                "Ultimate Ears",
                11999,
                "Speakers",
                "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/HGPQ2?wid=1144&hei=1144&fmt=jpeg&qlt=95&op_usm=0.5,0.5&.v=1509146406671"));

        items.add(new Item("Apple AirPods with charging case",
                "Apple",
                14500,
                "Headphones",
                "https://brain-images-ssl.cdn.dixons.com/7/5/10191857/u_10191857.jpg"));

        items.add(new Item("Lypertek Tevi Wireless Headphones",
                "Lypertek",
                11900,
                "Headphones",
                "https://fdn.gsmarena.com/imgroot/news/20/12/lypertek-tevi/-1200w5/gsmarena_001.jpg"));

        items.add(new Item("Garmin Venu SQ GPS Smartwatch",
                "Garmin",
                11999,
                "Wearables",
                "https://euronics.ie/uploaded/thumbnails/db_file_img_17104_600x800.jpg"));

        items.add(new Item("UE FITS",
                "Ultimate Ears",
                20999,
                "Headphones",
                "https://cdn.shopify.com/s/files/1/0058/1576/3001/products/ohboy072020_basic_cloud_02051_1080x.jpg?v=1614382043"));


        items.add(new Item("Google Pixel 5",
                "Google",
                62900,
                "Smartphones",
                "https://cdn.dxomark.com/wp-content/uploads/medias/post-59199/google_pixel_5_frontback.jpeg"));


    }


    public void setUpRCV() {

        recyclerView = getView().findViewById(R.id.catalogue_rcv);
        adapter = new CatalogueItemAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter.addItems(items);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(Item item) {

        Log.d(TAG, "onItemClick: You clicked " + item.toString());
    }

   public void setUpFilter(){


        searchBarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                adapter.getFilter().filter(s.toString());
            }
        });
    }



}