package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Address;
import com.niall.electronicsstore.entities.Name;
import com.niall.electronicsstore.entities.PaymentMethod;
import com.niall.electronicsstore.entities.User;

import org.jetbrains.annotations.NotNull;

public class RegisterUserActivity extends AppCompatActivity {


    private FirebaseAuth mainAuth;
    private Intent dashIntent;


    //User details
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;

    //User address details
    private EditText addressLine1Edit;
    private EditText addressLine2Edit;
    private EditText zipEdit;
    private EditText cityEdit;
    private EditText countryEdit;

    //User Payment details
    private EditText nameOnCardEdit;
    private EditText cardNumberEdit;
    private EditText securityCodeEdit;
    private EditText expirationMonthEdit;
    private EditText expirationYearEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        mainAuth = FirebaseAuth.getInstance();

        dashIntent = new Intent(this, BottomNavActivity.class);


        //initialise variables
        firstNameEdit = findViewById(R.id.firstNameText);
        lastNameEdit = findViewById(R.id.lastNameEditText);
        emailEdit = findViewById(R.id.emailEditTextRegisterUser);
        passwordEdit = findViewById(R.id.passwordEditTextRegisterUser);

        addressLine1Edit = findViewById(R.id.addressLine1EditText);
        addressLine2Edit = findViewById(R.id.addressLine2EditText);
        zipEdit = findViewById(R.id.zipCodeEditText);
        cityEdit = findViewById(R.id.cityEditText);
        countryEdit = findViewById(R.id.countryEditText);

        nameOnCardEdit = findViewById(R.id.nameOnCardText);
        cardNumberEdit = findViewById(R.id.cardNumberEditText);
        securityCodeEdit = findViewById(R.id.securityCodeEditText);
        expirationMonthEdit = findViewById(R.id.oder_details_expiration_month_text);
        expirationYearEdit = findViewById(R.id.oder_details_expiration_year_text);

    }

    @NotNull
    private String getPasswordInput() {
        return passwordEdit.getText().toString();
    }

    @NotNull
    private String getEmailInput() {
        return emailEdit.getText().toString();
    }

    public void onRegisterClick(View view) {


        mainAuth.createUserWithEmailAndPassword(getEmailInput(), getPasswordInput())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(RegisterUserActivity.this, "Registration successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mainAuth.getCurrentUser();
                            String userId = user.getUid();

                            Name newName = new Name.NameBuilder(firstNameEdit.getText().toString()
                                    , lastNameEdit.getText().toString())
                                    .build();



                            User aUser = new User.UserBuilder(emailEdit.getText().toString(), newName, false)
                                    .address(new Address.AddressBuilder(addressLine1Edit.getText().toString()
                                            , addressLine2Edit.getText().toString()
                                            , zipEdit.getText().toString()
                                            , cityEdit.getText().toString()
                                            , countryEdit.getText().toString())
                                            .build()).paymentMethod(new PaymentMethod.PaymentMethodBuilder(nameOnCardEdit.getText().toString()
                                            , cardNumberEdit.getText().toString()
                                            , securityCodeEdit.getText().toString()
                                            , Integer.parseInt(expirationMonthEdit.getText().toString())
                                            , Integer.parseInt(expirationYearEdit.getText().toString())).build()).build();


                            String name = user.getDisplayName();

                            System.out.println(name);

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child("User").child(userId).setValue(aUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterUserActivity.this, "Write successful", Toast.LENGTH_LONG).show();

                                    //startDashboard Activity

                                    startActivity(dashIntent);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterUserActivity.this, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        //...
                    }
                });
    }
}