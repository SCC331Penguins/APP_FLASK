package app.android.scc331.rest_test.Fragements;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.R;


public class RouterDevicesFragment extends Fragment {

    private String router_id;
    private String TAG = "SENSOR";
    ViewPager pager;
    private Button add_script;

    public RouterDevicesFragment() {
        // Required empty public constructor
    }

    public static RouterDevicesFragment newInstance(String router_id) {
        RouterDevicesFragment fragment = new RouterDevicesFragment();
        Bundle args = new Bundle();
        args.putString("router_id",router_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            router_id = getArguments().getString("router_id");
        }
        Log.i(TAG, ""+router_id);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_router_devices, container, false);
        pager = v.findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        TextView routername = v.findViewById(R.id.router_sensor_name);
        routername.setText(router_id);
        pager.setCurrentItem(0);
        add_script = v.findViewById(R.id.add_script_button);
        add_script.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TriggerConditionsMainFragment fragment = new TriggerConditionsMainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("router_id", router_id);
                fragment.setArguments(bundle);
                ft.replace(R.id.main_content_pane, fragment);
                ft.commit();
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    class PagerAdapter extends android.support.v13.app.FragmentPagerAdapter
    {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return SensorListFragment.newInstance(router_id);
                case 1:
                    return ActuatorsDirectControlFragment.newInstance();
            }
            return null;
        }
    }

}
