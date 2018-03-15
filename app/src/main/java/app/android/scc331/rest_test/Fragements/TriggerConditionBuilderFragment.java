package app.android.scc331.rest_test.Fragements;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.TriggerCondition;
import app.android.scc331.rest_test.R;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;


/**
 * Created by Nikola on 14/02/2018.
 */

public class TriggerConditionBuilderFragment extends Fragment
{
    public interface OnSubmitListener {
        public void onSubmitClicked(TriggerCondition triggerCondition);
    }

    private String logicalOperator;
    private OnSubmitListener callback;
    private LinearLayout layout;
    private View view;
    private HashMap<String,ArrayList> sensorsMetrics;
    private TriggerCondition triggerCondition;
    private boolean edit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            triggerCondition = (TriggerCondition) bundle.getSerializable("tc");
        }


        view = inflater.inflate(R.layout.fragment_trigger_condition_builder, container, false);
        layout = view.findViewById(R.id.builderLayout);
        sensorsMetrics = new HashMap<>();


        addLabel("Select sensor for trigger condition:");


        ArrayList<String> sensors = new ArrayList<>();
        if(MainActivity.sensors != null)
        {
            for (int i = 0; i < MainActivity.sensors.size(); i++)
            {
                sensors.add(MainActivity.sensors.get(i).getId());
                ArrayList<String> metrics = new ArrayList<>();
                Boolean[] config = MainActivity.sensors.get(i).getConfig();
                for (int j = 0; j < config.length; j++)
                {
                    if(config[j])
                    {
                        switch (j)
                        {
                            //{LIGHT, HUMID, TEMP, SOUND, MOTION, TILT, UV, IDK};
                            case 0:
                                metrics.add("light");
                                break;
                            case 1:
                                metrics.add("temperature");
                                break;
                            case 2:
                                metrics.add("humidity");
                                break;
                            case 3:
                                metrics.add("sound");
                                break;
                            case 4:
                                metrics.add("uv");
                                break;
                            case 5:
                                metrics.add("ir");
                                break;
                            case 6:
                                metrics.add("motion");
                                break;
                            case 7:
                                metrics.add("tilt");
                                break;
                        }
                    }
            }
            sensorsMetrics.put(MainActivity.sensors.get(i).getId(), metrics );
          }
        }

        final Spinner sensorsSpinner = addSpinner(sensors);

        addLabel("Select metric for trigger condition:");

        final Spinner metricsSpinner = addSpinner(new ArrayList<String>());
        sensorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                updateSpinnerValues(sensorsMetrics.get(parent.getItemAtPosition(position).toString()), metricsSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        addLabel("Enter threshold:");
        final EditText threshold = addTextField(InputType.TYPE_CLASS_NUMBER, "Metric threshold number");
        addLabel("Select relational operator:");
        final Spinner operatorsSpinner = addSpinner(Arrays.asList("<", ">", "="));
        Button submitButton = new Button(getActivity());
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	int index = -1;
                if (edit)
					index = TriggerConditionManagerFragment.conditions.indexOf(triggerCondition);
                triggerCondition.threshold = threshold.getText().toString();
                if(operatorsSpinner.getSelectedItem().toString().equals("="))
                    triggerCondition.relationalOperator = "==";
                else
                    triggerCondition.relationalOperator = operatorsSpinner.getSelectedItem().toString();
                triggerCondition.metric = metricsSpinner.getSelectedItem().toString();
                triggerCondition.sensorName = sensorsSpinner.getSelectedItem().toString();
				if(edit)
				{
					TriggerConditionManagerFragment.conditions.set(index, triggerCondition);
					callback.onSubmitClicked(null);
					return;
				}
				callback.onSubmitClicked(triggerCondition);
            }
        });
        layout.addView(submitButton);
        if(triggerCondition == null)
            triggerCondition = new TriggerCondition();
        else
        {
            edit = true;
        }
        return view;
    }

    private Spinner addSpinner(List<String> values)
    {
        final Spinner spinner = new Spinner(getActivity());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, values);
        dataAdapter.setDropDownViewResource(R.layout.item);
        spinner.setAdapter(dataAdapter);
        layout.addView(spinner);
        return spinner;
    }

    private TextView addLabel(String text)
    {
        final TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
        tv.setText(text);
        tv.setTextSize(18);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(tv);
        return tv;
    }

    private Spinner updateSpinnerValues(List<String> newValues, Spinner spinner)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, newValues);
        spinner.setAdapter(dataAdapter);
        spinner.setDrawingCacheBackgroundColor(Color.parseColor("#FFFFFF"));
        dataAdapter.notifyDataSetChanged();
        return spinner;
    }

    private EditText addTextField(int type, String hint)
    {
        final EditText editText = new EditText(getActivity());
        editText.setInputType(type);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
        editText.setHint(hint);
        layout.addView(editText);
        editText.setHintTextColor(Color.parseColor("#FFFFFF"));
        editText.setTextColor(Color.parseColor("#FFFFFF"));

        return editText;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            callback = (OnSubmitListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitClicked");
        }
    }
}
