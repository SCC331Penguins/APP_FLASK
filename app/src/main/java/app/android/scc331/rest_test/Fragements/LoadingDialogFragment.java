package app.android.scc331.rest_test.Fragements;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import app.android.scc331.rest_test.R;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;


public class LoadingDialogFragment extends DialogFragment
{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle bundle = this.getArguments();
		String title = "Loading";
		if (bundle != null)
			title = bundle.getString("title");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.fragment_loading_dialog, null);
		v.setBackgroundColor(defaultTheme ? Color.parseColor("#A40E4C") :Color.parseColor("#212227"));
		((TextView)v.findViewById(R.id.loading_title)).setText(title);
		LottieAnimationView av = v.findViewById(R.id.loading_animation);
		av.setAnimation(R.raw.loading);
		av.playAnimation();
		builder.setView(v);
		AlertDialog d = builder.create();
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return d;
	}
}