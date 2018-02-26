package app.android.scc331.rest_test.Objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Alex Stout on 26/02/2018.
 */

public class HistoricData {

    private HashMap<String, HistoricDataSet> results = new HashMap<>();

    public HistoricData(JSONObject jsonObject) throws JSONException {

        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String s = it.next();
            System.out.println("Key: " + s);
            JSONObject jso = jsonObject.getJSONObject(s);
            JSONArray jsa = jso.getJSONArray("data");

            for (int i = 0; i < jsa.length(); i++) {
                JSONObject ob = jsa.getJSONObject(i);

                HistoricDataSet historicDataSet = new HistoricDataSet(ob);
                results.put(s, historicDataSet);

            }

        }
    }

}
