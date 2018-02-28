package app.android.scc331.rest_test.Services.LiveData.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Graphs.LineChartElement;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetSensorRestOperation;

@SuppressLint("SetTextI18n")
public class GaugeElement extends LinearLayout implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    public static final int TEMPERATURE = 1;
    public static final int HUMID = 2;
    public static final int LIGHT = 3;

    private static final String TEMPERATURE_UNIT = "c";
    private static final String HUMIDITY_UNIT = "%";
    private static final String LIGHT_UNIT = "lux";

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

    private SpinnerSensorListener spinnerSensorListener;

    private String[] names;
    private String selected;

    private Router router;

    private int sel;

    private LineChartElement lineChartElement;

    private ArrayList<Sensor> sensors;

    public GaugeElement(Context context, Router router) {
        super(context);

        this.router = router;

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

        for (Sensor s : sensors) {
            String name = MainActivity.savedState.getSensorName(s.getId());
            if (name == null) {
                sensorNames.add(s.getId());
            } else {
                sensorNames.add(name);
            }
        }

        names = new String[sensorNames.size()];
        names = sensorNames.toArray(names);

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

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setOrientation(VERTICAL);

        lineChartElement = new LineChartElement(context, sensors.get(0).getId(), router.getId());

        addView(gaugeView);
        addView(motionTilt);
        addView(soundIrUv);
        addView(lineChartElement);

        this.setLayoutParams(layoutParams);
    }

    public void setData(HashMap<String, ArrayList<LiveData>> data) {

        if(data.size() == 0){
            Log.d("GDATA", "Sending GDATA for sensors");
            return;
        }

        if (selected.equals("ALL")) {

            HashMap<Integer, Double> averaged = new HashMap<>();

            averaged.put(LiveData.TEMPERATURE, 0d);
            averaged.put(LiveData.HUMIDITY, 0d);
            averaged.put(LiveData.LIGHT, 0d);
            averaged.put(LiveData.MOVEMENT, 0d);
            averaged.put(LiveData.SOUND, 0d);
            averaged.put(LiveData.IR, 0d);
            averaged.put(LiveData.UV, 0d);
            averaged.put(LiveData.TILT_X, 0d);
            averaged.put(LiveData.TILT_Y, 0d);

            int dataLen = data.size();

            for (String s : data.keySet()) {
                ArrayList<LiveData> liveData = data.get(s);

                for (LiveData liveData1 : liveData) {

                    switch (liveData1.getType()) {

                        case LiveData.TEMPERATURE:
                            averaged.put(LiveData.TEMPERATURE, averaged.get(LiveData.TEMPERATURE) + liveData1.getValue());
                            break;

                        case LiveData.HUMIDITY:
                            averaged.put(LiveData.HUMIDITY, averaged.get(LiveData.HUMIDITY) + liveData1.getValue());
                            break;

                        case LiveData.LIGHT:
                            averaged.put(LiveData.LIGHT, averaged.get(LiveData.LIGHT) + liveData1.getValue());
                            break;

                        case LiveData.MOVEMENT:
                            averaged.put(LiveData.MOVEMENT, averaged.get(LiveData.MOVEMENT) + liveData1.getValue());
                            break;

                        case LiveData.SOUND:
                            averaged.put(LiveData.SOUND, averaged.get(LiveData.SOUND) + liveData1.getValue());
                            break;

                        case LiveData.IR:
                            averaged.put(LiveData.IR, averaged.get(LiveData.IR) + liveData1.getValue());
                            break;

                        case LiveData.UV:
                            averaged.put(LiveData.UV, averaged.get(LiveData.UV) + liveData1.getValue());
                            break;

                        case LiveData.TILT_X:
                            averaged.put(LiveData.TILT_X, averaged.get(LiveData.TILT_X) + liveData1.getValue());
                            break;

                        case LiveData.TILT_Y:
                            averaged.put(LiveData.TILT_Y, averaged.get(LiveData.TILT_Y) + liveData1.getValue());
                            break;
                    }
                }
            }
            setTemperature(averaged.get(LiveData.TEMPERATURE)/dataLen);

            setHumidity(averaged.get(LiveData.HUMIDITY)/dataLen);

            setLight(averaged.get(LiveData.LIGHT)/dataLen);

            setMotion(averaged.get(LiveData.MOVEMENT)/dataLen);

            setSound(averaged.get(LiveData.SOUND)/dataLen);

            setUv(averaged.get(LiveData.UV)/dataLen);

            setIr(averaged.get(LiveData.IR)/dataLen);

            setTiltY(averaged.get(LiveData.TILT_Y)/dataLen);

            setTiltX(averaged.get(LiveData.TILT_X)/dataLen);
            return;
        }

        ArrayList<LiveData> liveDataList = data.get(selected);

        for (
                LiveData liveData : liveDataList)

        {

            switch (liveData.getType()) {

                case LiveData.TEMPERATURE:
                    setTemperature(liveData.getValue());
                    break;

                case LiveData.HUMIDITY:
                    setHumidity(liveData.getValue());
                    break;

                case LiveData.LIGHT:
                    setLight(liveData.getValue());
                    break;

                case LiveData.MOVEMENT:
                    setMotion(liveData.getValue());
                    break;

                case LiveData.SOUND:
                    setSound(liveData.getValue());
                    break;

                case LiveData.IR:
                    setIr(liveData.getValue());
                    break;

                case LiveData.UV:
                    setUv(liveData.getValue());
                    break;

                case LiveData.TILT_X:
                    setTiltX(liveData.getValue());
                    break;

                case LiveData.TILT_Y:
                    setTiltY(liveData.getType());
                    break;
            }


        }

    }

    public void setUv(double value) {
        uv.setText(String.valueOf(value));
    }

    public void setIr(double value) {
        ir.setText(String.valueOf(value));
    }

    public void setSound(double value) {
        sound.setText(String.valueOf(value));
    }

    public void setTiltX(double x) {
        tiltX.setText("X: " + x);
    }

    private void setTiltY(double y) {
        tiltY.setText("Y: " + y);
    }

    public void setMotion(double bool) {

        if (bool == 1) {
            motion.setTextColor(Color.parseColor("#FFFFFF"));
            motion.setBackgroundColor(Color.parseColor("#44dd44"));
        } else {
            motion.setTextColor(Color.parseColor("#333333"));
            motion.setBackgroundColor(Color.parseColor("#444444"));
        }
    }


    public void setTemperature(double value) {
        String stringValue = String.valueOf(value);

        if (temperature.getVisibility() == GONE) {
            temperatureBigValue.setText(stringValue);
            temperatureValue.setText(TEMPERATURE_UNIT);
        } else {
            temperature.setSpeedAt((float) value);
            temperatureValue.setText(stringValue + TEMPERATURE_UNIT);
        }
    }

    public void setHumidity(double value) {
        String stringValue = String.valueOf(value);

        if (humidity.getVisibility() == GONE) {
            humidityBigValue.setText(stringValue);
            humidityValue.setText(HUMIDITY_UNIT);
        } else {
            humidity.setSpeedAt((float) value);
            humidityValue.setText(stringValue + HUMIDITY_UNIT);
        }

    }

    public void setLight(double value) {
        String stringValue = String.valueOf(value);

        if (light.getVisibility() == GONE) {
            lightBigValue.setText(stringValue);
            lightValue.setText(LIGHT_UNIT);
        } else {
            light.setSpeedAt((float) value);
            lightValue.setText(stringValue + LIGHT_UNIT);
        }
    }

    private int getVisibility(View view) {
        if (view.getVisibility() == GONE) {
            return VISIBLE;
        }
        return GONE;
    }

    private void toggleTemperature() {
        temperature.setVisibility(getVisibility(temperature));
        temperatureBigValue.setVisibility(getVisibility(temperatureBigValue));
    }

    private void toggleHumidity() {
        humidity.setVisibility(getVisibility(humidity));
        humidityBigValue.setVisibility(getVisibility(humidityBigValue));

    }

    private void toggleLight() {
        light.setVisibility(getVisibility(light));
        lightBigValue.setVisibility(getVisibility(lightBigValue));
    }

    @Override
    public void onClick(View view) {
        Log.d("CLUCK", "Clicked");
        switch (view.getId()) {
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

    public void setSpinnerSensorListener(SpinnerSensorListener spinnerSensorListener){
        this.spinnerSensorListener = spinnerSensorListener;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selected = sensors.get(i).getId();
            lineChartElement.setData(selected,router.getId(), getContext());
        //spinnerSensorListener.onNewSelected(selected);
        Log.d("SELECTED", selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
