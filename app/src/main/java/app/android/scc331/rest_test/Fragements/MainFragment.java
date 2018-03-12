package app.android.scc331.rest_test.Fragements;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Objects.Graphs.LineChartElement;
import app.android.scc331.rest_test.Objects.HistoricData;
import app.android.scc331.rest_test.Objects.HistoricDataSet;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.RoomMaker.CustomZoomableImageView;
import app.android.scc331.rest_test.Services.GetHistoricDataRestOperation;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.Command;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveDataGraph;
import app.android.scc331.rest_test.Services.SetButtonRestOperation;

public class MainFragment extends Fragment {

    private Button startLiveData;
    private Button stopLiveData;
    private TextView liveData;

    private String channel;

    private LiveDataGraph liveDataGraph;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        startLiveData = v.findViewById(R.id.request_live_data);

        startLiveData.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogFragment newFragment = DatePicker.newInstance(1);
                newFragment.show(getFragmentManager(), "datePicker");
                return true;
            }
        });

        stopLiveData = v.findViewById(R.id.stop_live_data);

        stopLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actuator actuator = new Actuator("AA:BB:CC", "Lights", "setAllOn");

                SetButtonRestOperation setButtonRestOperation = new SetButtonRestOperation(getActivity());
                setButtonRestOperation.Start("SCC33102_R01",new Command(actuator, "setAllOn"),
                        new Command(actuator, "setAllOn"), new Command(actuator, "setAllOn"),
                        new Command(actuator, "setAllOff"));
                ;
            }
        });
        liveData = v.findViewById(R.id.live_data_text);

        return v;
    }



    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}

