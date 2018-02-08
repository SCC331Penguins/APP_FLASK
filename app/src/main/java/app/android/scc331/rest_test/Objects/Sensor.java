package app.android.scc331.rest_test.Objects;

import android.util.Log;

public class Sensor {

    private static int LIGHT = 1;
    private static int HUMID = 2;
    private static int TEMP = 4;
    private static int SOUND = 8;
    private static int MOTION = 16;
    private static int TILT = 32;
    private static int UV = 64;
    //TODO CHANGE THE IDK
    private static int IDK = 128;

    private final int[] configArray = new int[] {LIGHT, HUMID, TEMP, SOUND, MOTION, TILT, UV, IDK};

    private String id;
    private int config;

    public Sensor(String id, int config){
        this.id = id;
        this.config = config;
    }

    public String getId(){
        return id;
    }

    public Boolean[] getConfig(){
        Boolean[] sensorConfig = new Boolean[8];
        int j = 0;
        for(Integer i : configArray){
            int and = i & config;
            if(and == 0){
                sensorConfig[j] = false;
            }else{
                sensorConfig[j] = true;
            }
            Log.i("BOOL", ""+sensorConfig[j]);
            j++;
        }
        return sensorConfig;
    }
}
