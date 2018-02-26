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
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.GaugeElement;

public class LiveDataFragment extends Fragment {

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

    private Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_data, container, false);

        Spinner spinner = v.findViewById(R.id.router_drop_down);
        spinner.setBackgroundColor(Color.parseColor("#777777"));

        router_element = v.findViewById(R.id.live_router_element);

        status_layout = v.findViewById(R.id.layout_status);

        requestLiveData = v.findViewById(R.id.request_live_data_button);

        status_text = v.findViewById(R.id.live_data_status_text);

        preLiveData = v.findViewById(R.id.pre_live_data_layout);

        liveDataLayout = v.findViewById(R.id.live_data_visual_panel);

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
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.router_spinner_item, router_names);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
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

                if (r.isOnline()) {
                    Drawable d = getResources().getDrawable(R.drawable.on_green);
                    status.setImageDrawable(d);
                    requestLiveData.setVisibility(View.VISIBLE);
                } else {
                    Drawable d = getResources().getDrawable(R.drawable.off_red);
                    status.setImageDrawable(d);
                    requestLiveData.setVisibility(View.GONE);
                }

                //TODO Remove this
                if(r.getId().equals("SCC33102_R01"))
                    requestLiveData.setVisibility(View.VISIBLE);

                    Log.d("ROUTER SELECT", r.toString());
                    GaugeElement gaugeElement = new GaugeElement(getContext(), selectedRouter);
                    liveDataLayout.addView(gaugeElement);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateResults(gaugeElement);
                        }
                    });
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

    private void updateResults(GaugeElement gaugeElement){

        int temperature = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        int humid = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        int light = ThreadLocalRandom.current().nextInt(0, 100 + 1);

        int sound = ThreadLocalRandom.current().nextInt(1400, 2400 + 1);
        int ir = ThreadLocalRandom.current().nextInt(1400, 2400 + 1);
        int uv = ThreadLocalRandom.current().nextInt(1400, 2400 + 1);

        boolean motion = ThreadLocalRandom.current().nextBoolean();

        gaugeElement.setTemperature(temperature);
        gaugeElement.setHumidity(humid);
        gaugeElement.setLight(light);

        gaugeElement.setMotion(motion);

        gaugeElement.setTilt(temperature, humid, light);

        gaugeElement.setSound(sound);
        gaugeElement.setIr(ir);
        gaugeElement.setUv(uv);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateResults(gaugeElement);
            }
        }, 500);
    }

    public static LiveDataFragment newInstance() {
        return (new LiveDataFragment());
    }

    public void updateData(JSONObject data){
        preLiveData.setVisibility(View.GONE);
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

    public interface LiveDataInteractionListener {
        void setStatusText(String text);
        void startLiveDataProcess(String router_id);
        void updateLiveData(JSONObject data);
    }
}
