package com.example.f89497_quyet_nguyen_save_password.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f89497_quyet_nguyen_save_password.R;
import com.example.f89497_quyet_nguyen_save_password.Utils;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DBManager;

public class NewAccountActivity extends AppCompatActivity {

    EditText edtWebsite;
    EditText edtUsername;
    EditText edtPassword;
    Button btnAdd;
    Button btnGeneratePassword;
    Button btnCopyPassword;
    DBManager dbManager;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        //init elements
        edtWebsite = findViewById(R.id.edtWebsite);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnAdd = findViewById(R.id.btnAdd);
        btnGeneratePassword = findViewById(R.id.btnGeneratePassword);
        btnCopyPassword = findViewById(R.id.btnCopyPassword);
        dbManager = new DBManager(this);
        dbManager.open();

        /* add new account to database */
        btnAdd.setOnClickListener(v -> {
            String website = edtWebsite.getText().toString();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            dbManager.insert(website,username,password);
            intent = new Intent(NewAccountActivity.this,ShowAccountsActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Show Accounts Activity!",Toast.LENGTH_SHORT).show();
        });

        //create strong password
        btnGeneratePassword.setOnClickListener(v -> edtPassword.setText(Utils.generateStrongPassword(16)));

        //copy password to clipboard
        btnCopyPassword.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", edtPassword.getText().toString());
            clipboard.setPrimaryClip(clip);
        });

    }
}
