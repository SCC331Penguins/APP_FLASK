package app.android.scc331.rest_test.Fragements;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import app.android.scc331.rest_test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment
{

	public static boolean alarmIsArmed = true;

	public AlarmFragment()
	{
		// Required empty public constructor
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


			}
		});

		return view;
	}

	public static AlarmFragment newInstance()
	{
		AlarmFragment fragment = new AlarmFragment();
		return fragment;
	}

}
