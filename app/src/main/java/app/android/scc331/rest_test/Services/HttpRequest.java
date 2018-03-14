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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by wilson on 09/03/18.
 */

public class HttpRequest extends AsyncTask<String, Integer, String> {
    String url = null;
    Map<String, String> params = null;
    HttpClient httpClient;
    HttpRequest(String url, Map<String, String> params){
        this.url = url;
        this.params = params;
        this.httpClient = new DefaultHttpClient();
    }
    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try{
            HttpResponse response = performPost(url, params);
            result = EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public HttpResponse performPost(String url, Map<String, String> params) throws IOException, JSONException {
        HttpPost post = new HttpPost(url);
        String json = new JSONObject(params).toString();
        post.setEntity(new StringEntity(json));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");

        HttpResponse r = httpClient.execute(post);
        return r;
    }
}
