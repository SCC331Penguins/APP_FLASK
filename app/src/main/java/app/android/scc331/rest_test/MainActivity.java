package app.android.scc331.rest_test;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.android.scc331.rest_test.Fragements.LiveDataFragment;
import app.android.scc331.rest_test.Fragements.MainFragment;
import app.android.scc331.rest_test.Fragements.RouterFragement;
import app.android.scc331.rest_test.Fragements.SensorDetailsFragment;
import app.android.scc331.rest_test.Fragements.RouterDevicesFragment;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.SavedState;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.RoomMaker.RoomViewFragement;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveData;
import app.android.scc331.rest_test.Services.LiveData.Elements.SpinnerSensorListener;
import app.android.scc331.rest_test.Services.LiveData.MQTTConnection;
import app.android.scc331.rest_test.Services.LiveData.OpenConnection;
import app.android.scc331.rest_test.Services.SetTokenRestOperation;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener,
        RouterFragement.OnFragmentInteractionListener,
        SensorDetailsFragment.OnFragmentInteractionListener,
        LiveDataFragment.LiveDataInteractionListener,
        MQTTConnection.Callbacks,
        OpenConnection{

    public static ArrayList<Sensor> sensors;
    public static ArrayList<Router> routers;
    public static ArrayList<Actuator> actuators;
    public static MQTTConnection mqttConnection;

    private boolean mBounded;

    private Handler handler = new Handler();

    private boolean channelLive = false;

    public static SavedState savedState;

    private BottomBar bottomBar;

    private SpinnerSensorListener spinnerSensorListener;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mqttConnection = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(getApplicationContext(), "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MQTTConnection.LocalBinder mLocalBinder = (MQTTConnection.LocalBinder) service;
            mqttConnection = mLocalBinder.getServerInstance();
            mqttConnection.registerClient(MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedState = SavedState.load(getApplicationContext());
        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                GetRouterRestOperation getRouterRestOperation = new GetRouterRestOperation(getApplicationContext());
                MainActivity.routers = (ArrayList<Router>) getRouterRestOperation.Start();
            }
        }).start();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
        MainFragment f = new MainFragment();
        fragmentTransaction.replace(R.id.main_content_pane, f).commit();

        updateRouters(getApplicationContext());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    @Override
    protected void onStart() {
        Log.d("START", "MainActivity");
        Intent mIntent = new Intent(this, MQTTConnection.class);
        startService(mIntent);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        GetRouterRestOperation getRouterRestOperation = new GetRouterRestOperation(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                routers = (ArrayList<Router>) getRouterRestOperation.Start();

            }
        }).start();
        super.onStart();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public static void updateRouters(Context context) {
        SetTokenRestOperation setTokenRestOperation = new SetTokenRestOperation(context);
        setTokenRestOperation.Start(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
        if (tabId == R.id.router_tab) {
            RouterFragement f = new RouterFragement();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.welcome_tab) {
            MainFragment f = new MainFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.settings_tab) {
            MainFragment f = new MainFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.live_data_tab) {
            LiveDataFragment f = new LiveDataFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f, "live_data_fragment").commit();
        }
    }

    @Override
    public void onTabReSelected(int tabId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
        if (tabId == R.id.router_tab) {
            RouterFragement f = new RouterFragement();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.welcome_tab) {
            MainFragment f = new MainFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.room_overview_tab) {
            RoomViewFragement f = new RoomViewFragement();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        } else if (tabId == R.id.live_data_tab) {
        }
    }


    @Override
    public void setStatusText(String text) {
        LiveDataFragment f = (LiveDataFragment) getFragmentManager().findFragmentByTag("live_data_fragment");
        f.setStatusText(text);
    }

    @Override
    public void startLiveDataProcess(String router_id) {
        channelLive = false;
        setStatusText("Requesting new channel...");
        GetNewChannelRestOperation getNewChannelRestOperation = new GetNewChannelRestOperation(this);
        String channel_name = getNewChannelRestOperation.Start(router_id);
        setStatusText("Channel requested...");
        if (channel_name != null) {
            mqttConnection.subscribe(channel_name, router_id);
            setStatusText("Waiting for router connection...");
        } else {
            setStatusText("Error getting channel.");
        }
    }

    @Override
    public void getData(String sensor_id) {
        mqttConnection.getData(sensor_id);
    }

    @Override
    public void promptLiveData(HashMap<String, ArrayList<LiveData>> sensor_data_hashmap) {
        channelLive = true;
        LiveDataFragment f = (LiveDataFragment) getFragmentManager().findFragmentByTag("live_data_fragment");
        f.updateData(sensor_data_hashmap);
    }

    @Override
    public void updateLiveData(JSONObject data) {

    }

    @Override
    public void openConnection(String router_id) {
        if(mqttConnection.isConnected(router_id)) return;

        GetNewChannelRestOperation getNewChannelRestOperation = new GetNewChannelRestOperation(this);
        String channel_name = getNewChannelRestOperation.Start(router_id);

        if(channel_name != null){
            mqttConnection.subscribe(channel_name, router_id);
        }
    }
}
