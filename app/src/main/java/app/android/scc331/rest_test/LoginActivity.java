package app.android.scc331.rest_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import app.android.scc331.rest_test.Services.LoginRestOperation;
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
        LoginRestOperation lro = new LoginRestOperation(this);
        boolean result = (boolean) lro.Start(username.getText().toString(), password.getText().toString());
        if(result){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    private void register(){

    }
}
