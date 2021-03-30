package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.fragments.AdminFragment;
import com.niall.electronicsstore.fragments.CatalogueFragment;
import com.niall.electronicsstore.fragments.ShoppingCartFragment;

public class BottomNavActivity extends AppCompatActivity {

    final SparseArray<Fragment> fragments = new SparseArray<>();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null) {
            assert user != null;
            Log.d("TAG", "onCreate: User signed in: " + user.getEmail());
        }

        if (savedInstanceState == null) {
            setInitialFrag();
        }

        BottomNavigationView bNV = findViewById(R.id.bottom_nav_view);

        bNV.setSelectedItemId(R.id.catalogue_nav);

        bNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = fragments.get(item.getItemId());

                switch(item.getItemId()) {
                    case R.id.cart_nav:
                        if (frag == null) frag = new ShoppingCartFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, frag)
                                .commit();
                        fragments.put(item.getItemId(), frag);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.catalogue_nav:
                        if (frag == null) frag = new CatalogueFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, frag)
                                .commit();
                        fragments.put(item.getItemId(), frag);
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.admin_nav:
                        if (frag == null) frag = new AdminFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, frag)
                                .commit();
                        fragments.put(item.getItemId(), frag);
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });

    }

    private void setInitialFrag() {

        Fragment frag = new CatalogueFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, frag)
                .commit();
        fragments.put(R.id.catalogue_nav, frag);
    }
}