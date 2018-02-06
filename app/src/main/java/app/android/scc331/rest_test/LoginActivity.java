package app.android.scc331.rest_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import app.android.scc331.rest_test.Services.RestOperation;

public class LoginActivity extends AppCompatActivity {

    private Button login, register;
    private AutoCompleteTextView username, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        username = findViewById(R.id.username_box);
        password = findViewById(R.id.password_box);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                login();
            }
        });

    }

    private void login(){
        RestOperation restOperation = new RestOperation();
        boolean result = restOperation.login(this, username.getText().toString(), password.getText().toString());
        SharedPreferences preferences = getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
        Log.i("LOGIN", "RESULT: " + result);
    }

    private void register(){

    }
}
