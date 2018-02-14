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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import app.android.scc331.rest_test.Objects.Actuator;


/**
 * Created by alexs on 07/02/2018.
 */

public class GetActuatorRestOperation {

    private Context context;

    //TODO CHANGE THESE
    private final String TAG = "LOGIN";

    private HttpClient httpClient;

    public GetActuatorRestOperation(Context context){
        this.context = context;
        Log.i(TAG,"Attempting to login to the server");
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public Object Start(String router_id){
        Object returnObject = null;
        try {
            returnObject = new PostTask(router_id).execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnObject;
    }

    public class PostTask extends AsyncTask<String, Integer, ArrayList<Actuator>> {

        String router_id;

        PostTask(String router_id){
            this.router_id = router_id;
        }

        @Override
        protected ArrayList<Actuator> doInBackground(String... strings) {
            ArrayList<Actuator> sensors = null;
            try{
                sensors = (ArrayList<Actuator>) performPost(router_id);
            }catch (Exception e){
                e.printStackTrace();
            }
            return sensors;
        }
    }

    public Object performPost(String router_id) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_GET_ACTUATOR);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        post.setEntity(new StringEntity("{\"token\":\"" + token + "\",\"router_id\":\""+router_id+"\"}"));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        ;
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        int status = r.getStatusLine().getStatusCode();

        if(status == 200)
        {
            HttpEntity e = r.getEntity();
            String jsondatastring = EntityUtils.toString(e);
            Log.i(TAG,""+jsondatastring);
            JSONArray mainOb = new JSONArray((jsondatastring));

            JSONArray rou = mainOb.getJSONArray(0);

            Log.i(TAG, ""+rou.get(0));

            ArrayList<Actuator> actuators = new ArrayList<Actuator>();


            for( int i = 0; i != mainOb.length(); i++){

                JSONArray innerArray = mainOb.getJSONArray(i);

                String actuator_id = (String) innerArray.get(0);
                String type = (String) innerArray.get(1);

                actuators.add(new Actuator(actuator_id, type));
            }

            return actuators;
        }else{
            return null;
        }
    }
}
