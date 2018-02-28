package app.android.scc331.rest_test.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HistoricData {

    private HashMap<String, HistoricDataSet> results = new HashMap<>();

    public HistoricData(JSONObject jsonObject) throws JSONException {

        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String s = it.next();
            System.out.println("Key parse: " + s);
            JSONObject jso = jsonObject.getJSONObject(s);
            JSONArray jsa = jso.getJSONArray("data");

            for (int i = 0; i < jsa.length(); i++) {
                JSONObject ob = jsa.getJSONObject(i);
                HistoricDataSet historicDataSet = new HistoricDataSet(ob,s);
                results.put(s, historicDataSet);

            }
        }
    }

    public HistoricDataSet getHistoricDataSet(String sensor_id){
        return results.get(sensor_id);
    }

    public ArrayList<HistoricDataSet> getHistoricDataSet(){

        ArrayList<HistoricDataSet> historicDataSets = new ArrayList<>();

        for(String s : results.keySet()){
            historicDataSets.add(results.get(s));
        }
        return historicDataSets;
    }

}
