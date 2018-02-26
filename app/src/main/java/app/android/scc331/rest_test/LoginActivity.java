package app.android.scc331.rest_test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.iid.FirebaseInstanceId;

import app.android.scc331.rest_test.Objects.LoginDetail;
import app.android.scc331.rest_test.Services.LoginRestOperation;
import app.android.scc331.rest_test.Services.RestOperation;

public class LoginActivity extends AppCompatActivity {

    private Button login, register;
    private AutoCompleteTextView username, password;
    private ProgressDialog progressDialog;

    private CheckBox remember, auto;

    private LoginDetail loginDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    private void login() {
        LoginRestOperation lro = new LoginRestOperation(this);
        boolean result = (boolean) lro.Start(username.getText().toString(), password.getText().toString());
        if (result) {
            if(remember.isChecked()){
                loginDetail.setLoginDetails(username.getText().toString(), password.getText().toString(), auto.isChecked());
                loginDetail.save(getApplicationContext());
            }else{
                loginDetail.setLoginDetails(null, null, false);
                loginDetail.save(getApplicationContext());
            }
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    private void register() {

    }
}
