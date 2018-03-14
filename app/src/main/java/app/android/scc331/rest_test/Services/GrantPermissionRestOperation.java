package app.android.scc331.rest_test.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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

import app.android.scc331.rest_test.Fragements.RouterPermissionFragment;
import app.android.scc331.rest_test.Objects.UserData;


public class GrantPermissionRestOperation {

    private HttpClient httpClient;

    private RouterPermissionFragment routerPermissionFragment;

    private Context context;

    private String router_id;

    private String username;

    private String url;

    private UserData userData;

    public GrantPermissionRestOperation(Context context, RouterPermissionFragment routerPermissionFragment) {
        this.context = context;
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
        this.routerPermissionFragment = routerPermissionFragment;
    }

    public void run(String router_id, String username, String url){
        this.router_id = router_id;
        this.username = username;
        this.url = url;
        new POST().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class POST extends AsyncTask<String, Integer, Boolean> {



        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                return performPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                if(url.equals(RestPaths.GRANT_PERMISSION)){
                    routerPermissionFragment.addUserData(userData);
                }else{
                    routerPermissionFragment.removeUserData(userData);
                }
            }else{
                Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    public Boolean performPost() throws IOException, JSONException {

        HttpPost post = new HttpPost(url);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");


        JSONObject toSend = new JSONObject();
        toSend.put("token", token);
        toSend.put("router_id", router_id);
        toSend.put("username", username);


        post.setEntity(new StringEntity(toSend.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");

        HttpResponse r = httpClient.execute(post);

        int status = r.getStatusLine().getStatusCode();

        if(status == 200)
        {
            HttpEntity e = r.getEntity();
            String jsondatastring = EntityUtils.toString(e);
            JSONObject jsonObject = new JSONObject((jsondatastring));

            String user = jsonObject.getString("username");
            int id = jsonObject.getInt("id");

            userData = new UserData(user, id);

            return true;
        }

        return false;
    }
}