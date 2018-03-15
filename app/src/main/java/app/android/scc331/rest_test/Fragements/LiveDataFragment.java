package app.android.scc331.rest_test.Fragements;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.GaugeElement;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveData;
import app.android.scc331.rest_test.Services.LiveData.Elements.SpinnerSensorListener;

public class LiveDataFragment extends Fragment implements SpinnerSensorListener{

    private LiveDataInteractionListener mListener;

    private List<String> router_names = new ArrayList<>();

    private int selected;

    private Router selectedRouter;

    private TextView status_text;

    private LinearLayout router_element;

    private LinearLayout status_layout;

    private Button requestLiveData;

    private LinearLayout preLiveData;

    private LinearLayout liveDataLayout;

    private ScrollView liveDataLayoutScroll;

    private Handler handler = new Handler();

    private GaugeElement dataDashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_data, container, false);

        Spinner spinner = v.findViewById(R.id.router_drop_down);

        router_element = v.findViewById(R.id.live_router_element);

        status_layout = v.findViewById(R.id.layout_status);

        requestLiveData = v.findViewById(R.id.request_live_data_button);

        status_text = v.findViewById(R.id.live_data_status_text);

        preLiveData = v.findViewById(R.id.pre_live_data_layout);

        liveDataLayout = v.findViewById(R.id.live_data_visual_panel);

        liveDataLayoutScroll = v.findViewById(R.id.live_data_visual_panel_scroll);

        requestLiveData.setVisibility(View.GONE);
        status_layout.setVisibility(View.GONE);

        if(MainActivity.routers != null)
            for (Router router : MainActivity.routers) {
                String name = MainActivity.savedState.getRouterName(router.getId());
                if (name == null) {
                    router_names.add(router.getId());
                } else {
                    router_names.add(name);
                }
            }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.router_spinner_item, router_names);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Router r = MainActivity.routers.get(i);
                selectedRouter = r;
                TextView id = router_element.findViewById(R.id.text_router_id);
                TextView name = router_element.findViewById(R.id.text_router_name);
                ImageView status = router_element.findViewById(R.id.router_status_image);

                id.setText(r.getId());
                name.setText(router_names.get(i));

                MainActivity.mqttConnection.unsubscribe();

                if(dataDashboard != null){
                    liveDataLayout.removeView(dataDashboard);
                }
                dataDashboard = null;

                status_layout.setVisibility(View.GONE);
                preLiveData.setVisibility(View.VISIBLE);
                liveDataLayout.setVisibility(View.GONE);

                if (r.getId().equals("SCC33102_R01")) {
                    Drawable d = getResources().getDrawable(R.drawable.on_green);
                    status.setImageDrawable(d);
                    requestLiveData.setVisibility(View.VISIBLE);
                } else {
                    Drawable d = getResources().getDrawable(R.drawable.off_red);
                    status.setImageDrawable(d);
                    requestLiveData.setVisibility(View.GONE);
                }

                Log.d("ROUTER SELECT", r.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        requestLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_layout.setVisibility(View.VISIBLE);
                preLiveData.setVisibility(View.VISIBLE);
                mListener.startLiveDataProcess(selectedRouter.getId());
            }
        });
        return v;
    }

    public void setNewDate(String text, long utc, int type){
        if (dataDashboard != null){
            dataDashboard.setNewDate(text,utc,type);
        }
    }

    public static LiveDataFragment newInstance() {
        return (new LiveDataFragment());
    }

    public void updateData(HashMap<String, ArrayList<LiveData>> data){
        if(dataDashboard == null) {
            dataDashboard = new GaugeElement(getActivity(), selectedRouter);
            liveDataLayout.addView(dataDashboard);
            liveDataLayout.setVisibility(View.VISIBLE);
            preLiveData.setVisibility(View.GONE);
            dataDashboard.setSpinnerSensorListener(this);
        }
        dataDashboard.setData(data);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LiveDataInteractionListener) {
            mListener = (LiveDataInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LiveDataInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setStatusText(String text){
        status_text.setText(text);
    }

    @Override
    public void onNewSelected(String sensor_id) {
        mListener.getData(sensor_id);
    }

    public interface LiveDataInteractionListener {
        void setStatusText(String text);
        void startLiveDataProcess(String router_id);
        void getData(String sensor_id);
        void updateLiveData(JSONObject data);
    }
}
