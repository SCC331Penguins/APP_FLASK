package app.android.scc331.rest_test.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import app.android.scc331.rest_test.RoomMaker.Element;
import app.android.scc331.rest_test.RoomMaker.ElementData;
import app.android.scc331.rest_test.RoomMaker.RouterElement;
import app.android.scc331.rest_test.RoomMaker.RouterSensorElementData;
import app.android.scc331.rest_test.Util.InternalStorage;

public class SavedState implements Serializable {

    private static final String SAVE_KEY = "saved_state_username_";

    private HashMap<String, String> router_names = new HashMap<>();
    private HashMap<String, String> sensor_names = new HashMap<>();

    private HashMap<String, ArrayList<ElementData>>  routerRoomPlans = new HashMap<>();
    private HashMap<String, RouterSensorElementData>  routerRoomPlanRouterSensor = new HashMap<>();
    private HashMap<String, ArrayList<RouterSensorElementData>> roomPlanSensors = new HashMap<>();

    public void saveElements(String router_id, ArrayList<ElementData> elements){
        this.routerRoomPlans.put(router_id, elements);
    }

    public void saveRouterSensorElements(String router_id, RouterSensorElementData data){
        routerRoomPlanRouterSensor.put(router_id, data);
    }

    public RouterSensorElementData getRouterSensorElements(String router_id){
        return routerRoomPlanRouterSensor.get(router_id);
    }

    public void saveSensorElements(String router_id, ArrayList<RouterSensorElementData> data){
        roomPlanSensors.put(router_id, data);
    }

    public ArrayList<RouterSensorElementData> getSensorElements(String router_id){
        return roomPlanSensors.get(router_id);
    }

    public ArrayList<ElementData> getElements(String router_id) {
        return routerRoomPlans.get(router_id);
    }

    public void saveRouter(String router_id, String router_name){
        Log.d("RNAMES", ""+ router_id + " : " + router_name);
        router_names.put(router_id, router_name);
    }

    public void saveSensor(String sensor_id, String sensor_name){
        sensor_names.put(sensor_id, sensor_name);
    }

    public String getRouterName(String router_id){
        Log.d("RNAME", ""+router_names.get(router_id));
        return router_names.get(router_id);
    }

    public String getSensorName(String sensor_id){
        return sensor_names.get(sensor_id);
    }

    public void save(Context context){
        SharedPreferences preferences = context.getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");
        try {
            InternalStorage.writeObject(context,SAVE_KEY+username, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SavedState load(Context context){

        SharedPreferences preferences = context.getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");

        if(InternalStorage.fileExistance(context, SAVE_KEY+username)){
            try {
                return (SavedState) InternalStorage.readObject(context, SAVE_KEY+username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new SavedState();
    }

}
