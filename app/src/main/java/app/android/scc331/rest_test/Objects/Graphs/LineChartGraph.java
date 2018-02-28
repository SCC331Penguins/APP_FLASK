package app.android.scc331.rest_test.Objects.Graphs;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
import java.util.TimeZone;

public class LineChartGraph{

    private static final String TEMPERATURE = "Temperature";
    private static final String HUMIDITY = "Humidity";
    private static final String LIGHT = "Light";

    private XAxis xAxis;
    private YAxis leftAxis;
    private LineChart lineChart;

    private String tag;

    public LineChartGraph(LineChart lineChart) {

        this.lineChart = lineChart;

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);


        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.rgb(0, 0, 0));
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long unixSeconds = (long) value;
                Date date = new Date(unixSeconds * 1000L);
                mFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                return mFormat.format(date);
            }
        });

        leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(0, 0, 0));
    }

    public void setData(ArrayList<Entry> entries, String type) {
        lineChart.resetTracking();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet t = new LineDataSet(entries, type);
        t.setLineWidth(2.5f);
        t.setDrawFilled(true);

        switch (type){
            case TEMPERATURE:
                t.setColor(Color.parseColor("#FF0000"));
                t.setCircleColor(Color.parseColor("#FF0000"));
                t.setFillColor(Color.parseColor("#FF0000"));
                break;
            case HUMIDITY:
                t.setColor(Color.parseColor("#00FF00"));
                t.setCircleColor(Color.parseColor("#00FF00"));
                t.setFillColor(Color.parseColor("#00FF00"));
                break;
            case LIGHT:
                t.setColor(Color.parseColor("#0000FF"));
                t.setCircleColor(Color.parseColor("#0000FF"));
                t.setFillColor(Color.parseColor("#0000FF"));

        }

        dataSets.add(t);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setVisible(boolean visible){
        if(visible){
            lineChart.setVisibility(View.VISIBLE);
        }else{
            lineChart.setVisibility(View.GONE);
        }
    }
}
