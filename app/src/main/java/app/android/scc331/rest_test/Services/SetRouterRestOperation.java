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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.Sensor;

/**
 * Created by alexs on 07/02/2018.
 */

public class SetRouterRestOperation {

    private Context context;

    //TODO CHANGE THESE
    private final String TAG = "LOGIN";

    private HttpClient httpClient;

    public SetRouterRestOperation(Context context){
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

    public class PostTask extends AsyncTask<String, Integer, String>{

        String router_id;

        PostTask(String router_id){
            this.router_id = router_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try{
                response = (String) performPost(router_id);
            }catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }
    }

    public Object performPost(String router_id) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_SET_ROUTER);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        post.setEntity(new StringEntity("{\"token\":\"" + token + "\",\"router_id\":\""+router_id+"\"}"));
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
            return null;
        }else{
            return null;
        }
    }
}
