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
import com.niall.electronicsstore.entities.User;

import org.jetbrains.annotations.NotNull;

import static com.niall.electronicsstore.util.Constants.adminCodes;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mainAuth;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText adminCodeText;
    private boolean admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mainAuth = FirebaseAuth.getInstance();

        if (mainAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, BottomNavActivity.class));
            return;
        }


        emailEdit = findViewById(R.id.emailEditText);
        passwordEdit = findViewById(R.id.passwordEditText);
        adminCodeText = findViewById(R.id.adminTextField);
    }



    @NotNull
    private String getPasswordInput() {
        return passwordEdit.getText().toString();
    }

    @NotNull
    private String getEmailInput() {
        return emailEdit.getText().toString();
    }

    public void onRegisterClick(View view){

        admin = false;
        if(!adminCodeText.getText().toString().equals("")){

            for(int i = 0; i < adminCodes.length; i++){

                if(adminCodeText.getText().toString().equals(adminCodes[i])){
                     admin = true;
                }
            }


        }
        mainAuth.createUserWithEmailAndPassword(getEmailInput(), getPasswordInput())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Registration successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mainAuth.getCurrentUser();
                            String userId = user.getUid();



                            User aUser;

                            if(admin){
                                 aUser = new User(emailEdit.getText().toString(), passwordEdit.getText().toString(), true);

                            }
                            else{
                                 aUser = new User(emailEdit.getText().toString(), passwordEdit.getText().toString(), false);

                            }

                            String name = user.getDisplayName();

                            System.out.println(name);

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child("User").child(userId).setValue(aUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Write successful", Toast.LENGTH_LONG).show();

                                    //startDashboard Activity
                                }
                            });




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        //...
                    }
                });
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
}