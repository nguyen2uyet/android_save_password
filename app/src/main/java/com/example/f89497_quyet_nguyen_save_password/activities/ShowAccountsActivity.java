package com.example.f89497_quyet_nguyen_save_password.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f89497_quyet_nguyen_save_password.R;
import com.example.f89497_quyet_nguyen_save_password.services.ThemeMusicService;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DBManager;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DatabaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ShowAccountsActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView tvGoogleUser;
    Button btnSignOut;
    DBManager dbManager;
    Button btnAdd;
    Button btnOpenMusic;
    Button btnStopMusic;
    ListView lvAccounts;
    SimpleCursorAdapter adapter;
    Intent intent;
    SharedPreferences sharedPreferences;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.WEBSITE, DatabaseHelper.USERNAME,DatabaseHelper.PASSWORD};

    final int[] to = new int[] { R.id.tvListViewAccountId, R.id.tvListViewAccountWebsite, R.id.tvListViewAccountUsername, R.id.tvListViewAccountPassword };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_accounts);

        //get the elements
        btnAdd = findViewById(R.id.btnAdd);
        btnOpenMusic = findViewById(R.id.btnOpenMusic);
        btnStopMusic = findViewById(R.id.btnStopMusic);
        lvAccounts = findViewById(R.id.lvAccounts);
        sharedPreferences = getSharedPreferences("account_id",MODE_PRIVATE);
        tvGoogleUser = findViewById(R.id.tvGoogleUser);
        btnSignOut = findViewById(R.id.btnLogout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        //get name of user google account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            tvGoogleUser.setText(personName);
        }

        //sign out google account
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



        //get data from database
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        //create adapter and give data of adapter to list view
        adapter = new SimpleCursorAdapter(this,R.layout.list_row,cursor,from,to,0);
        adapter.notifyDataSetChanged();
        lvAccounts.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ShowAccountsActivity.this,NewAccountActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"New Account Activity!",Toast.LENGTH_SHORT).show();
            }
        });

        //click on a item of list view and go to the UPDATE ACTIVITY
        lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
                intent = new Intent(ShowAccountsActivity.this, UpdateAccountActivity.class);
                TextView idTextView = view.findViewById(R.id.tvListViewAccountId);
                TextView websiteTextView = view.findViewById(R.id.tvListViewAccountWebsite);
                TextView usernameTextView = view.findViewById(R.id.tvListViewAccountUsername);
                TextView passwordTextView = view.findViewById(R.id.tvListViewAccountPassword);

                String _id = idTextView.getText().toString();
                String website = websiteTextView.getText().toString();
                String username= usernameTextView.getText().toString();
                String password = passwordTextView.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id",_id);
                editor.putString("website",website);
                editor.putString("username",username);
                editor.putString("password",password);
                editor.commit();
                startActivity(intent);
            }
        });

        //long click on item of list view and DELETE ACCOUNT
        lvAccounts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Long _id = Long.parseLong(((TextView)view.findViewById(R.id.tvListViewAccountId)).getText().toString());
                new AlertDialog.Builder(ShowAccountsActivity.this)
                        .setTitle("Do you want to remove account: " + ((TextView)view.findViewById(R.id.tvListViewAccountId)).getText().toString())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.delete(_id);
                                Cursor cursor = dbManager.fetch();
                                adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.list_row,cursor,from,to,0);
                                lvAccounts.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            };
        });

        //start THEME MUSIC SERVICE
        btnOpenMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent( ShowAccountsActivity.this, ThemeMusicService.class ) );
            }
        });

        //stop THEME MUSIC SERVICE
        btnStopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent( ShowAccountsActivity.this, ThemeMusicService.class ) );
            }
        });
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(ShowAccountsActivity.this,MainActivity.class));
            }
        });
    }

}
