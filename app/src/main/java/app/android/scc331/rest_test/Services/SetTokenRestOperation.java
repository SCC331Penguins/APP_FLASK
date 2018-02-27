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

public class SetTokenRestOperation {

    private Context context;

    //TODO CHANGE THESE
    private final String TAG = "TOKEN";

    private HttpClient httpClient;

    public SetTokenRestOperation(Context context){
        this.context = context;
        Log.i(TAG,"Attempting to login to the server");
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String token){
        try {
            new PostTask(token).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public class PostTask extends AsyncTask<String, Integer, String>{

        private String token;

        PostTask(String token){
            this.token = token;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                performPost(token);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public Object performPost(String token_phone) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_UPDATE_TOKEN);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        StringBuilder s = new StringBuilder();
        s.append("{\"token\":\"" + token +"\", \"token_phone\":\"" + token_phone + "\"}");
        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        return null;
    }
}