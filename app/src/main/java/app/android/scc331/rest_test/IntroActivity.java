package app.android.scc331.rest_test;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;

public class IntroActivity  extends AppIntro
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addSlide(AppIntroFragment.newInstance("Smart Environment Toolkit", "Welcome to the SET app.", R.drawable.ic_photon_web, defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227")));
		addSlide(AppIntroFragment.newInstance("Automatic configuration", "Your SET sensors will automatically be configured once you have added your router details on the routers tab.", R.drawable.router, defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227")));
		addSlide(AppIntroFragment.newInstance("Smart device control", "Access all smart devices and sensors on your network from the routers tab.", R.drawable.smart, defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227")));
		addSlide(AppIntroFragment.newInstance("Statistical breakdowns", "View a breakdown of different data gathered from your home from the Live tab.", R.drawable.stats, defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227")));
		addSlide(AppIntroFragment.newInstance("Data", "Set up the data to be tracked by your sensors and control your actuators from the routers tab.", R.drawable.ic_data, defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227")));
		setProgressButtonEnabled(true);
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
	}

	@Override
	public void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
	}

}
