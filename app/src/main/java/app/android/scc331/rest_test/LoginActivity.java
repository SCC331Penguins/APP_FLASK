package app.android.scc331.rest_test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import app.android.scc331.rest_test.Fragements.LoadingDialogFragment;
import app.android.scc331.rest_test.Objects.LoginDetail;
import app.android.scc331.rest_test.Services.LoginRestOperation;
import app.android.scc331.rest_test.Services.RestOperation;

public class LoginActivity extends AppCompatActivity {
    public static boolean defaultTheme = true;
    private Button login, register;
    private AutoCompleteTextView username, password;
    private ProgressDialog progressDialog;

    private CheckBox remember, auto;

    private LoginDetail loginDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        defaultTheme = getPrefs.getBoolean("defaultTheme", true);
        setTheme(defaultTheme ? R.style.AppTheme : R.style.AppTheme1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        username = findViewById(R.id.username_box);
        password = findViewById(R.id.password_box);

        //TODO REMOVE THIS
        username.setText("Stouty");
        password.setText("dredd");

        auto = findViewById(R.id.auto_login);
        remember = findViewById(R.id.remember_login);

        loginDetail = LoginDetail.load(getApplicationContext());

        if(loginDetail.getUsername() != null){
            username.setText(loginDetail.getUsername());
            password.setText(loginDetail.getPassword());

            if(loginDetail.getAutoLogin()){
                login();
            }

        }

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) ||
        (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED))
        {
            Log.d("PERM", "Requesting permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        Log.d("PERM", "permission granted");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                login();
            }
        });
    }

    private void login()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                LoginRestOperation lro = new LoginRestOperation(getApplication());
                boolean result = (boolean) lro.Start(username.getText().toString(), password.getText().toString());
                if (result)
                {
                    if (remember.isChecked())
                    {
                        loginDetail.setLoginDetails(username.getText().toString(), password.getText().toString(), auto.isChecked());
                        loginDetail.save(getApplicationContext());
                    } else
                    {
                        loginDetail.setLoginDetails(null, null, false);
                        loginDetail.save(getApplicationContext());
                    }

                    SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                    //  Create a new boolean and preference and set it to true
                    boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                    //  If the activity has never started before...
                    if (isFirstStart)
                    {
                        SharedPreferences.Editor e = getPrefs.edit();
                        //  Edit preference to make it false because we don't want this to run again
                        e.putBoolean("firstStart", false);
                        //  Apply changes
                        e.apply();
                        //  Launch app intro
                        final Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                        startActivity(i);

                        //  Make a new preferences editor

                    } else
                    {

                        Intent i = new Intent(getApplication(), MainActivity.class);
                        startActivity(i);
                    }
                }
            }
        }).start();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Logging you in...");
        LoadingDialogFragment loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.setArguments(bundle);
        loadingDialogFragment.show(getFragmentManager(), "");
    }

    private void register() {

    }
}
