package app.android.scc331.rest_test.Fragements;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.APIActuatorControlRest;
import app.android.scc331.rest_test.Util.ImageData;
import app.android.scc331.rest_test.Util.MenuItemData;
import app.android.scc331.rest_test.Util.WheelImageAdapter;
import app.android.scc331.rest_test.Util.WheelTextAdapter;
import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActuatorsDirectControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActuatorsDirectControlFragment extends Fragment implements CursorWheelLayout.OnMenuSelectedListener
{

	CursorWheelLayout actuatorsWheel, actionsWheel;
	List<MenuItemData> actionsList;
	List<ImageData> actuatorsList;
	ArrayList<Actuator> actuators = MainActivity.actuators;
	ArrayList<Actuator> actuatorsReal = new ArrayList<>();
	Button execute;


	private static final String ARG_PARAM1 = "param1";

	private String router_id;

	public ActuatorsDirectControlFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment ActuatorsDirectControlFragment.
	 */
	public static ActuatorsDirectControlFragment newInstance(String router_id)
	{
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, router_id);
		ActuatorsDirectControlFragment fragment = new ActuatorsDirectControlFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			router_id = getArguments().getString(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_actuators_direct_control, container, false);
		execute = view.findViewById(R.id.executeButton);
		actionsList = new ArrayList<>();
		actuatorsList = new ArrayList<>();
		actionsWheel = view.findViewById(R.id.wheel_actions);
		actuatorsWheel = view.findViewById(R.id.wheel_actuators);

		if(MainActivity.actuators != null)
		{
			actuators = MainActivity.actuators;
			for (int i = 0; i < actuators.size(); i++)
			{
				Actuator actuator = actuators.get(i);
				if(actuator.getType().equals("Lights"))
				{
					actuatorsReal.add(actuator);
					actuatorsList.add(new ImageData(R.drawable.ic_light_sensor, "Smart Light"));
				}
				else if(actuator.getType().equals("Kettle"))
				{
					actuatorsReal.add(actuator);
					actuatorsList.add(new ImageData(R.drawable.ic_kettle, "Smart Kettle"));
				}
				else if (actuator.getType().equals("Plug"))
				{
					actuatorsReal.add(actuator);
					actuatorsList.add(new ImageData(R.drawable.ic_plug, "Plug"));
				}
			}
			WheelImageAdapter wheelImageAdapter = new WheelImageAdapter(getActivity(), actuatorsList);
			actuatorsWheel.setAdapter(wheelImageAdapter);



			actuatorsWheel.setSelection(1);
			actuatorsWheel.setOnMenuSelectedListener(this);

			execute.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Toast.makeText(getActivity(), "Action executed.", Toast.LENGTH_LONG).show();
					//MainActivity.mqttConnection.sendCommand(actuators.get(actuatorsWheel.getSelectedPosition()), actionsList.get(actionsWheel.getSelectedPosition()).title);

					APIActuatorControlRest apiActuatorControlRest = new APIActuatorControlRest(getActivity());
					apiActuatorControlRest.Start(actuators.get(actuatorsWheel.getSelectedPosition()).getId(), actionsList.get(actionsWheel.getSelectedPosition()).title, router_id);
					Toast.makeText(getActivity(), "Action executed.", Toast.LENGTH_LONG).show();
				}
			});
		}

		// Inflate the alarm_toggle_states for this fragment
		return view;
	}


	@Override
	public void onItemSelected(CursorWheelLayout parent, View view, int pos)
	{
		if(parent.getId() == R.id.wheel_actuators && parent.getSelectedPosition() != -1)
		{
			Actuator actuator =	actuatorsReal.get(parent.getSelectedPosition());
			actionsList.clear();
			for (int j = 0; j < actuator.getFunctions().size(); j++)
			{
				actionsList.add(new MenuItemData(actuator.getFunctions().get(j)));
			}
			WheelTextAdapter adapter = new WheelTextAdapter(getActivity(), actionsList);
			actionsWheel.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			actionsWheel.invalidate();
		}
	}
}
