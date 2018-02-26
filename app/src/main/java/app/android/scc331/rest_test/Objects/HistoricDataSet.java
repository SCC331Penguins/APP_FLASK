package app.android.scc331.rest_test.Objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class HistoricDataSet {

    private ArrayList<HistoricDataValues> temperature = new ArrayList<>();
    private ArrayList<HistoricDataValues> humidity = new ArrayList<>();
    private ArrayList<HistoricDataValues> light = new ArrayList<>();
    private ArrayList<HistoricDataValues> movement = new ArrayList<>();
    private ArrayList<HistoricDataValues> sound = new ArrayList<>();
    private ArrayList<HistoricDataValues> ir = new ArrayList<>();
    private ArrayList<HistoricDataValues> uv = new ArrayList<>();

    public HistoricDataSet(JSONObject jsonObject) {

        for (Iterator<String> it1 = jsonObject.keys(); it1.hasNext(); ) {
            String s1 = it1.next();
            JSONArray jsonArray = null;
            try {
                jsonArray = jsonObject.getJSONArray(s1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (s1) {
                case "temperature":
                        temperature = getHistoricDataValues(jsonArray);
                    break;
                case "humidity":
                        humidity = getHistoricDataValues(jsonArray);
                    break;

                case "light":
                        light = getHistoricDataValues(jsonArray);
                    break;

                case "movement":
                        movement = getHistoricDataValues(jsonArray);
                    break;

                case "sound":
                        sound = getHistoricDataValues(jsonArray);
                    break;

                case "uv":
                        uv = getHistoricDataValues(jsonArray);
                    break;

                case "ir":
                        ir = getHistoricDataValues(jsonArray);
                    break;
            }
        }
    }

    private ArrayList<HistoricDataValues> getHistoricDataValues(JSONArray jsonArray) {

        ArrayList<HistoricDataValues> historicDataValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray jsonArray1 = null;
            HistoricDataValues historicDataValue = null;
            try {
                jsonArray1 = (JSONArray) jsonArray.get(i);
                historicDataValue = new HistoricDataValues(jsonArray1.getLong(0), jsonArray1.getInt(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            historicDataValues.add(historicDataValue);
        }
        return historicDataValues;
    }
}
