package app.android.scc331.rest_test.Fragements;

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

import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Graphs.LineChartElement;
import app.android.scc331.rest_test.Objects.HistoricData;
import app.android.scc331.rest_test.Objects.HistoricDataSet;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.RoomMaker.CustomZoomableImageView;
import app.android.scc331.rest_test.Services.GetHistoricDataRestOperation;
import app.android.scc331.rest_test.Services.GetNewChannelRestOperation;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;

public class MainFragment extends Fragment {

    private Button startLiveData;
    private Button stopLiveData;
    private TextView liveData;

    private String channel;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        startLiveData = v.findViewById(R.id.request_live_data);

        startLiveData.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                GetNewChannelRestOperation channelRestOperation = new GetNewChannelRestOperation(getContext());
                channel = channelRestOperation.Start("SCC33102_R01");
                return true;
            }
        });

        stopLiveData = v.findViewById(R.id.stop_live_data);

        stopLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
