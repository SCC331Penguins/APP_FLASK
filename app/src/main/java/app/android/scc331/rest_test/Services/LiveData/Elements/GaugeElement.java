package app.android.scc331.rest_test.Services.LiveData.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.anastr.speedviewlib.SpeedView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetSensorRestOperation;

@SuppressLint("SetTextI18n")
public class GaugeElement extends LinearLayout implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    public static final int TEMPERATURE = 1;
    public static final int HUMID = 2;
    public static final int LIGHT = 3;

    private static final String TEMPERATURE_UNIT  = "c";
    private static final String HUMIDITY_UNIT  = "%";
    private static final String LIGHT_UNIT  = "lux";

    private boolean motionBoolean;

    private SpeedView temperature;
    private SpeedView humidity;
    private SpeedView light;

    private TextView temperatureValue;
    private TextView humidityValue;
    private TextView lightValue;

    private TextView temperatureBigValue;
    private TextView humidityBigValue;
    private TextView lightBigValue;

    private TextView motion;
    private TextView tiltX;
    private TextView tiltY;

    private TextView sound;
    private TextView ir;
    private TextView uv;

    private Spinner spinnerToShow;

    private ArrayList<Sensor> sensors;

    public GaugeElement(Context context, Router router) {
        super(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View gaugeView = layoutInflater.inflate(R.layout.speedview_element, this, false);

        temperature = gaugeView.findViewById(R.id.temperature_gauge);
        humidity = gaugeView.findViewById(R.id.humidity_gauge);
        light = gaugeView.findViewById(R.id.light_gauge);

        temperatureValue = gaugeView.findViewById(R.id.temperature_value);
        humidityValue = gaugeView.findViewById(R.id.humidity_value);
        lightValue = gaugeView.findViewById(R.id.light_value);

        temperatureBigValue = gaugeView.findViewById(R.id.temperature_big_value);
        humidityBigValue = gaugeView.findViewById(R.id.humidity_big_value);
        lightBigValue = gaugeView.findViewById(R.id.light_big_value);

        gaugeView.findViewById(R.id.temperature_layout).setOnClickListener(this);
        gaugeView.findViewById(R.id.humidity_layout).setOnClickListener(this);
        gaugeView.findViewById(R.id.light_layout).setOnClickListener(this);

        spinnerToShow = gaugeView.findViewById(R.id.live_data_spinner);

        GetSensorRestOperation getSensorRestOperation = new GetSensorRestOperation(context);
        //sensors = (ArrayList<Sensor>) getSensorRestOperation.Start(router.getId());
        sensors = (ArrayList<Sensor>) getSensorRestOperation.Start("SCC33102_R01");

        ArrayList<String> sensorNames = new ArrayList<>();

        sensorNames.add("All");
        for(Sensor s : sensors) {
            String name = MainActivity.savedState.getSensorName(s.getId());
            if(name == null){
                sensorNames.add(s.getId());
            }else{
                sensorNames.add(name);
            }
        }

        String[] names = sensorNames.stream().toArray(String[]::new);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.router_spinner_item, names);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerToShow.setAdapter(spinnerArrayAdapter);

        spinnerToShow.setOnItemSelectedListener(this);

        View motionTilt = layoutInflater.inflate(R.layout.motion_tilt_element, this, false);

        motion = motionTilt.findViewById(R.id.motion_panel);
        tiltX = motionTilt.findViewById(R.id.tilt_x);
        tiltY = motionTilt.findViewById(R.id.tilt_y);

        View soundIrUv = layoutInflater.inflate(R.layout.sound_ir_uv, this, false);

        sound = soundIrUv.findViewById(R.id.sound_value);
        ir = soundIrUv.findViewById(R.id.ir_value);
        uv = soundIrUv.findViewById(R.id.uv_value);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setOrientation(VERTICAL);

        addView(gaugeView);
        addView(motionTilt);
        addView(soundIrUv);

        this.setLayoutParams(layoutParams);
    }

    public void setUv(int value){
        uv.setText(String.valueOf(value));
    }

    public void setIr(int value){
        ir.setText(String.valueOf(value));
    }

    public void setSound(int value){
        sound.setText(String.valueOf(value));
    }

    public void setTilt(int x, int y, int z){
        tiltX.setText("X: "+x);
        tiltY.setText("Y: "+y);
    }

    public void setMotion(boolean bool){

        if(bool){
            motion.setTextColor(Color.parseColor("#FFFFFF"));
            motion.setBackgroundColor(Color.parseColor("#44dd44"));
        }else{
            motion.setTextColor(Color.parseColor("#333333"));
            motion.setBackgroundColor(Color.parseColor("#444444"));
        }

        motionBoolean = bool;
    }


    public void setTemperature(int value){
        String stringValue = String.valueOf(value);

        if(temperature.getVisibility() == GONE)
        {
            temperatureBigValue.setText(stringValue);
            temperatureValue.setText(TEMPERATURE_UNIT);
        }
        else
        {
            temperature.setSpeedAt(value);
            temperatureValue.setText(stringValue + TEMPERATURE_UNIT);
        }
    }

    public void setHumidity(int value){
        String stringValue = String.valueOf(value);

        if(humidity.getVisibility() == GONE)
        {
            humidityBigValue.setText(stringValue);
            humidityValue.setText(HUMIDITY_UNIT);
        }
        else
        {
            humidity.setSpeedAt(value);
            humidityValue.setText(stringValue + HUMIDITY_UNIT);
        }

    }

    public void setLight(int value){
        String stringValue = String.valueOf(value);

        if(light.getVisibility() == GONE)
        {
            lightBigValue.setText(stringValue);
            lightValue.setText(LIGHT_UNIT);
        }
        else
        {
            light.setSpeedAt(value);
            lightValue.setText(stringValue + LIGHT_UNIT);
        }
    }

    private int getVisibility(View view){
        if(view.getVisibility() == GONE) {
            return VISIBLE;
        }
        return GONE;
    }

    private void toggleTemperature(){
        temperature.setVisibility(getVisibility(temperature));
        temperatureBigValue.setVisibility(getVisibility(temperatureBigValue));
    }

    private void toggleHumidity(){
        humidity.setVisibility(getVisibility(humidity));
        humidityBigValue.setVisibility(getVisibility(humidityBigValue));

    }

    private void toggleLight(){
        light.setVisibility(getVisibility(light));
        lightBigValue.setVisibility(getVisibility(lightBigValue));
    }

    @Override
    public void onClick(View view) {
        Log.d("CLUCK", "Clicked");
        switch (view.getId())
        {
            case R.id.temperature_layout:
                toggleTemperature();
                break;

            case R.id.humidity_layout:
                toggleHumidity();
                break;

            case R.id.light_layout:
                toggleLight();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
