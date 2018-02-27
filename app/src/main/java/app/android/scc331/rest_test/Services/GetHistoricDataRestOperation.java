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

import app.android.scc331.rest_test.Objects.HistoricData;

public class GetHistoricDataRestOperation {

    private Context context;

    //TODO CHANGE THESE
    private final String TAG = "HISTORIC DATA";

    private HttpClient httpClient;

    public GetHistoricDataRestOperation(Context context){
        this.context = context;
        Log.i(TAG,"Attempting to login to the server");
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public HistoricData Start(String sensor_id, String router_id){
        Object returnObject = null;
        try {
            returnObject = new PostTask(sensor_id, router_id).execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (HistoricData) returnObject;
    }

    public class PostTask extends AsyncTask<String, Integer, Object>{

        private String sensor_id;
        private String router_id;

        PostTask(String sensor_id, String router_id){
            this.sensor_id = sensor_id;
            this.router_id = router_id;
        }

        @Override
        protected Object doInBackground(String... strings) {
            Object result = null;
            try{
                result = performPost(sensor_id, router_id);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }

    public Object performPost(String sensor_id, String router_id) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_GET_HISTORIC);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        StringBuilder s = new StringBuilder();
        s.append("{\"token\":\"" + token + "\", \"router_id\":\"" + router_id + "\", \"sensor_id\":\""+ sensor_id +"\"}");
        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        int status = r.getStatusLine().getStatusCode();

        String result = null;

        if (status == 200)//200 0k
        {
            HttpEntity e = r.getEntity();
            String jsondatastring = EntityUtils.toString(e);
            Log.i(TAG, "" + jsondatastring);
            JSONObject jsonObject = new JSONObject(jsondatastring);
            JSONObject data = jsonObject.getJSONObject("data");
            return new HistoricData(data);
        }
        return null;
    }
}