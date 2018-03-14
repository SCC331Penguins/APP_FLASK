package app.android.scc331.rest_test.RoomMaker;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.ZoneListener;
import app.android.scc331.rest_test.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomViewFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomViewFragement extends Fragment implements ZoneListener{

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String router_id;
    private FrameLayout frameLayout;

    private Button editButton;

    private ArrayList<FrameLayout> elements = new ArrayList<>();

    public RoomViewFragement() {
    }

    public static RoomViewFragement newInstance(String param1, String param2) {
        RoomViewFragement fragment = new RoomViewFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            router_id = getArguments().getString(ARG_PARAM1);
        }
    }

    private List<String> router_names = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_room_view_fragement, container, false);

        Spinner spinner = v.findViewById(R.id.router_drop_down);
        spinner.setBackgroundColor(Color.parseColor("#777777"));

        frameLayout = v.findViewById(R.id.room_show_panel);

        editButton = v.findViewById(R.id.edit_room_layout);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
                MapDesignFragment nextFrag = MapDesignFragment.newInstance(router_id);
                fragmentTransaction.replace(R.id.main_content_pane, nextFrag, "map_design_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        int i = 1;
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
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("CLICK", "" + i);
                router_id = MainActivity.routers.get(i).getId();
                ArrayList<ElementData> elementData = MainActivity.savedState.getElements(router_id);
                RouterSensorElementData routerSensorElementData = MainActivity.savedState.getRouterSensorElements(router_id);
                ArrayList<RouterSensorElementData> sensorElementData = MainActivity.savedState.getSensorElements(router_id);

                for(FrameLayout element : elements){
                    frameLayout.removeView(element);
                }

                elements.clear();


                if (elementData != null){
                    for (ElementData element : elementData) {
                        Log.d("ELEMENT LOAD", "Loading element: " + element.label_name);
                        Element e = Element.initFromSave(getActivity(), element);
                        elements.add(e);
                        frameLayout.addView(e);
                    }
                }
                if (routerSensorElementData != null) {
                    RouterElement routerElement = RouterElement.loadFromSave(getActivity(), routerSensorElementData);
                    elements.add(routerElement);
                    frameLayout.addView(routerElement);
                }
                if (sensorElementData != null)
                {
                    for (RouterSensorElementData rsd : sensorElementData) {
                        SensorElement se = SensorElement.loadFromSave(getActivity(), rsd);
                        elements.add(se);
                        frameLayout.addView(se);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        frameLayout.addView(new RouterElement(getActivity(),""));

        MainActivity.currentZone.setZoneListener(this);

        return v;
    }

    @Override
    public void onZoneChange(String zone){
        for(FrameLayout e : elements){
            e.setBackgroundColor(Color.WHITE);
            Log.d("FRAMELAYOUT", e.toString());
            if(e instanceof Element){
                Element a = (Element)e;
                String room = ((Element) e).getlabel();
                String sensor = MainActivity.savedState.getSensorRoom(room);
                Log.d("ELEMENT", "Zone: " + zone + " Room: " + room + " Sensor: " + sensor );
                if(sensor != null){
                    if(sensor.contains(zone))
                        e.setBackgroundColor(Color.CYAN);
                }
            }
        }
    }
}
