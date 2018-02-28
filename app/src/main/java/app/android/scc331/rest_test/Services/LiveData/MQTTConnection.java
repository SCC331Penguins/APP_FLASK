package app.android.scc331.rest_test.Services.LiveData;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.android.scc331.rest_test.Objects.Actuator;
import app.android.scc331.rest_test.Services.LiveData.Elements.LiveData;
import app.android.scc331.rest_test.Services.RestPaths;

public class MQTTConnection extends Service implements MqttCallback {

    private static final String TAG = "MQTT";

    Callbacks activity;

    IBinder mBinder = new LocalBinder();

    private MqttAndroidClient client;

    private String topic;

    private String connectedRouter;

    public MQTTConnection() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), "tcp://" + RestPaths.IP + ":1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Connected to MQTT Server at: " + RestPaths.IP + ":1883");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Connection Failed.");
                    Log.d(TAG, "Exception: " + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        Log.d(TAG, topic + " : " + message.toString());
        MessageParser m = new MessageParser(message.toString());
        if (m.getType().equals("DATA")) {
            String s = new String(message.getPayload());
            JSONObject jsonObject = new JSONObject(new JSONObject(s).getJSONObject("payload").toString());
            System.out.println("Message payload: "+jsonObject.toString());

            HashMap<String, ArrayList<LiveData>> sensor_data_hashmap = new HashMap<>();
            System.out.println("Created Hashmap");


            ArrayList<LiveData> liveDataList = new ArrayList<>();
            System.out.println("Created Arraylist");

            liveDataList.add(new LiveData((Double) jsonObject.get("temp"), jsonObject.getString("SENSORID"), LiveData.TEMPERATURE));
            liveDataList.add(new LiveData((Double) jsonObject.get("humidity"),  jsonObject.getString("SENSORID"), LiveData.HUMIDITY));
            liveDataList.add(new LiveData((Double) jsonObject.get("light"),  jsonObject.getString("SENSORID"), LiveData.LIGHT));
            liveDataList.add(new LiveData(jsonObject.getDouble("sound"), jsonObject.getString("SENSORID"), LiveData.SOUND));
            liveDataList.add(new LiveData(jsonObject.getInt("uv"),  jsonObject.getString("SENSORID"), LiveData.UV));
            liveDataList.add(new LiveData(jsonObject.getInt("ir"),  jsonObject.getString("SENSORID"), LiveData.IR));
            liveDataList.add(new LiveData(jsonObject.getBoolean("motion"),  jsonObject.getString("SENSORID"), LiveData.MOVEMENT));
            liveDataList.add(new LiveData(jsonObject.getDouble("tiltX"),  jsonObject.getString("SENSORID"), LiveData.TILT_X));
            liveDataList.add(new LiveData(jsonObject.getDouble("tiltY"),  jsonObject.getString("SENSORID"), LiveData.TILT_Y));
            System.out.println("Set Arraylist");

            sensor_data_hashmap.put(jsonObject.getString("SENSORID"), liveDataList);
            System.out.println("Put arraylist in hashmap");

            System.out.println("Looping through live data");
            for(LiveData liveData : liveDataList) {
                System.out.println("LiveDataClass: " + liveData.getValue());
            }


            Log.d("JSON", jsonObject.toString());

            activity.promptLiveData(sensor_data_hashmap);
            System.out.println("Prompted activity");
        } else if (m.getType().equals("YOPHO")) {
            Log.d("YOPHO", "GOT YOPHO");
            getData("430032000f47353136383631");
            activity.promptLiveData(null);
        }
    }

    public void getData(String sensor_id) {
        Log.d("GDATA", "Sending GDATA for: " + sensor_id);
        String json = "{\"type\":\"GDATA\", \"payload\":{\"sensor_id\":\"" + sensor_id + "\", \"channel_id\":\"" + topic + "\"}}";
        Log.d("GDATA TOPIC", topic);
        publish(topic, json);
    }

    public void sendCommand(Actuator actuator, String command){
        String json = "{\"type\":\"COM\", \"payload\":{\"MAC\":\""+actuator.getId()+"\", \"command\":\""+command+"\"}}";
        Log.d("COMMAND", ""+topic);
        publish(topic, json);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MQTTConnection getServerInstance() {
            return MQTTConnection.this;
        }
    }

    public void requestLiveData(String router_id, String sensor_id) {
        StringBuilder s = new StringBuilder();
        s.append("");
    }

    public void unsubscribe(){
        try {
            client.unsubscribe(topic);
            Log.d(TAG,"Unsubscribed from: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        this.connectedRouter = null;
        this.topic = null;
    }

    public boolean isConnected(String router_id){
        if(connectedRouter == null) return false;
        if(connectedRouter.equals(router_id)){
            return true;
        }
        return false;
    }

    public void subscribe(String topic, String connectedRouter) {
        if(topic == null) unsubscribe();
        this.topic = topic;
        Log.d(TAG, "Attempting subscription: " + topic);
        if (client == null) return;
        try {
            client.subscribe(topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Subscribed to: " + topic);
        this.connectedRouter = connectedRouter;
    }

    public void publish(String topic, String message) {
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void registerClient(Activity activity) {
        this.activity = (Callbacks) activity;
    }

    public interface Callbacks {
        void promptLiveData(HashMap<String, ArrayList<LiveData>> sensor_data_hashmap);

        void updateLiveData(JSONObject data);
    }

}
