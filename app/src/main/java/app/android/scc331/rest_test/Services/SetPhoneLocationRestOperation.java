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

import app.android.scc331.rest_test.Services.LiveData.Elements.Command;

public class SetPhoneLocationRestOperation {

    private Context context;

    private final String TAG = "SETButtons";

    private HttpClient httpClient;

    public SetPhoneLocationRestOperation(Context context){
        this.context = context;
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String router_id, String sensor_id){
        try {
            new PostTask(router_id, sensor_id).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public class PostTask extends AsyncTask<String, Integer, String> {

        private String router_id;
        private String sensor_id;

        PostTask(String router_id, String sensor_id){
            this.router_id = router_id;
            this.sensor_id = sensor_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                performPost(router_id, sensor_id);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public Object performPost(String router_id, String sensor_id) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_PHONE_LOCATION);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        StringBuilder s = new StringBuilder();
        s.append("{\"token\":\"" + token +"\", \"router_id\":\""+router_id+"\", \"sensor_id\":\""+sensor_id+"\"}");

        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        return null;
    }
}
