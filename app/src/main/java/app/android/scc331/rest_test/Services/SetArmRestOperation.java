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

/**
 * Created by Alex Stout on 12/03/2018.
 */

public class SetArmRestOperation {

    private Context context;

    private final String TAG = "SETButtons";

    private HttpClient httpClient;

    public SetArmRestOperation(Context context){
        this.context = context;
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String router_id, boolean armed){
        try {
            new PostTask(router_id, armed).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public class PostTask extends AsyncTask<String, Integer, String> {

        private String router_id;
        private boolean armed;

        PostTask(String router_id, boolean armed){
            this.router_id = router_id;
            this.armed = armed;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                performPost(router_id, armed);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public Object performPost(String router_id, boolean armed) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_ARM_SYSTEM);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        StringBuilder s = new StringBuilder();
        int arm = 0;
        if (armed)
            arm = 1;

        s.append("{\"token\":\"" + token +"\", \"router_id\":\""+router_id+"\", \"armed\":"+arm+"}");

        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        return null;
    }
}
