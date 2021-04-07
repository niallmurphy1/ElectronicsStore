package com.niall.electronicsstore.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.niall.electronicsstore.R;

public class RegLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log);

        FirebaseAuth mainAuth = FirebaseAuth.getInstance();

        Intent dashIntent = new Intent(this, BottomNavActivity.class);

        if (mainAuth.getCurrentUser() != null) {
            startActivity(dashIntent);
        }

    }

    public void onRegisterUserClick(View view) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }

    public void onRegisterAdminClick(View view) {
        startActivity(new Intent(this, RegisterAdminActivity.class));
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}