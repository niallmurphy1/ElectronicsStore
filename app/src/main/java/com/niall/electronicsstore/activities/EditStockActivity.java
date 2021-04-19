package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.CatalogueItemAdapter;
import com.niall.electronicsstore.entities.Item;

import java.util.ArrayList;

public class EditStockActivity extends AppCompatActivity implements CatalogueItemAdapter.ViewHolder.OnItemListener{


    private RecyclerView recyclerView;
    private CatalogueItemAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();

    private TextView currentStockText;
    private NumberPicker stockPicker;
    private DatabaseReference itemsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);

        itemsRef = FirebaseDatabase.getInstance().getReference("Item");

        setUpRCV();
        getItemsFromFirebase();

    }


    public void getItemsFromFirebase(){

        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                items.clear();

                for(DataSnapshot keyNode: snapshot.getChildren()){

                    Item item = keyNode.getValue(Item.class);

                    String key = keyNode.getKey();
                    item.setId(key);
                    items.add(item);

                }


                Log.d("TAG", "onDataChange: IIIIIIItems: " + items.toString());
                adapter.addItems(items);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setUpRCV() {

        recyclerView = findViewById(R.id.edit_stock_rcv);
        adapter = new CatalogueItemAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }




    @Override
    public void onItemClick(Item item) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_stock, null);
        currentStockText = dialogView.findViewById(R.id.current_stock_txt_view);
        stockPicker = dialogView.findViewById(R.id.stock_number_picker);
        stockPicker.setMinValue(1);
        stockPicker.setMaxValue(1000);
        currentStockText.setText("Current stock: " + item.getStockLevel());
        builder.setView(dialogView);
        builder.setTitle("Update Stock");
        builder.setPositiveButton("Update", (dialog, which) -> {

            itemsRef.child(item.getId()).child("stockLevel").setValue(item.getStockLevel() + stockPicker.getValue());

            Toast.makeText(this, "Success, " + stockPicker.getValue() + " units added for " + item.getName(), Toast.LENGTH_SHORT).show();

        }).setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}