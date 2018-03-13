package app.android.scc331.rest_test.Fragements;

import android.app.Fragment;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.LiveData.Elements.Command;
import app.android.scc331.rest_test.Services.SetButtonRestOperation;
import app.android.scc331.rest_test.Util.MenuItemData;
import app.android.scc331.rest_test.Util.WheelTextAdapter;
import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActuatorsDirectControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InternetButtonFragment extends Fragment
{
	ArrayList<Actuator> actuatorsList = MainActivity.actuators;
	CursorWheelLayout internetButtonWheel;
	List<MenuItemData> buttonsList;
	Actuator actuator1, actuator2, actuator3, actuator4;
	Command command1, command2, command3, command4;
	Button bind;
	boolean bound1, bound2, bound3, bound4;
	public InternetButtonFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment ActuatorsDirectControlFragment.
	 */
	public static InternetButtonFragment newInstance()
	{
		InternetButtonFragment fragment = new InternetButtonFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_internet_button, container, false);
		bind = v.findViewById(R.id.bind_button);
		internetButtonWheel = v.findViewById(R.id.wheel_internet_button);
		Spinner actions = v.findViewById(R.id.actions_spinner);
		Spinner actuators = v.findViewById(R.id.actuators_spinner);
		if(actuatorsList != null)
		{
			ArrayList<String> actuatorNames = new ArrayList<>();
			for (int i = 0; i < actuatorsList.size(); i++)
			{
				actuatorNames.add(actuatorsList.get(i).getType());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, actuatorNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			actuators.setAdapter(adapter);
			actuators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
				{
					ArrayAdapter<String> actionsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, actuatorsList.get(i).getFunctions());
					actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					actions.setAdapter(actionsAdapter);
					actionsAdapter.notifyDataSetChanged();

				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView)
				{
				}
			});
		}


		internetButtonWheel.setAdapter(new CursorWheelLayout.CycleWheelAdapter()
		{
			@Override
			public int getCount()
			{
				return 4;
			}

			@Override
			public View getView(View parent, int position)
			{
				TextView view = new TextView(getActivity());
				view.setText("Button " + (position + 1));
				view.setGravity(Gravity.CENTER);
				view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
				return view;
			}

			@Override
			public Object getItem(int position)
			{
				return position;
			}
		});



		bind.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if(!actuatorsList.isEmpty())
					{
						switch (internetButtonWheel.getSelectedPosition())
						{
							case 0:
								bound1 = true;
								v.findViewById(R.id.button_1).setBackgroundResource(R.drawable.on_green);
								actuator1 = actuatorsList. get(actuators.getSelectedItemPosition() );
								command1 = new Command(actuator1, actuator1.getFunctions().get(actions.getSelectedItemPosition()));
								break;
							case 1:
								bound2 = true;
								v.findViewById(R.id.button_2).setBackgroundResource(R.drawable.on_green);
								actuator2 = actuatorsList. get(actuators.getSelectedItemPosition() );
								command2 = new Command(actuator2, actuator2.getFunctions().get(actions.getSelectedItemPosition()));
								break;
							case 2:
								bound3 = true;
								v.findViewById(R.id.button_3).setBackgroundResource(R.drawable.on_green);
								actuator3 = actuatorsList. get(actuators.getSelectedItemPosition() );
								command3 = new Command(actuator3, actuator3.getFunctions().get(actions.getSelectedItemPosition()));
								break;
							case 3:
								bound4 = true;
								v.findViewById(R.id.button_4).setBackgroundResource(R.drawable.on_green);
								actuator4 = actuatorsList. get(actuators.getSelectedItemPosition() );
								command4 = new Command(actuator4, actuator4.getFunctions().get(actions.getSelectedItemPosition()));
								break;
						}
					}
				}
			});
		// Inflate the alarm_toggle_states for this fragment
		v.findViewById(R.id.internet_button_confirm).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if( bound1 && bound2 && bound3 && bound4)
				{
					new SetButtonRestOperation(getActivity()).Start(RouterDevicesFragment.router_id, command1, command2, command3, command4);
				}
				else
				{
					Toast.makeText(getActivity(), "You need to bind all buttons first!", Toast.LENGTH_LONG).show();
				}
			}
		});
		return v;
	}
}
