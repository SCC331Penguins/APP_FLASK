package app.android.scc331.rest_test.Services.LiveData;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
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
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Services.RestPaths;

public class MQTTConnection extends Service implements MqttCallback {

    private static final String TAG = "MQTT";

    Callbacks activity;

    IBinder mBinder = new LocalBinder();

    private MqttAndroidClient client;

    public MQTTConnection(){
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
                    Log.d(TAG, "Exception: "+ exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void connectionLost(Throwable cause) {}

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, topic + " : " + message.toString());
        MessageParser m = new MessageParser(message.toString());
        if(m.getType().equals("DATA")) {
            activity.promptLiveData();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}

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

    public void requestLiveData(String router_id, String sensor_id){
        StringBuilder s = new StringBuilder();
        s.append("");
    }

    public void subscribe(String topic, int qos){
        Log.d(TAG, "Attempting subscription: " + topic);
        if(client==null) return;
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Subscribed to: " + topic);
    }

    public void publish(String topic, String message){
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    public interface Callbacks{
        void promptLiveData();
        void updateLiveData(JSONObject data);
    }

}
