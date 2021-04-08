package com.niall.electronicsstore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.activities.RegLogActivity;
import com.niall.electronicsstore.activities.RegisterActivity;
import com.niall.electronicsstore.adapters.CatalogueItemAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.interpreter.Expression;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class CatalogueFragment extends Fragment implements CatalogueItemAdapter.ViewHolder.OnItemListener {


    private static final String TAG = "CatalogueTAG";
    private RecyclerView recyclerView;
    private CatalogueItemAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();


    //bottom sheet components
    private LinearLayout headerLayout;
    private ConstraintLayout bottomSheetConstraint;
    private ImageView headerArrowImage;
    private BottomSheetBehavior bottomSheetBehavior;
    private Button addToCartButton;
    private Button changeCurrencyBtn;
    private ImageView itemImage;
    private TextView titleText;
    private TextView descriptionText;
    private TextView manufacturerText;
    private TextView categoryText;
    private TextView priceText;

    private Class tempClass = Class.forName("Euro");

    private EditText searchBarEdit;


    public FirebaseAuth firebaseAuth;

    public CatalogueFragment() throws ClassNotFoundException {
    }


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

        addToCartButton = view.findViewById(R.id.add_to_cart_button);
        changeCurrencyBtn = view.findViewById(R.id.bap_change_currency_button);

        itemImage = view.findViewById(R.id.bap_sheet_item_image);
        titleText = view.findViewById(R.id.bap_sheet_title_text);
        descriptionText = view.findViewById(R.id.bap_sheet_description_text);
        categoryText = view.findViewById(R.id.bap_sheet_category_text);
        manufacturerText = view.findViewById(R.id.bap_sheet_manufacturer_text);
        priceText = view.findViewById(R.id.bap_sheet_price_text);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: add items to firebase
                addToUserCartFirebase();
            }
        });


        //set up bottom sheet
        bottomSheetConstraint = view.findViewById(R.id.bottom_sheet_item_view);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetConstraint);
        headerLayout = view.findViewById(R.id.header_layout_item_view);
        headerArrowImage = view.findViewById(R.id.bap_sheet_arrow);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                headerArrowImage.setRotation(180 * slideOffset);
            }
        });


        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

    }

    private void convertCurrency(Item item) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, java.lang.InstantiationException {


        //Class tempClass = Class.forName("Euro");

        Constructor con = tempClass.getConstructor();

        Object converFrom = (Expression) con.newInstance();

        Class[] methodParams = new Class[]{Double.TYPE};

        Method conversionMethod = tempClass.getMethod("Pound", methodParams);

        Object[] params = new Object[]{(double) item.getPriceCents()};

        String toQuantity = (String) conversionMethod.invoke(converFrom, params);

        String conversionResult = toQuantity;

        double priceCents = Double.valueOf(conversionResult);

        double priceWhole = (priceCents / 100.00);

        String newPrice = formatPriceEuro(priceWhole);

        priceText.setText(newPrice + " pounds");





    }


    @Override
    public void onItemClick(Item item) {

        Log.d(TAG, "onItemClick: You clicked " + item.toString());

        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }


        Picasso.get()
                .load(item.getImage())
                .fit()
                .centerCrop()
                .into(itemImage);

        titleText.setText(item.getName());

        categoryText.setText(item.getCategory());

        manufacturerText.setText(item.getManufacturer());

        double price = (item.getPriceCents() / 100.00);

        priceText.setText(formatPriceEuro(price));

        //TODO: description for this

//        if(!item.getDescription().equals(null)) {
//            descriptionText.setText(item.getDescription());
//        }else{
//            descriptionText.setText("This is a cool product");
//        }

        changeCurrencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    convertCurrency(item);
                } catch (ClassNotFoundException
                        | NoSuchMethodException
                        | IllegalAccessException
                        | InvocationTargetException
                        | java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public String formatPriceEuro(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        return formatter.format(price);

    }

    private void addToUserCartFirebase() {
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


    public void setUpFilter() {


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