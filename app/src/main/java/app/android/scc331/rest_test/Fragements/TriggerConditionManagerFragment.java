package app.android.scc331.rest_test.Fragements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.TriggerCondition;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.SetScriptRestOperation;


/**
 * Created by Nikola on 14/02/2018.
 */

public class TriggerConditionManagerFragment extends Fragment implements ServiceDialogFragment.NoticeDialogListener {

    static ArrayList<TriggerCondition> conditions = new ArrayList<>();
    ArrayList<String> actuators = new ArrayList<>();
    private String triggerConditionString= "";
    TextView tv;
    public static String router_id;

    String actuator;
    String feedback ;
    private ListView tcList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_trigger_condition_manager, container, false);
        Bundle bundle = this.getArguments();
        tcList = view.findViewById(R.id.tc_list_view);
        //displayList();
        if (bundle != null)
        {
            router_id = bundle.getString("router_id", "o");
            System.out.println(router_id);
        }
        view.findViewById(R.id.addTriggerConditionButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(conditions.isEmpty())
                    ((TriggerConditionsMainFragment)getParentFragment()).replaceFragment(new TriggerConditionBuilderFragment());
                else
                {
                    registerForContextMenu(v);
                    v.showContextMenu();
                }
            }
        });

        view.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SetScriptRestOperation setScriptRestOperation = new SetScriptRestOperation(getContext());
                String encodeURL = null;
                try {
                    encodeURL = URLEncoder.encode( triggerConditionString, "UTF-8" );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                setScriptRestOperation.Start(encodeURL, router_id);
            }
        });

        view.findViewById(R.id.serviceButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ServiceDialogFragment newFragment = new ServiceDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray("values", new String[]{"Smart Lights"});
                bundle.putString("title", "Select a service");
                newFragment.setArguments(bundle);
                newFragment.show(getChildFragmentManager(), "service");
            }
        });

        view.findViewById(R.id.addActuator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.actuators != null)
                {

                    ServiceDialogFragment newFragment = new ServiceDialogFragment();
                    Bundle bundle = new Bundle();

                    for (int i = 0; i < MainActivity.actuators.size(); i++) {
                        actuators.add(MainActivity.actuators.get(i).getType());
                    }
                    bundle.putStringArray("values",Arrays.copyOf(actuators.toArray(), actuators.toArray().length, String[].class));
                    bundle.putString("title", "Select an actuator");
                    newFragment.setArguments(bundle);
                    newFragment.show(getChildFragmentManager(), "service");
                }

            }
        });

        tv = view.findViewById(R.id.tv);
        tv.setText(feedback);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        return view;
    }

    public void update(ArrayList<TriggerCondition> updatedConditions)
    {

        conditions = updatedConditions;
        triggerConditionString = "while True:\n" +
                "    if(";
        for (int i = 0; i < conditions.size(); i++)
        {
            TriggerCondition tc = conditions.get(i);
            if(i!=0) {
                triggerConditionString += " " + tc.logicalOperator + " ";
                if(tc.logicalOperator != null)
                    feedback += tc.logicalOperator + " ";
                if (feedback.contains("null"))
                    feedback = "";
            }
            else
            {
                    feedback += " if ";
            }
            feedback += "sensor " + tc.sensorName + " " + tc.metric + " " + tc.relationalOperator + tc.threshold + "\n";
            triggerConditionString += "sensors[\"" + tc.sensorName + "\"].";
            triggerConditionString += tc.metric + tc.relationalOperator + tc.threshold;
        }
        triggerConditionString += "):\n" +
                "        print (\"" + actuator+"\")\n" +
                "        time.sleep(3)";
        if(tv != null)
            if(feedback != null)
                tv.setText(feedback);

    }


    public void onResume() {
        super.onResume();
        //displayList();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select logical relation between conditions");
        //groupId, itemId, order, title
        menu.add(ContextMenu.NONE, 0, 0, "and");
        menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "or");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Bundle bundle = new Bundle();
        bundle.putString("operator", item.getTitle().toString());
        TriggerConditionBuilderFragment TriggerConditionBuilderFragment = new TriggerConditionBuilderFragment();
        TriggerConditionBuilderFragment.setArguments(bundle);
        ((TriggerConditionsMainFragment)getParentFragment()).replaceFragment(TriggerConditionBuilderFragment);
        return true;
    }


    @Override
    public void onDialogClick(int mode, String result) {

        if(mode == 0)
        {
            ServiceDialogFragment newFragment = new ServiceDialogFragment();
            Bundle bundle = new Bundle();
            ArrayList<String> sensors = new ArrayList<>();
            if(MainActivity.sensors != null) {
                for (int i = 0; i < MainActivity.sensors.size(); i++) {
                    if(MainActivity.sensors.get(i).getConfig()[0])
                        sensors.add(MainActivity.sensors.get(i).getId());
                }
            }
            bundle.putStringArray("values", Arrays.copyOf(sensors.toArray(), sensors.toArray().length, String[].class));
            bundle.putString("title", "Choose a sensor to use for service");
            newFragment.setArguments(bundle);
            newFragment.show(getChildFragmentManager(), "service");
        }
        else if (mode == 1)
        {
            String triggerConditionString1 = "while True:\n" +
                    "    if(";
            triggerConditionString1 += "sensors[\"" + result + "\"].";
            triggerConditionString1 += "light" + "<" + "30";

            triggerConditionString1 += "):\n" +
                    "        print (\"lights turn on\")\n" +
                    "        time.sleep(3)";

            SetScriptRestOperation setScriptRestOperation = new SetScriptRestOperation(getContext());
            String encodeURL = null;
            try {
                encodeURL = URLEncoder.encode( triggerConditionString1, "UTF-8" );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), triggerConditionString1, Toast.LENGTH_LONG).show();
            setScriptRestOperation.Start(encodeURL, router_id);
        }
        else if (mode == 2)
        {
            feedback += " actuator " + result + " triggers\n";
            tv.setText(feedback);
        }

    }
    public void displayList(){

        TriggerConditionListAdapter routerListAdapter = new TriggerConditionListAdapter(getActivity());
            tcList.setAdapter(routerListAdapter);
            routerListAdapter.notifyDataSetChanged();
        }


    class TriggerConditionListAdapter extends ArrayAdapter<Router>{

        ArrayList<TriggerCondition> conditions;
        Context context;

        public TriggerConditionListAdapter(@NonNull Context context) {
            super(context, R.layout.router_list_item);
            this.conditions = TriggerConditionManagerFragment.conditions;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.trigger_condition_item, parent, false);

            TextView id = row.findViewById(R.id.sensor_id);
            TextView metric = row.findViewById(R.id.metric_name);
            TextView comparison = row.findViewById(R.id.tc_condtion);

            final TriggerCondition triggerCondition = conditions.get(position);

            id.setText(triggerCondition.sensorName);
            metric.setText(triggerCondition.metric);
            comparison.setText(triggerCondition.relationalOperator + triggerCondition.threshold);


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


            return row;
        }
    }
}



