package app.android.scc331.rest_test.Objects.Graphs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import app.android.scc331.rest_test.Objects.HistoricData;
import app.android.scc331.rest_test.Objects.HistoricDataSet;
import app.android.scc331.rest_test.Objects.HistoricDataValues;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetHistoricDataRestOperation;

@SuppressLint("ResourceType")
public class LineChartElement extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

    private LineChartGraph lineChartTemp, lineChartHumid, lineChartLight;

    private RadioButton temperatureButton, humidityButton, lightButton;

    private TextView graphLabel;

    public LineChartElement(Context context, String sensor_id, String router_id){
        super(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.linechart_element, this, false);

        graphLabel = v.findViewById(R.id.graph_label);

        lineChartTemp = new LineChartGraph(v.findViewById(R.id.line_chart_temperature));
        lineChartHumid = new LineChartGraph(v.findViewById(R.id.line_chart_humidity));
        lineChartLight = new LineChartGraph(v.findViewById(R.id.line_chart_light));

        temperatureButton = v.findViewById(R.id.temperature_radio);
        humidityButton = v.findViewById(R.id.humidity_radio);
        lightButton = v.findViewById(R.id.light_radio);

        lineChartTemp.setVisible(true);
        lineChartHumid.setVisible(false);
        lineChartLight.setVisible(false);

        /*
        temperatureButton.setOnCheckedChangeListener(this);
        humidityButton.setOnCheckedChangeListener(this);
        lightButton.setOnCheckedChangeListener(this);
        */

        RadioGroup radioGroup = v.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);

        this.setOrientation(VERTICAL);

        GetHistoricDataRestOperation getHistoricDataRestOperation = new GetHistoricDataRestOperation(context);
        HistoricData historicData = getHistoricDataRestOperation.Start(sensor_id, router_id);

        HistoricDataSet historicDataSet = historicData.getHistoricDataSet(sensor_id);

        initiateData(historicDataSet.getTemperature(), historicDataSet.getHumidity(), historicDataSet.getLight());

        addView(v);
    }

    public void initiateData(ArrayList<HistoricDataValues> temperature, ArrayList<HistoricDataValues> humidity,
                                         ArrayList<HistoricDataValues> light){

        ArrayList<Entry> temperatureEntries = new ArrayList<>();
        ArrayList<Entry> humidEntries = new ArrayList<>();
        ArrayList<Entry> lightEntries = new ArrayList<>();

        for(HistoricDataValues hdv : temperature){
            temperatureEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : humidity){
            humidEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        for(HistoricDataValues hdv : light){
            lightEntries.add(new Entry(hdv.getTime(), (float) hdv.getValue()));
        }

        lineChartTemp.setData(temperatureEntries, "Temperature");
        lineChartHumid.setData(humidEntries, "Humidity");
        lineChartLight.setData(lightEntries, "Light");

    }

    private void setData(){

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i){
            case R.id.temperature_radio:
                lineChartTemp.setVisible(true);
                lineChartHumid.setVisible(false);
                lineChartLight.setVisible(false);
                graphLabel.setText("TEMPERATURE");
                break;
            case R.id.humidity_radio:
                lineChartTemp.setVisible(false);
                lineChartHumid.setVisible(true);
                lineChartLight.setVisible(false);
                graphLabel.setText("HUMIDITY");
                break;
            case R.id.light_radio:
                lineChartTemp.setVisible(false);
                lineChartHumid.setVisible(false);
                lineChartLight.setVisible(true);
                graphLabel.setText("LIGHT");
                break;
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