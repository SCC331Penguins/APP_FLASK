package app.android.scc331.rest_test.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Alex Stout on 13/03/2018.
 */

public class SetRoomRestOperation {

    private HttpClient httpClient;

    private static final String TAG = "SETRoom";

    private Context context;

    public SetRoomRestOperation(Context context){
        this.context = context;
        Log.i(TAG,"Attempting to login to the server");
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String router_id, HashMap<String, String> sensorRooms){
        try {
            new PostTask(router_id, sensorRooms).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class PostTask extends AsyncTask<String, Integer, String> {

        String router_id;
        HashMap<String, String> sensorRooms;

        PostTask(String router_id, HashMap<String, String> sensorRooms){
            this.router_id = router_id;
            this.sensorRooms = sensorRooms;
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try{
                response = (String) performPost(router_id, sensorRooms);
            }catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }
    }

    public Object performPost(String router_id, HashMap<String, String> sensorRooms) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_SET_ROOM);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        post.setEntity(new StringEntity("{\"token\":\"" + token + "\",\"router_id\":\""+router_id+"\"}"));

        JSONObject toSend = new JSONObject();

        toSend.put("token", token);
        toSend.put("router_id", router_id);


        StringBuilder sensors = new StringBuilder();

        sensors.append(", \"sensors\": [");

        for(String key : sensorRooms.keySet()){
            sensors.append("{\"id\": \""+key+"\", \"room\": \""+sensorRooms.get(key)+"\"},");
        }

        String sensorString = sensors.toString().substring(0, sensors.toString().length()-1);

        sensorString = sensorString + "]}";
        String jsonObjString = toSend.toString().substring(0, toSend.toString().length()-1);

        String json = jsonObjString + sensorString;

        Log.d("ROOMREST", json);

        post.setEntity(new StringEntity(json));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        int status = r.getStatusLine().getStatusCode();

        if(status == 200)//200 0k
        {
            HttpEntity e = r.getEntity();
            String jsondatastring = EntityUtils.toString(e);
            Log.i(TAG,""+jsondatastring);
        }else{
            return null;
        }
        return null;
    }
}
