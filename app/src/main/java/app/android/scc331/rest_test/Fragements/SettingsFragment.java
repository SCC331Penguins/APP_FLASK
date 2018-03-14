package app.android.scc331.rest_test.Fragements;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.android.scc331.rest_test.LoginActivity;
import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.R;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment
{



	public SettingsFragment()
	{
		// Required empty public constructor
	}


	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();

		fragment.setArguments(args);
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
		View v = inflater.inflate(R.layout.fragment_settings, container, false);
		// Inflate the layout for this fragment
		v.findViewById(R.id.change_theme).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

				SharedPreferences.Editor e = getPrefs.edit();

				if(defaultTheme)
				{
					e.putBoolean("defaultTheme", false);
					LoginActivity.defaultTheme = false;
				}
				else
				{
					e.putBoolean("defaultTheme", true);
					LoginActivity.defaultTheme = true;
				}
				//  Apply changes
				e.apply();
				Intent i = new Intent(getActivity(), MainActivity.class);
				startActivity(i);

			}
		});
		return v;
	}

}
