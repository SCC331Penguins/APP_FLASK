package app.android.scc331.rest_test.Services.LiveData.Elements;

import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import app.android.scc331.rest_test.R;

/**
 * Created by Alex Stout on 28/02/2018.
 */

public class LiveDataGraph extends LinearLayout implements OnChartValueSelectedListener {

    private LineChart mChart;

    private Context context;

    public LiveDataGraph(Context context, LineChart lineChart) {
        super(context);
        this.context = context;
        mChart = lineChart;
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);

        mChart.setData(new LineData());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);

         YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(0, 0, 0));

        mChart.invalidate();
    }

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

    public void addEntry(float val) {

        LineData data = mChart.getData();

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        if(set.getEntryCount() > 100){
            set.removeEntry(0);
        }

        set.setDrawFilled(true);

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());

        data.addEntry(new Entry(data.getDataSetByIndex(randomDataSetIndex).getEntryCount(), val), randomDataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        mChart.setVisibleXRangeMaximum(6);
        //mChart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
    }

    public void removeLastEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXValue(set.getEntryCount() - 1, Float.NaN);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntryByXValue(xIndex, dataSetIndex);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }

    public void setVisible(boolean visible){
        if(!visible) {
            mChart.setVisibility(GONE);
        }else{
            mChart.setVisibility(VISIBLE);
        }
    }

    private void addDataSet() {

        LineData data = mChart.getData();

        if (data != null) {

            int count = (data.getDataSetCount() + 1);

            ArrayList<Entry> yVals = new ArrayList<Entry>();

            for (int i = 0; i < data.getEntryCount(); i++) {
                yVals.add(new Entry(i, (float) (Math.random() * 50f) + 50f * count));
            }

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawFilled(true);
            set.setFillColor(Color.RED);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void removeDataSet() {

        LineData data = mChart.getData();

        if (data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    //Add this
    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }


    @Override
    public void onNothingSelected() {

    }


    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);
        set.setFillColor(Color.RED);

        return set;
    }
}