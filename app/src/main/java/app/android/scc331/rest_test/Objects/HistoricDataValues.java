package app.android.scc331.rest_test.Objects;

import android.util.Log;

public class HistoricDataValues {

    private long time;
    private int value;

    public HistoricDataValues(long time, int value){
        this.time = time;
        this.value = value;

        Log.d("DATA", time + " / " + value );
    }

    public int getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }
}
