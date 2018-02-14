package app.android.scc331.rest_test;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import app.android.scc331.rest_test.Fragements.MainFragment;
import app.android.scc331.rest_test.Fragements.RouterFragement;
import app.android.scc331.rest_test.Fragements.SensorDetailsFragment;
import app.android.scc331.rest_test.Fragements.SensorFragment;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.SavedState;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.RoomMaker.MapDesignFragment;
import app.android.scc331.rest_test.RoomMaker.RoomViewFragement;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;
import app.android.scc331.rest_test.Services.SetTokenRestOperation;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener,
        RouterFragement.OnFragmentInteractionListener,
        SensorFragment.OnFragmentInteractionListener,
        SensorDetailsFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener{

    public static ArrayList<Sensor> sensors;
    public static ArrayList<Router> routers;
    public static ArrayList<Actuator> actuators;

    public static SavedState savedState;

    private BottomBar bottomBar;

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public static void updateRouters(Context context){
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
        }else if(tabId == R.id.welcome_tab){
            MainFragment f = new MainFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
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
        }else if(tabId == R.id.welcome_tab){
            MainFragment f = new MainFragment();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        }else if(tabId == R.id.room_overview_tab){
            RoomViewFragement f = new RoomViewFragement();
            fragmentTransaction.replace(R.id.main_content_pane, f).commit();
        }
    }
}
