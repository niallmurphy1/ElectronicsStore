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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.AdminDetails;
import com.niall.electronicsstore.entities.Name;
import com.niall.electronicsstore.entities.User;

import org.jetbrains.annotations.NotNull;

import static com.niall.electronicsstore.util.Constants.adminCodes;
import static com.niall.electronicsstore.util.Constants.jobTitles;

public class RegisterAdminActivity extends AppCompatActivity {


    String TAG = "RegisterAdminActivity";

    //Firebase
    private FirebaseAuth mainAuth;

    //Dashboard Intent
    private Intent dashIntent;

    //Admin account details
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;

    private EditText employeeIDEdit;
    private EditText adminCodeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        //initialise firebase variable
        mainAuth = FirebaseAuth.getInstance();

        dashIntent = new Intent(this, BottomNavActivity.class);

        //initialise variables
        firstNameEdit = findViewById(R.id.firstNameAdminText);
        lastNameEdit = findViewById(R.id.lastNameAdminText);
        emailEdit = findViewById(R.id.emailEditTextAdminText);
        passwordEdit = findViewById(R.id.passwordTextAdmin);

        employeeIDEdit = findViewById(R.id.employeeIDText);
        adminCodeEdit = findViewById(R.id.employeeAdminCodeText);


    }

    @NotNull
    private String getPasswordInput() {
        return passwordEdit.getText().toString();
    }

    @NotNull
    private String getEmailInput() {
        return emailEdit.getText().toString();
    }

    @NotNull
    private String getAdminCodeInput() {
        return adminCodeEdit.getText().toString();
    }


    public void onRegisterRegisterAdminClick(View view) {

        if (getEmailInput().equals("niallos712@gmail.com") && getAdminCodeInput().equals(adminCodes[0])
                || getEmailInput().equals("n1@live.ie") && getAdminCodeInput().equals(adminCodes[1])
                || getEmailInput().equals("joe@gmail.com") && getAdminCodeInput().equals(adminCodes[2])
                || getEmailInput().equals("acc123@gmail.com") && getAdminCodeInput().equals(adminCodes[3])
                || getEmailInput().equals("cashier@gmail.com") && getAdminCodeInput().equals(adminCodes[4])
        ) {

            switch (getEmailInput()) {

                case ("niallos712@gmail.com"):
                    registerAdmin(jobTitles[0]);
                    break;


                case ("n1@live.ie"):
                    registerAdmin(jobTitles[1]);
                    break;


                case ("joe@gmail.com"):
                    registerAdmin(jobTitles[2]);
                    break;

                case ("acc123@gmail.com"):
                    registerAdmin(jobTitles[3]);
                    break;


                case ("cashier@gmail.com"):
                    registerAdmin(jobTitles[4]);
                    break;


            }

        } else {

            Toast.makeText(this, "Access denied: Admin details incorrect", Toast.LENGTH_LONG).show();
        }

    }


    public void registerAdmin(String jobTitle) {


        if (getEmailInput().equals("") || getPasswordInput().equals("")) {

            Toast.makeText(this, "You must enter and email and password", Toast.LENGTH_SHORT).show();

        } else {

            mainAuth.createUserWithEmailAndPassword(getEmailInput(), getPasswordInput())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Log.d("TAG", "createUserAdmin:success");
                                Toast.makeText(RegisterAdminActivity.this, "Registration successful.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mainAuth.getCurrentUser();
                                String userId = user.getUid();


                                Name adminName = new Name.NameBuilder(firstNameEdit.getText().toString(), lastNameEdit.getText().toString()).build();

                                AdminDetails adminDetails = new AdminDetails.AdminBuilder(employeeIDEdit.getText().toString(), jobTitle).build();

                                User adminUser = new User.UserBuilder(getEmailInput(), adminName)
                                        .adminDetails(adminDetails).build();


                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                db.child("User").child(userId).setValue(adminUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegisterAdminActivity.this, "Write successful", Toast.LENGTH_LONG).show();

                                        //startDashboard Activity

                                        startActivity(dashIntent);
                                    }
                                });

                            } else {

                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterAdminActivity.this, "Register failed: " + task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }


                        }


                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: " + e);
                }
            });


        }
    }

}