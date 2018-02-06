package app.android.scc331.rest_test.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.net.URLEncoder;

import app.android.scc331.rest_test.LoginActivity;
import app.android.scc331.rest_test.MainActivity;

/**
 * Created by Alex Stout on 06/02/2018.
 */

public class RestOperation {

    private final String TAG = "REST OP";

    private final String IP = "10.32.165.30";
    private final String URL = "http://" + IP + ":5000";

    private final String PATH_LOGIN = URL + "/user/login";
    private final String PATH_REGISTER = URL + "/user/register";
    private final String PATH_TEST = URL + "/test";

    private ProgressDialog progressDialog;

    private HttpClient httpClient;

    private String token;

    private Context context;

    public boolean login(Context context, String username, String password){
        Log.i(TAG,"Attempting to login to the server");
        this.context = context;
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);

        boolean ret = false;
        try {
            ret = new LoginTask(username, password).execute("").get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public class LoginTask extends AsyncTask<String, Integer, Boolean>{

        private String username, password;

        LoginTask(String username, String password ){
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try{
                String token = login_json(username, password);

                if(token==null){
                    SharedPreferences preferences = context.getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
                    preferences.edit().putString("token",null).apply();
                    return false;
                }else{
                    SharedPreferences preferences = context.getSharedPreferences("com.set.app", Context.MODE_PRIVATE);
                    preferences.edit().putString("token",token).apply();
                    return true;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(s!=null){
                Log.i(TAG,"Success");
            }else{
                //failure
            }
        }
    }

    public String login_json(String username, String password) throws ClientProtocolException, IOException, JSONException{

        HttpPost post = new HttpPost(PATH_LOGIN.toString());

        post.setEntity(new StringEntity("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
;
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        int status = r.getStatusLine().getStatusCode();

        if(status == 200)//200 0k
        {
            HttpEntity e = r.getEntity();
            String jsondatastring = EntityUtils.toString(e);
            Log.i(TAG,""+jsondatastring);
            JSONObject mainOb = new JSONObject((jsondatastring));
            String logged_in = mainOb.getString("logged_in");
            if(logged_in.equals("true")){
                String token = mainOb.getString("token");
                Log.i(TAG, "Logged in! Token: " + token);
                this.token = token;
                return token;
            }
            return null;
        }else{
            return null;
        }

    }

}
