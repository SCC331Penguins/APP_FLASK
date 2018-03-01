package app.android.scc331.rest_test.Objects;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class Actuator implements Serializable {

    public static final String LIGHT = "LIGHT";
    public static final String KETTLE = "KETTLE";
    public static final String PLUG = "PLUG";

    private String id;
    public String type;
    private ArrayList<String> functions = new ArrayList<>();

    public Actuator(String id, String type, String functions) {
        this.id = id;
        this.type = type;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(functions);
            for (int i = 0; i < jsonArray.length(); i++) {
                this.functions.add(jsonArray.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getFunctions() {
        return functions;
    }
}
