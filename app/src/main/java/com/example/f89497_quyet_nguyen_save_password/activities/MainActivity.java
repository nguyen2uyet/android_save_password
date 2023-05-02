package com.example.f89497_quyet_nguyen_save_password.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.f89497_quyet_nguyen_save_password.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //create google sign in option and google sign in client
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    ImageView facebookBtn;
    TextView tvFacebook;
    TextView tvGoogle;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        googleBtn = findViewById(R.id.google_btn);
        facebookBtn = findViewById(R.id.fb_btn);
        tvFacebook = findViewById(R.id.tvFacebook);
        tvGoogle = findViewById(R.id.tvGoogle);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        //check google sign in account
        //if you don't have credential => can't enter second activity
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null) navigateToShowAccountsActivity();

        //google button listener
        googleBtn.setOnClickListener(v -> signIn());


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        navigateToShowAccountsActivity();
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                        // App code
                    }
                });

        facebookBtn.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Collections.singletonList("public_profile")));



    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    //on activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToShowAccountsActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    void navigateToShowAccountsActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this,ShowAccountsActivity.class);
        startActivity(intent);
    }

}