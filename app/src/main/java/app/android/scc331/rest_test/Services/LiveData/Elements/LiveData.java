package app.android.scc331.rest_test.Services.LiveData.Elements;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LiveData {

    public static final int TEMPERATURE = 1;
    public static final int HUMIDITY = 2;
    public static final int LIGHT = 3;
    public static final int MOVEMENT = 4;
    public static final int SOUND = 5;
    public static final int IR = 6;
    public static final int UV = 8;
    public static final int TILT_X = 9;
    public static final int TILT_Y = 10;

    private int type;

    private String sensor_id;

    private double value;

    public LiveData(double value, String sensor_id, int type){
        this.sensor_id = sensor_id;
        this.value = round(value);
        this.type = type;
    }

    public LiveData(boolean value, String sensor_id, int type){
        this.sensor_id = sensor_id;
        this.type = type;

        if(value) {
            this.value = 1;
        }else{
            this.value = 0;
        }
    }

    public LiveData(int value, String sensor_id, int type){
        this.sensor_id = sensor_id;
        this.type = type;
        this.value = value;
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public int getType(){
        return type;
    }

    public double getValue(){
        return value;
    }

    public String getSensorId(){
        return sensor_id;
    }

}
