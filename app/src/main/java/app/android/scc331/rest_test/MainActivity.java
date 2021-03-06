package app.android.scc331.rest_test;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.android.scc331.rest_test.Fragements.DatePicker;
import app.android.scc331.rest_test.Fragements.LiveDataFragment;
import app.android.scc331.rest_test.Fragements.MainFragment;
import app.android.scc331.rest_test.Fragements.RouterFragement;
import app.android.scc331.rest_test.Fragements.SensorDetailsFragment;
import app.android.scc331.rest_test.Fragements.SettingsFragment;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.CurrentZone;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.SavedState;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.Objects.ZoneListener;
import app.android.scc331.rest_test.RoomMaker.RoomViewFragement;
import app.android.scc331.rest_test.Services.BLENotifier;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveData;
import app.android.scc331.rest_test.Services.LiveData.Elements.SpinnerSensorListener;
import app.android.scc331.rest_test.Services.LiveData.MQTTConnection;
import app.android.scc331.rest_test.Services.LiveData.OpenConnection;
import app.android.scc331.rest_test.Services.SetTokenRestOperation;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener,
        RouterFragement.OnFragmentInteractionListener,
        SensorDetailsFragment.OnFragmentInteractionListener,
        LiveDataFragment.LiveDataInteractionListener,
        MQTTConnection.Callbacks,
        OpenConnection, DatePicker.DateInteractionListener, BeaconConsumer {

    public static ArrayList<Sensor> sensors;
    public static ArrayList<Router> routers;
    public static ArrayList<Actuator> actuators;
    public static MQTTConnection mqttConnection;
    public BeaconManager beaconManager;
    public static long startDate, endDate;

    private boolean mBounded;

    private Handler handler = new Handler();

    private boolean channelLive = false;

    public static SavedState savedState;

    public static CurrentZone currentZone;

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
        setTheme(defaultTheme ? R.style.AppTheme : R.style.AppTheme1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedState = SavedState.load(getApplicationContext());
        bottomBar = findViewById(R.id.bottomBar);
        for (int i = 0; i < bottomBar.getTabCount(); i++)
            bottomBar.getTabAtPosition(i).setBarColorWhenSelected( defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227") );
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
        bottomBar.selectTabAtPosition(2);
        bottomBar.invalidate();

        currentZone = new CurrentZone(null);

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_PRIVILEGED) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
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
        super.onStart();
        Log.d("START", "MainActivity");
        Intent mIntent = new Intent(this, MQTTConnection.class);
        startService(mIntent);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        GetRouterRestOperation getRouterRestOperation = new GetRouterRestOperation(this);
        beaconManager = BeaconManager.getInstanceForApplication(MainActivity.this);
        // Detect the main identifier (UID) frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry (TLM) frame:
                beaconManager.getBeaconParsers().add(new BeaconParser().
                        setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        // Detect the URL frame:
                beaconManager.getBeaconParsers().add(new BeaconParser().
                        setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        new Thread(new Runnable() {
            @Override
            public void run() {
                routers = (ArrayList<Router>) getRouterRestOperation.Start();
                System.out.println(1335);
                System.out.println(routers);
                beaconManager.bind(MainActivity.this);
            }
        }).start();

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
            SettingsFragment f = SettingsFragment.newInstance();
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
        } else if (tabId == R.id.settings_tab)
        {
            SettingsFragment f = SettingsFragment.newInstance();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
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

    @Override
    public void onDateChange(long utc, int type) {
        System.out.println("On Date Change Main Act");
        if(type == 0){
            startDate = utc;
        }else{
            endDate = utc;
        }
    }

    @Override
    public void onDateChange(String text, int type) {
        System.out.println("On Date Change Main Act");
        LiveDataFragment f = (LiveDataFragment) getFragmentManager().findFragmentByTag("live_data_fragment");
        if(type==0){
            f.setNewDate(text,startDate, 0);
        }else{
            f.setNewDate(text,endDate, 1);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        System.out.println(routers);
        BLENotifier bleNotifier = new BLENotifier(
                sharedPreferences.getString("token",""),
                routers.get(0).getId()
        );
        beaconManager.addRangeNotifier(bleNotifier);
        beaconManager.addMonitorNotifier(bleNotifier);
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }
}
