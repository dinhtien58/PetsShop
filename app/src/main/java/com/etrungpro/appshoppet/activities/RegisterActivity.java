package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.User;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFirstNameRegister;
    EditText edtLastNameRegister;
    TextInputEditText edtUserRegister;
    TextInputEditText edtPasswordRegister;
    TextInputEditText edtPasswordConfirm;
    TextView tvSignIn;
    Button btnRegister;
    ProgressDialog progressDialog;
    ImageView loginBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = edtFirstNameRegister.getText().toString();
                String lastName = edtLastNameRegister.getText().toString();
                String email = edtUserRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString().trim();
                String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(RegisterActivity.this, password + " " + passwordConfirm , Toast.LENGTH_SHORT).show();
                    edtPasswordConfirm.setFocusable(true);
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseAuth user = FirebaseAuth.getInstance();
                            Timestamp timestamp = Timestamp.now();
                            User newUser = new User(email, firstName, lastName, "toc_tim.jpg", password, timestamp);
                            db.collection("users").document(user.getUid()).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> cart = new HashMap<>();
                                        ArrayList<String> productIds = new ArrayList<>();
                                        cart.put("productIds", productIds);
                                        cart.put("userid", user.getUid());
                                        db.collection("carts")
                                                .add(cart)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                    }

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("create error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tvSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }


    void initUI() {
        progressDialog = new ProgressDialog(this);
        edtUserRegister = findViewById(R.id.edtUserRegister);
        edtFirstNameRegister = findViewById(R.id.edt_firstName_register);
        edtLastNameRegister = findViewById(R.id.edt_lastName_register);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtPasswordConfirm = findViewById(R.id.edtPasswordConfirm);
        btnRegister = findViewById(R.id.btn_register);
        loginBanner = findViewById(R.id.login__banner);
        tvSignIn = findViewById(R.id.tv_signin);

    }
}