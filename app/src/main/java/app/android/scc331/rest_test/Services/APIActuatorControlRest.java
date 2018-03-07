package app.android.scc331.rest_test.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.IOException;

public class APIActuatorControlRest {

    private Context context;

    //TODO CHANGE THESE
    private final String TAG = "TOKEN";

    private HttpClient httpClient;

    public APIActuatorControlRest(Context context){
        this.context = context;
        Log.i(TAG,"Attempting to login to the server");
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String macAddress, String command, String router_id){
        try {
            new PostTask(macAddress, command, router_id).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public class PostTask extends AsyncTask<String, Integer, String> {

        private String macAddress, command, router_id;

        PostTask(String macAddress, String command, String router_id){
            this.macAddress = macAddress;
            this.command = command;
            this.router_id = router_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                performPost(macAddress, command, router_id);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public Object performPost(String macAddress, String command, String router_id) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_ACTUATOR_CONTROL);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        StringBuilder s = new StringBuilder();
        s.append("{\"token\":\"" + token +"\", \"MAC\":\"" + macAddress + "\", \"command\":\"" + command + "\", \"router_id\":\"" + router_id + "\"}");
        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        return null;
    }
}