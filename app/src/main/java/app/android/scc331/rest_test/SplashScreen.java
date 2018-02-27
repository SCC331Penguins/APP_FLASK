package app.android.scc331.rest_test;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import app.android.scc331.rest_test.Services.PingRestServer;

public class SplashScreen extends AppCompatActivity {

    private TextView textView;
    private PingRestServer pingRestServer;
    private Handler handler;

    boolean server = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        textView = findViewById(R.id.status_text);

        pingRestServer = new PingRestServer();

        textView.setText("Connecting to server...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ping();
                    if (server){
                        break;
                    }
                }

                textView.setText("Connected!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);

            }
        }).start();

    }

    private boolean ping() {
        server = pingRestServer.Start();

        Log.i("PINGING", "Ping... " + server);

        if(!server){
            handler.postDelayed(new Runnable() {
                @Override
                public void run(){
                    ping();
                }
            }, 2000);
        }

        return true;
    }
}
