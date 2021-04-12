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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.activities.RegLogActivity;
import com.niall.electronicsstore.activities.RegisterUserActivity;
import com.niall.electronicsstore.adapters.CatalogueItemAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.interpreter.Euro;
import com.niall.electronicsstore.interpreter.Expression;
import com.niall.electronicsstore.util.ExpressionUtil;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;



public class CatalogueFragment extends Fragment implements CatalogueItemAdapter.ViewHolder.OnItemListener {



    private static final String TAG = "CatalogueTAG";
    private RecyclerView recyclerView;
    private CatalogueItemAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();


    private FirebaseAuth mainAuth = FirebaseAuth.getInstance();
    private DatabaseReference userCartRef;

    //bottom sheet components
    private LinearLayout headerLayout;
    private ConstraintLayout bottomSheetConstraint;
    private ImageView headerArrowImage;
    private BottomSheetBehavior bottomSheetBehavior;
    private Button addToCartButton;
    private ImageView itemImage;
    private TextView titleText;
    private TextView descriptionText;
    private TextView manufacturerText;
    private TextView categoryText;
    private TextView priceText;

    private String userId;

    private ArrayList<Item> shopCartItems;

    private int previousChecked;

    private RadioGroup currencyRadioGroup;

    private Expression converter;

    private EditText searchBarEdit;


    public FirebaseAuth firebaseAuth;

    public CatalogueFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        userId = mainAuth.getUid();

        setHasOptionsMenu(true);

        shopCartItems = new ArrayList<>();

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-shopCart");

        retrieveCartFromFirebase();

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

        itemImage = view.findViewById(R.id.bap_sheet_item_image);
        titleText = view.findViewById(R.id.bap_sheet_title_text);
        descriptionText = view.findViewById(R.id.bap_sheet_description_text);
        categoryText = view.findViewById(R.id.bap_sheet_category_text);
        manufacturerText = view.findViewById(R.id.bap_sheet_manufacturer_text);
        priceText = view.findViewById(R.id.bap_sheet_price_text);



        //set up bottom sheet
        bottomSheetConstraint = view.findViewById(R.id.bottom_sheet_item_view);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetConstraint);
        headerLayout = view.findViewById(R.id.header_layout_item_view);
        headerArrowImage = view.findViewById(R.id.bap_sheet_arrow);
        currencyRadioGroup = view.findViewById(R.id.bap_currency_radio_group);
        RadioButton euroRadio = view.findViewById(R.id.radio_euro);

        //euroRadio.setSelected(true);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        previousChecked = currencyRadioGroup.getCheckedRadioButtonId();

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



    private void convertCurrency(Item item, String to){


        //TODO fix this currency malarkey, very confusing
       switch(to){

           case "Euro":

               double priceCents = Double.parseDouble(converter.euros(item.getPriceCents()));

               double priceWhole = (priceCents / 100.00);

               //put a switch here for type of price
               String newPrice = formatPriceEuro(priceWhole);

               priceText.setText(newPrice);

               converter = ExpressionUtil.forCode(to);

               previousChecked = R.id.radio_euro;

               break;

           case "Pound":

               double priceCentsPounds = Double.parseDouble(converter.pounds(item.getPriceCents()));

               item.setPriceCents((int) priceCentsPounds);

               double priceWholePounds = (priceCentsPounds / 100.00);

               //put a switch here for type of price
               String newPricePounds = formatPricePounds(priceWholePounds);


               converter = ExpressionUtil.forCode(to);

               previousChecked = R.id.radio_pounds;

               break;

       }

//        double priceCents = Double.parseDouble(converter.pounds(item.getPriceCents()));
//
//        double priceWhole = (priceCents / 100.00);
//
//        //put a switch here for type of price
//        String newPrice = formatPricePounds(priceWhole);
//
//        priceText.setText(newPrice);
//
//        converter = ExpressionUtil.forCode(to);



    }


    @Override
    public void onItemClick(Item item) {


        Log.d(TAG, "onItemClick: You clicked " + item.toString());

        converter =  new Euro();

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

        descriptionText.setText(item.getDescription());


        currencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch(checkedId){

                    case(R.id.radio_euro):

                        convertCurrency(item, "Euro");
                        break;


                    case(R.id.radio_pounds):

                        convertCurrency(item, "Pound");
                        break;
                }
            }
        });


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean duplicate = false;

                for (Item shopItem : shopCartItems) {

                    if (shopItem.getName().equals(item.getName())) {

                        Toast.makeText(getContext(), "This item is already in your cart!", Toast.LENGTH_SHORT).show();
                        duplicate = true;
                    }

                }

                if (!duplicate) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-shopCart");

                    String key = db.push().getKey();

                    assert key != null;

                    item.setCustQuant(1);

                    db.child(key).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Added to cart!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });



    }

    public String formatPriceEuro(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        return formatter.format(price);

    }

    public String formatPricePounds(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        return formatter.format(price);

    }


    public void retrieveCartFromFirebase(){

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                shopCartItems.clear();

                for(DataSnapshot keyNode: snapshot.getChildren()){

                    Item item = keyNode.getValue(Item.class);
                    item.setId(keyNode.getKey());
                    shopCartItems.add(item);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void fillRCV() {

        items.clear();
        items.add(new Item("Magic Keyboard with Numeric Keypad ",
                "Apple",
                14999,
                "Keyboards",
                "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MRMH2B?wid=1144&hei=1144&fmt=jpeg&qlt=95&.v=1520717406876",
                "\n" +
                        "Magic Keyboard with Numeric Keypad features an extended layout, with document navigation controls for quick scrolling and full-size arrow keys for gaming. A scissor mechanism beneath each key allows for increased stability, while optimised key travel and a low profile provide a comfortable and precise typing experience. The numeric keypad is also great for spreadsheets and finance applications. And the built-in, rechargeable battery is incredibly long-lasting, powering your keyboard for about a month or more between charges",
                100, 0));

        items.add(new Item("UE MEGABOOM",
                "Ultimate Ears",
                11999,
                "Speakers",
                "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/HGPQ2?wid=1144&hei=1144&fmt=jpeg&qlt=95&op_usm=0.5,0.5&.v=1509146406671",
                "Super-portable wireless Bluetooth speaker built for adventure. With balanced 360° sound, deep bass and serious waterproof, drop proof and dust proof, Now with one-touch music controls, it's the ultimate go-anywhere speaker.",
                100, 0));

        items.add(new Item("Apple AirPods with charging case",
                "Apple",
                14500,
                "Headphones",
                "https://brain-images-ssl.cdn.dixons.com/7/5/10191857/u_10191857.jpg",
                "The new AirPods deliver the wireless headphone experience, reimagined. Just pull them out of the charging case and they’re ready to use with your iPhone, Apple Watch, iPad or Mac.\n" +
                        "\n" +
                        "After a simple one-tap setup, AirPods work like magic. They’re automatically on and always connected. AirPods can even sense when they’re in your ears and pause when you take them out.",
                100, 0));

        items.add(new Item("Lypertek Tevi Wireless Headphones",
                "Lypertek",
                11900,
                "Headphones",
                "https://fdn.gsmarena.com/imgroot/news/20/12/lypertek-tevi/-1200w5/gsmarena_001.jpg",
                "\n" +
                        "We think nothing is more important than sound in earphones.  Sound is the #1 priority in the development of TEVI.\n" +
                        "\n" +
                        "TEVI has the signature sound of LYPERTEK which is the result of a variety of psychoaccoustics / acoustic engineering studies and on long-standing know-how in the audio industry of LYPERTEK.\n" +
                        "\n" +
                        "A true wireless earphones are small, but it's one complete audio system. In its small housing, there's DAC which receives wireless audio signal and converts it into an analog signal, an AMP that amplifies the analog signal, and a speaker unit that plays sound with the signal.Each part of it is carefully selected and tuned for TEVI for high quality sound.",
                100, 0));

        items.add(new Item("Garmin Venu SQ GPS Smartwatch",
                "Garmin",
                11999,
                "Wearables",
                "https://euronics.ie/uploaded/thumbnails/db_file_img_17104_600x800.jpg",
                "With a sleek design that’s suited for every outfit and every part of your day, this watch features a bright colour display and optional always-on mode, so you can see everything with a quick glance.",
                100, 0));

        items.add(new Item("UE FITS",
                "Ultimate Ears",
                20999,
                "Headphones",
                "https://cdn.shopify.com/s/files/1/0058/1576/3001/products/ohboy072020_basic_cloud_02051_1080x.jpg?v=1614382043",
                "UE FITS are the world’s first true wireless earbuds that are custom fitted to your unique ear shape — in less time than it takes to make a cup of coffee. The result? You can wear FITS in complete comfort all day long, while enjoying exceptional noise isolation and truly immersive sound. Does this sound like magical technology from the future? We couldn’t agree more.",
                100, 0));


        items.add(new Item("Google Pixel 5",
                "Google",
                62900,
                "Smartphones",
                "https://cdn.dxomark.com/wp-content/uploads/medias/post-59199/google_pixel_5_frontback.jpeg",
                "Google Pixel 5 smartphone was launched on 30th September 2020. The phone comes with a 6.00-inch touchscreen display with a resolution of 1080x2340 pixels at a pixel density of 432 pixels per inch (ppi) and an aspect ratio of 19.5:9. Google Pixel 5 is powered by a 1.8GHz octa-core Qualcomm Snapdragon 765G processor. It comes with 8GB of RAM. The Google Pixel 5 runs Android 11 and is powered by a 4080mAh non-removable battery. The Google Pixel 5 supports wireless charging, as well as proprietary fast charging.",
                100, 0));



    }

    public void addItemsToFirebase(ArrayList<Item> items){

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Item");

        for(Item item : items){



        }
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