package app.android.scc331.rest_test.Services.LiveData;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageParser {

    private String type;

    private JSONObject payload;

    public MessageParser(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getType(){
        return type;
    }
}
