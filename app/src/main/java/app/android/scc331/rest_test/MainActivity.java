package app.android.scc331.rest_test;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.android.scc331.rest_test.Fragements.RouterFragement;
import app.android.scc331.rest_test.Fragements.SensorFragment;

public class MainActivity extends AppCompatActivity implements RouterFragement.OnFragmentInteractionListener, SensorFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RouterFragement rf = new RouterFragement();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content_pane, rf).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
