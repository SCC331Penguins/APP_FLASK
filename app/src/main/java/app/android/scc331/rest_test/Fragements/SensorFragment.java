package app.android.scc331.rest_test.Fragements;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetActuatorRestOperation;
import app.android.scc331.rest_test.Services.GetSensorRestOperation;
import app.android.scc331.rest_test.Services.SetScriptRestOperation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private String router_id;
    private String TAG = "SENSOR";

    private TextView routername;

    private ListView listView;

    private ArrayList<Sensor> sensors;

    private Button add_script;

    public SensorFragment() {
        // Required empty public constructor
    }

    public static SensorFragment newInstance(String router_id) {
        SensorFragment fragment = new SensorFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor, container, false);
        listView = v.findViewById(R.id.sensor_list_view);

        GetSensorRestOperation getSensorRestOperation = new GetSensorRestOperation(getContext());
        sensors = (ArrayList<Sensor>) getSensorRestOperation.Start(router_id);
        MainActivity.sensors = sensors;

        GetActuatorRestOperation getActuatorRestOperation = new GetActuatorRestOperation(getContext());
        MainActivity.actuators = (ArrayList<Actuator>) getActuatorRestOperation.Start(router_id);

        routername = v.findViewById(R.id.router_sensor_name);
        routername.setText(MainActivity.savedState.getRouterName(router_id));
        add_script = v.findViewById(R.id.add_script_button);


        add_script.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetScriptRestOperation setScriptRestOperation = new SetScriptRestOperation(getContext());
                String encodeURL = null;
                try {
                    encodeURL = URLEncoder.encode( "print('hi script')", "UTF-8" );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                setScriptRestOperation.Start(encodeURL, router_id);
                //TODO SCRIPT STUFFFF
            }
        });

        if(sensors !=null) {
            Sensor[] sensorToArray = new Sensor[sensors.size()];
            sensorToArray = sensors.toArray(sensorToArray);
            SensorListAdapter sensorListAdapter = new SensorListAdapter(getActivity(), sensorToArray);
            listView.setAdapter(sensorListAdapter);
        }
        return v;
    }

    class SensorListAdapter extends ArrayAdapter<Sensor>{

        private Sensor[] sensors;
        private Context context;

        public SensorListAdapter(@NonNull Context context, Sensor[] sensors) {
            super(context, R.layout.sensor_list_item, sensors);
            this.sensors = sensors;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.sensor_list_item, parent, false);

            TextView name, id;
            ImageView sensorim1, sensorim2, sensorim4, sensorim8, sensorim16, sensorim32, sensorim64, sensorim128;

            String name_ss = MainActivity.savedState.getSensorName(sensors[position].getId());

            name = row.findViewById(R.id.text_sensor_name);
            id = row.findViewById(R.id.text_sensor_id);

            sensorim1 = row.findViewById(R.id.sensor_status_1);
            sensorim2 = row.findViewById(R.id.sensor_status_2);
            sensorim4 = row.findViewById(R.id.sensor_status_4);
            sensorim8 = row.findViewById(R.id.sensor_status_8);
            sensorim16 = row.findViewById(R.id.sensor_status_16);
            sensorim32 = row.findViewById(R.id.sensor_status_32);
            sensorim64 = row.findViewById(R.id.sensor_status_64);
            sensorim128 = row.findViewById(R.id.sensor_status_128);


            ImageView[] imageViews = new ImageView[]{sensorim1, sensorim2, sensorim4, sensorim8, sensorim16, sensorim32, sensorim64, sensorim128};

            Boolean[] status = sensors[position].getConfig();

            name.setText(name_ss);
            id.setText(sensors[position].getId());

            int i = 0;
            for(Boolean b : status){
                if(b){
                    imageViews[i].setBackgroundColor(Color.parseColor("#00FF00"));
                }else{
                    imageViews[i].setBackgroundColor(Color.parseColor("#666666"));
                }
                i++;
            }

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SensorDetailsFragment nextFrag = SensorDetailsFragment.newInstance(sensors[position].getId(), router_id);
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.main_content_pane, nextFrag,"sensor_detail_fragment")
                            .addToBackStack(null)
                            .commit();
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    final View vw = layoutInflater.inflate(R.layout.change_name_dialog, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setCancelable(false);
                    final EditText editname_text = (EditText) vw.findViewById(R.id.editname_edittext);
                    final Button save = vw.findViewById(R.id.save_button_dialog);
                    final Button cancel = vw.findViewById(R.id.cancel_button_dialog);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.savedState.saveSensor(sensors[position].getId(), editname_text.getText().toString());
                            MainActivity.savedState.save(getContext());
                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });


                    alertDialog.setView(vw);
                    alertDialog.show();

                    return false;
                }
            });

            return row;
        }
    }

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
