package com.example.f89497_quyet_nguyen_save_password.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f89497_quyet_nguyen_save_password.R;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DBManager;

public class UpdateAccountActivity extends AppCompatActivity {

    EditText edtUpdateWebsite;
    EditText edtUpdateUsername;
    EditText edtUpdatePassword;
    Button btnUpdate;
    Button btnCopyPassword;
    Button btnGoToWebsite;
    DBManager dbManager;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        //init elements
        edtUpdateWebsite = findViewById(R.id.edtUpdateWebsite);
        edtUpdateUsername = findViewById(R.id.edtUpdateUsername);
        edtUpdatePassword = findViewById(R.id.edtUpdatePassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCopyPassword = findViewById(R.id.btnCopyPasswordUpdate);
        btnGoToWebsite = findViewById(R.id.btnGoToWebsite);

        //get content from preference
        sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        edtUpdateWebsite.setText(sharedPreferences.getString("website",null));
        edtUpdateUsername.setText(sharedPreferences.getString("username",null));
        edtUpdatePassword.setText(sharedPreferences.getString("password",null));

        //create dbManager
        dbManager = new DBManager(this);
        dbManager.open();

        //update account
        btnUpdate.setOnClickListener(v -> {
            intent = new Intent(UpdateAccountActivity.this, MainActivity.class);
            String id = sharedPreferences.getString("id",null);
            String website = edtUpdateWebsite.getText().toString();
            String username = edtUpdateUsername.getText().toString();
            String password = edtUpdatePassword.getText().toString();
            if(id != null){
                dbManager.update(Long.parseLong(id),website,username,password);
            }
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Main Activity!",Toast.LENGTH_SHORT).show();
        });

        //copy password to clipboard
        btnCopyPassword.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", edtUpdatePassword.getText().toString());
            clipboard.setPrimaryClip(clip);
        });

        //go to the website
        btnGoToWebsite.setOnClickListener(v -> {
            String url = edtUpdateWebsite.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }
}
