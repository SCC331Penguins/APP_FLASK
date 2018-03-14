package app.android.scc331.rest_test;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity  extends AppIntro
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//addSlide(AppIntroFragment.newInstance("Smart Environment Toolkit", "Welcome to the SET app", image, backgroundColor));

		// OPTIONAL METHODS
		// Override bar/separator color.
		//setBarColor(Color.parseColor("#3F51B5"));
		//setSeparatorColor(Color.parseColor("#2196F3"));

		// Hide Skip/Done button.
		showSkipButton(false);
		setProgressButtonEnabled(false);
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		// Do something when users tap on Skip button.
	}

	@Override
	public void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		// Do something when users tap on Done button.
	}

}
