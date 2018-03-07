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
	Button execute;
	String router_id;
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
	// TODO: Rename and change types and number of parameters
	public static ActuatorsDirectControlFragment newInstance(String router_id)
	{
		ActuatorsDirectControlFragment fragment = new ActuatorsDirectControlFragment();
		Bundle args = new Bundle();
		args.putString("router_id", router_id);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			router_id = getArguments().getString("router_id");
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
				System.out.println(actuator.getType());
				if(actuator.getType().equals("Lights"))
					actuatorsList.add(new ImageData(R.drawable.ic_light_sensor, "Smart Light"));
				else if(actuator.getType().equals("Kettle"))
					actuatorsList.add(new ImageData(R.drawable.ic_kettle, "Smart Kettle"));
				else if (actuator.getType().equals("Plug"))
					actuatorsList.add(new ImageData(R.drawable.ic_plug, "Plug"));
			}
			WheelImageAdapter wheelImageAdapter = new WheelImageAdapter(getActivity(), actuatorsList);
			//TODO FIX THIS ERROR
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
					Actuator actuator = actuators.get(actuatorsWheel.getSelectedPosition());

					APIActuatorControlRest apiActuatorControlRest = new APIActuatorControlRest(getActivity());
					apiActuatorControlRest.Start(actuators.get(actuatorsWheel.getSelectedPosition()).getId(),
							actionsList.get(actionsWheel.getSelectedPosition()).title, router_id);
				}
			});
		}

		// Inflate the layout for this fragment
		return view;
	}


	@Override
	public void onItemSelected(CursorWheelLayout parent, View view, int pos)
	{
		if(parent.getId() == R.id.wheel_actuators)
		{
			Actuator actuator =	actuators.get(parent.getSelectedPosition());
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
