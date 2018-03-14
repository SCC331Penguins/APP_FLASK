package app.android.scc331.rest_test.Fragements;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.SetArmRestOperation;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment
{

	public static boolean alarmIsArmed = true;

	private static final String ARG_PARAM1 = "router_id";

	private String router_id;

	public AlarmFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			router_id = getArguments().getString(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_alarm, container, false);
		((ToggleButton)view.findViewById(R.id.button_alarm)).setChecked(alarmIsArmed);

		view.findViewById(R.id.button_alarm).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				alarmIsArmed = !alarmIsArmed;
				((ToggleButton)view).setChecked(alarmIsArmed);

				SetArmRestOperation setArmRestOperation = new SetArmRestOperation(getActivity());
				setArmRestOperation.Start(router_id, alarmIsArmed);


			}
		});

		return view;
	}

	public static AlarmFragment newInstance(String router_id)
	{
		AlarmFragment fragment = new AlarmFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, router_id);
		fragment.setArguments(args);
		return fragment;
	}

}
