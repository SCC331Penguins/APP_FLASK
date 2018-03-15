package app.android.scc331.rest_test.Fragements;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.ThreadLocalRandom;

import app.android.scc331.rest_test.R;

public class MainFragment extends Fragment {

View v ;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        TextView text = v.findViewById(R.id.tip);
        LottieAnimationView av = v.findViewById(R.id.animation_view);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        switch (randomNum)
        {
            case 1:
                av.setAnimation(R.raw.lamp);
                text.setText("Your SET toolkit can control the smart lights in your home.");
                break;
            case 2:
                av.setAnimation(R.raw.notification);
                text.setText("Your SET toolkit can arm and disarm the alarm in your home.");
                break;
            case 3:
                av.setAnimation(R.raw.skipping);
                text.setText("Your SET toolkit can detect motion in your home.");
                break;
            case 4:
                av.setAnimation(R.raw.thermometer3);
                text.setText("Your SET toolkit presents you with live data about temperature.");
                break;
            default:
                av.setAnimation(R.raw.thermometer3);
                text.setText("Your SET toolkit presents you with live data about temperature.");
                break;
        }
        av.playAnimation();
        return v;
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        TextView text = v.findViewById(R.id.tip);
        LottieAnimationView av = v.findViewById(R.id.animation_view);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        switch (randomNum)
        {
            case 1:
                av.setAnimation(R.raw.lamp);
                text.setText("Your SET toolkit can control the smart lights in your home.");
                break;
            case 2:
                av.setAnimation(R.raw.notification);
                text.setText("Your SET toolkit can arm and disarm the alarm in your home.");
                break;
            case 3:
                av.setAnimation(R.raw.skipping);
                text.setText("Your SET toolkit can detect motion in your home.");
                break;
            case 4:
                av.setAnimation(R.raw.thermometer3);
                text.setText("Your SET toolkit presents you with live data about temperature.");
                break;
            default:
                av.setAnimation(R.raw.thermometer3);
                text.setText("Your SET toolkit presents you with live data about temperature.");
                break;
        }
        av.playAnimation();
    }
}

