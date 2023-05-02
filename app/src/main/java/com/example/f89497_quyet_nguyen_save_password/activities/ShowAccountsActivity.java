package com.example.f89497_quyet_nguyen_save_password.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.f89497_quyet_nguyen_save_password.R;
import com.example.f89497_quyet_nguyen_save_password.services.ThemeMusicService;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;

public class ShowAccountsActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView tvGoogleUser;
    Button btnSignOut;
    Button btnAdd;
    Button btnOpenMusic;
    Button btnStopMusic;
    Intent intent;

    boolean isLoggedIn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_accounts);

        //get the elements
        btnAdd = findViewById(R.id.btnAdd);
        btnOpenMusic = findViewById(R.id.btnOpenMusic);
        btnStopMusic = findViewById(R.id.btnStopMusic);
        sharedPreferences = getSharedPreferences("account_id",MODE_PRIVATE);
        tvGoogleUser = findViewById(R.id.tvGoogleUser);
        btnSignOut = findViewById(R.id.btnLogout);

        //-----------Get Data of Google API------------//
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        //get name of user google account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            if(personName != null){
                tvGoogleUser.setText(personName);
            }
        }

        //-----------Get Data of Facebook API------------//
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
            // Application code
            if(isLoggedIn){
                try {
                    String fullname = null;
                    if (object != null) {
                        fullname = object.getString("name");
                    }
                    tvGoogleUser.setText(fullname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        btnSignOut.setOnClickListener(v -> signOut());

        //button add
        btnAdd.setOnClickListener(view -> {
            intent = new Intent(ShowAccountsActivity.this,NewAccountActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"New Account Activity!",Toast.LENGTH_SHORT).show();
        });

        //start THEME MUSIC SERVICE
        btnOpenMusic.setOnClickListener(v -> startService(new Intent( ShowAccountsActivity.this, ThemeMusicService.class ) ));

        //stop THEME MUSIC SERVICE
        btnStopMusic.setOnClickListener(v -> stopService(new Intent( ShowAccountsActivity.this, ThemeMusicService.class ) ));
    }


    void signOut(){
        if(isLoggedIn) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(ShowAccountsActivity.this,MainActivity.class));
            finish();
        }
        gsc.signOut().addOnCompleteListener(task -> {
            finish();
            startActivity(new Intent(ShowAccountsActivity.this,MainActivity.class));
        });
    }

}
