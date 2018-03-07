package app.android.scc331.rest_test.Fragements;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.SetSensorsRestOperation;

public class SensorDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private LinearLayout ss1, ss2, ss4, ss8, ss16, ss32, ss64, ss128;
    private ToggleButton sensor_tb_1,sensor_tb_2,sensor_tb_4,sensor_tb_8,sensor_tb_16,sensor_tb_32,sensor_tb_64,sensor_tb_128;
    private ImageView sensor_im_1,sensor_im_2,sensor_im_4,sensor_im_8,sensor_im_16,sensor_im_32,sensor_im_64,sensor_im_128;
    private TextView sensor_tx_1, sensor_tx_2, sensor_tx_4, sensor_tx_8, sensor_tx_16, sensor_tx_32, sensor_tx_64, sensor_tx_128;
    private int[] sensor_values = new int[]{1,2,4,8,16,32,64,128};

    private ToggleButton[] toggleButtons;
    private ImageView[] imageViews;

    private Button saveButton;

    private int config;

    private Sensor sensor = null;
    private String sensor_id;

    private String router_id;

    public SensorDetailsFragment() {}

    public static SensorDetailsFragment newInstance(String sensor_id, String router_id) {
        SensorDetailsFragment fragment = new SensorDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, sensor_id);
        args.putString(ARG_PARAM2, router_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sensor_id =  getArguments().getString(ARG_PARAM1);
            router_id = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor_details, container, false);

        TextView name = v.findViewById(R.id.sensor_info_frag_name);
        name.setText(MainActivity.savedState.getSensorName(sensor_id));

        for(Sensor s : MainActivity.sensors){
            if (s.getId().equals(sensor_id))
                sensor = s;
        }

        ss1 = v.findViewById(R.id.sensor_select_1);
        ss2 = v.findViewById(R.id.sensor_select_2);
        ss4 = v.findViewById(R.id.sensor_select_4);
        ss8 = v.findViewById(R.id.sensor_select_8);
        ss16 = v.findViewById(R.id.sensor_select_16);
        ss32 = v.findViewById(R.id.sensor_select_32);
        ss64 = v.findViewById(R.id.sensor_select_64);
        ss128 = v.findViewById(R.id.sensor_select_128);

        sensor_tb_1 = ss1.findViewById(R.id.sensor_toggle_button);
        sensor_tb_2 = ss2.findViewById(R.id.sensor_toggle_button);
        sensor_tb_4 = ss4.findViewById(R.id.sensor_toggle_button);
        sensor_tb_8 = ss8.findViewById(R.id.sensor_toggle_button);
        sensor_tb_16 = ss16.findViewById(R.id.sensor_toggle_button);
        sensor_tb_32 = ss32.findViewById(R.id.sensor_toggle_button);
        sensor_tb_64 = ss64.findViewById(R.id.sensor_toggle_button);
        sensor_tb_128 = ss128.findViewById(R.id.sensor_toggle_button);

        sensor_tx_1 = ss1.findViewById(R.id.sensor_label);
        sensor_tx_1.setText("LIGHT");
        sensor_tx_2 = ss2.findViewById(R.id.sensor_label);
        sensor_tx_2.setText("HUMIDITY");
        sensor_tx_4 = ss4.findViewById(R.id.sensor_label);
        sensor_tx_4.setText("TEMPERATURE");
        sensor_tx_8 = ss8.findViewById(R.id.sensor_label);
        sensor_tx_8.setText("SOUND");
        sensor_tx_16 = ss16.findViewById(R.id.sensor_label);
        sensor_tx_16.setText("MOVEMENT");
        sensor_tx_32 = ss32.findViewById(R.id.sensor_label);
        sensor_tx_32.setText("TILT");
        sensor_tx_64 = ss64.findViewById(R.id.sensor_label);
        sensor_tx_64.setText("ULTA VIOLET");
        sensor_tx_128 = ss128.findViewById(R.id.sensor_label);
        sensor_tx_128.setText("INFRA RED");

        toggleButtons = new ToggleButton[]{sensor_tb_1,sensor_tb_2,sensor_tb_4,sensor_tb_8,sensor_tb_16,sensor_tb_32,sensor_tb_64,sensor_tb_128};

        sensor_im_1 = v.findViewById(R.id.sensor_status_1);
        sensor_im_2 = v.findViewById(R.id.sensor_status_2);
        sensor_im_4 = v.findViewById(R.id.sensor_status_4);
        sensor_im_8 = v.findViewById(R.id.sensor_status_8);
        sensor_im_16 = v.findViewById(R.id.sensor_status_16);
        sensor_im_32 = v.findViewById(R.id.sensor_status_32);
        sensor_im_64 = v.findViewById(R.id.sensor_status_64);
        sensor_im_128 = v.findViewById(R.id.sensor_status_128);

        imageViews = new ImageView[]{sensor_im_1, sensor_im_2,sensor_im_4,sensor_im_8,sensor_im_16,sensor_im_32,sensor_im_64,sensor_im_128};

        saveButton = v.findViewById(R.id.sensor_detail_save);

        //Defined toggle buttons
        int i = 0;
        for(ToggleButton t : toggleButtons){
            t.setChecked(sensor.getConfig()[i]);
            t.setOnCheckedChangeListener(onCheckedChangeListener(i));
            i++;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSensor();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
                RouterDevicesFragment nextFrag = RouterDevicesFragment.newInstance(router_id);
                fragmentTransaction.replace(R.id.main_content_pane, nextFrag,"sensor_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        updateSensor();
        return v;
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener(final int i){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("TOGGLE", "Toggling: " + sensor_values[i] + " : " + b);
                if(b){
                    //sensor.setConfig( sensor.getConfigInt() + sensor_values[i]);
                    sensor.setConfig(sensor_values[i]);
                }else{
                    //sensor.setConfig( sensor.getConfigInt() - sensor_values[i]);
                    sensor.setConfig(sensor_values[i]);
                }
                updateSensor();
            }
        };
    }

    private void saveSensor(){
        SetSensorsRestOperation setSensorsRestOperation = new SetSensorsRestOperation(getActivity());
        setSensorsRestOperation.Start(MainActivity.sensors, router_id);
    }

    public void updateSensor(){

        Boolean[] configBool = sensor.getConfig();
        int j = 0;
        for(Boolean b : configBool){
            if(b){
                imageViews[j].setBackgroundColor(Color.parseColor("#00FF00"));
                //toggleButtons[j].setChecked(true);
            }else{
                imageViews[j].setBackgroundColor(Color.parseColor("#666666"));
                //toggleButtons[j].setChecked(false);
            }
            j++;
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
