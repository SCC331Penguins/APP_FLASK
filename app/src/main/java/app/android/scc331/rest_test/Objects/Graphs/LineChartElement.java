package app.android.scc331.rest_test.Objects.Graphs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import app.android.scc331.rest_test.Fragements.DatePicker;
import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.HistoricData;
import app.android.scc331.rest_test.Objects.HistoricDataSet;
import app.android.scc331.rest_test.Objects.HistoricDataValues;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetHistoricDataRestOperation;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveDataGraph;

@SuppressLint("ResourceType")
public class LineChartElement extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private LineChartGraph lineChartTemp, lineChartHumid, lineChartLight, lineChartMove, lineChartSound, lineChartUV;
    private LineChartGraph[] lineCharts;

    private LiveDataGraph liveDataTemp, liveDataHumid, liveDataLight, liveDataMove, liveDataSound, liveDataUV;
    private LiveDataGraph[] liveDataGraphs;

    private RadioGroup radioGroup1, radioGroup2;

    private RadioButton tempRadio, humidRadio, lightRadio, moveRadio, soundRadio, uvRadio;

    private TextView graphLabel;

    private Switch liveSwitch;

    private String sensor_id, router_id;

    private Button fromDateButton, toDateButton;

    private int checkedButtonId;
    private int liveSwitchId;

    //TODO Adapt for live data (live graphs) maybe in next release
    public LineChartElement(Context context, String sensor_id, String router_id){
        super(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.linechart_element, this, false);

        graphLabel = v.findViewById(R.id.graph_label);

        liveSwitch = v.findViewById(R.id.live_switch);
        liveSwitch.setOnCheckedChangeListener(this);
        liveSwitchId = liveSwitch.getId();

        radioGroup1 = v.findViewById(R.id.radio_group_1);
        radioGroup2 = v.findViewById(R.id.radio_group_2);

        lineChartTemp = new LineChartGraph(v.findViewById(R.id.line_chart_temperature));
        lineChartHumid = new LineChartGraph(v.findViewById(R.id.line_chart_humidity));
        lineChartLight = new LineChartGraph(v.findViewById(R.id.line_chart_light));
        lineChartMove = new LineChartGraph(v.findViewById(R.id.line_chart_movement));
        lineChartSound = new LineChartGraph(v.findViewById(R.id.line_chart_sound));
        lineChartUV = new LineChartGraph(v.findViewById(R.id.line_chart_uv));

        liveDataTemp = new LiveDataGraph(context, v.findViewById(R.id.line_chart_temperature_live));
        liveDataHumid = new LiveDataGraph(context, v.findViewById(R.id.line_chart_humidity_live));
        liveDataLight = new LiveDataGraph(context, v.findViewById(R.id.line_chart_light_live));
        liveDataMove = new LiveDataGraph(context, v.findViewById(R.id.line_chart_movement_live));
        liveDataSound = new LiveDataGraph(context, v.findViewById(R.id.line_chart_sound_live));
        liveDataUV = new LiveDataGraph(context, v.findViewById(R.id.line_chart_uv_live));

        liveDataGraphs = new LiveDataGraph[]{liveDataTemp, liveDataHumid, liveDataUV, liveDataMove, liveDataSound, liveDataLight};
        lineCharts = new LineChartGraph[]{lineChartTemp, lineChartUV, lineChartSound, lineChartMove, lineChartLight, lineChartHumid};

        tempRadio = v.findViewById(R.id.temperature_radio);
        humidRadio = v.findViewById(R.id.humidity_radio);
        lightRadio = v.findViewById(R.id.light_radio);
        soundRadio = v.findViewById(R.id.sound_radio);
        moveRadio = v.findViewById(R.id.movement_radio);
        uvRadio = v.findViewById(R.id.uv_radio);

        tempRadio.setOnCheckedChangeListener(this);
        humidRadio.setOnCheckedChangeListener(this);
        lightRadio.setOnCheckedChangeListener(this);
        soundRadio.setOnCheckedChangeListener(this);
        moveRadio.setOnCheckedChangeListener(this);
        uvRadio.setOnCheckedChangeListener(this);

        fromDateButton = v.findViewById(R.id.from_date_button);
        toDateButton = v.findViewById(R.id.to_date_button);

        fromDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePicker.newInstance(0);
                newFragment.show(((MainActivity)context).getFragmentManager(), "datePicker");
            }
        });

        toDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePicker.newInstance(1);
                newFragment.show(((MainActivity)context).getFragmentManager(), "datePicker");
            }
        });

        graphLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                liveDataTemp.addEntry(19);
            }
        });

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);

        this.setOrientation(VERTICAL);

        addView(v);
    }


    public void setNewDate(String text, long utc, int type){
        if(type == 0){
            fromDateButton.setText(text);
        }else {
            toDateButton.setText(text);
        }

        if(MainActivity.endDate < MainActivity.startDate){
            Toast.makeText(getContext(), "End date must be after start date.",Toast.LENGTH_LONG).show();
        }else{
            if(MainActivity.endDate == MainActivity.startDate){
                setData(sensor_id, router_id, MainActivity.startDate, MainActivity.endDate+86400, getContext());
            }else {
                setData(sensor_id, router_id, MainActivity.startDate, MainActivity.endDate, getContext());
            }
        }
    }

    public void showChart(LineChartGraph lineChartGraph){

        lineChartGraph.setVisible(true);

        for(LineChartGraph lineChart : lineCharts){
            if(lineChart != lineChartGraph)
                lineChart.setVisible(false);
        }

        for(LiveDataGraph lineChart : liveDataGraphs){
                lineChart.setVisible(false);
        }
    }

    public void showChart(LiveDataGraph lineChartGraph){

        lineChartGraph.setVisible(true);

        for(LiveDataGraph lineChart : liveDataGraphs){
            if(lineChart != lineChartGraph)
                lineChart.setVisible(false);
        }

        for(LineChartGraph lineChart : lineCharts){
            lineChart.setVisible(false);
        }
    }

    public void setData(String sensor_id, String router_id, long start, long end, Context context){
        this.sensor_id=sensor_id;
        this.router_id=router_id;
        GetHistoricDataRestOperation getHistoricDataRestOperation = new GetHistoricDataRestOperation(context);
        HistoricData historicData = getHistoricDataRestOperation.Start(sensor_id, router_id, start, end);

        HistoricDataSet historicDataSet = historicData.getHistoricDataSet(sensor_id);

        initiateData(historicDataSet.getTemperature(), historicDataSet.getHumidity(), historicDataSet.getLight(), historicDataSet.getMovement(),
                historicDataSet.getSound(), historicDataSet.getUv());
    }

    public void initiateData(ArrayList<HistoricDataValues> temperature, ArrayList<HistoricDataValues> humidity,
                             ArrayList<HistoricDataValues> light, ArrayList<HistoricDataValues> movement, ArrayList<HistoricDataValues> sound,
                                ArrayList<HistoricDataValues> uv){

        ArrayList<Entry> temperatureEntries = new ArrayList<>();
        ArrayList<Entry> humidEntries = new ArrayList<>();
        ArrayList<Entry> lightEntries = new ArrayList<>();
        ArrayList<Entry> soundEntries = new ArrayList<>();
        ArrayList<Entry> moveEntries = new ArrayList<>();
        ArrayList<Entry> uvEntries = new ArrayList<>();

        for(HistoricDataValues hdv : temperature){
            temperatureEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : humidity){
            humidEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : light){
            lightEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : movement){
            moveEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : sound){
            soundEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : uv){
            uvEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        lineChartTemp.setData(temperatureEntries, "Temperature");
        lineChartHumid.setData(humidEntries, "Humidity");
        lineChartLight.setData(lightEntries, "Light");
        lineChartMove.setData(lightEntries, "Movement");
        lineChartSound.setData(lightEntries, "Sound");
        lineChartUV.setData(lightEntries, "UV");

    }


    public void addChartTemp(float val){
        liveDataTemp.addEntry(val);
    }


    public void addChartHumid(float val){
        liveDataHumid.addEntry(val);
    }


    public void addChartLight(float val){
        liveDataLight.addEntry(val);
    }


    public void addChartMove(float val){
        liveDataMove.addEntry(val);
    }


    public void addChartSound(float val){
        liveDataSound.addEntry(val);
    }


    public void addChartUV(float val){
        liveDataUV.addEntry(val);
    }


    private void setData(){

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        radioGroup1.clearCheck();
        radioGroup2.clearCheck();

        int id = buttonView.getId();
        if(id != liveSwitchId)
            checkedButtonId = id;

        if(id == tempRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataTemp);
            }else {
                showChart(lineChartTemp);
            }
            graphLabel.setText("TEMPERATURE");
        }
        else if(id == moveRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataMove);
            }else {
                showChart(lineChartMove);
            }
            graphLabel.setText("MOVEMENT");
        }
        else if(id == humidRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataHumid);
            }else {
                showChart(lineChartHumid);
            }
            graphLabel.setText("HUMIDITY");

        }
        else if(id == lightRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataLight);
            }else {
                showChart(lineChartLight);
            }
            graphLabel.setText("LIGHT");
        }
        else if(id == soundRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataSound);
            }else {
                showChart(lineChartSound);
            }
            graphLabel.setText("SOUND");
        }
        else if(id == uvRadio.getId()){
            if(liveSwitch.isChecked()){
                showChart(liveDataUV);
            }else {
                showChart(lineChartUV);
            }
            graphLabel.setText("UV");
        }
    }
}

/*
package app.android.scc331.rest_test.Objects.Graphs;
import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LineChartElement {

    private LineChart mChart;
    private ArrayList<Entry> zone1, zone2, zone3;

    public LineChartElement(LineChart lineChart, ArrayList<Entry> temp, ArrayList<Entry> humid, ArrayList<Entry> light){

        this.zone1 = temp;
        this.zone2 = humid;
        this.zone3 = light;

        mChart = lineChart;
        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // add data
        setData();
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    void setZones(ArrayList<Entry> zone1, ArrayList<Entry> zone2, ArrayList<Entry> zone3)
    {
        this.zone1 = zone1;
        this.zone2 = zone2;
        this.zone3 = zone3;
        this.setData();
    }

    private void setData() {
        mChart.resetTracking();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet t = new LineDataSet(zone1, "Zone 1");
        t.setLineWidth(2.5f);

        t.setColor(Color.parseColor("#FF0000"));
        t.setCircleColor(Color.parseColor("#FF0000"));
        dataSets.add(t);

        LineDataSet h = new LineDataSet(zone2, "Zone 2");
        h.setLineWidth(2.5f);

        h.setColor(Color.parseColor("#00FF00"));
        h.setCircleColor(Color.parseColor("#00FF00"));
        dataSets.add(h);


        LineDataSet l = new LineDataSet(zone3, "Zone 3");
        l.setLineWidth(2.5f);

        l.setColor(Color.parseColor("#0000FF"));
        l.setCircleColor(Color.parseColor("#0000FF"));
        dataSets.add(l);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    public LineChart getLineChart(){
        return mChart;
    }
}
 */