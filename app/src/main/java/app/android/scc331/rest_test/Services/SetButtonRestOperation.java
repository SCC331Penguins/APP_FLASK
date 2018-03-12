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

public class SetButtonRestOperation {

    private Context context;

    private final String TAG = "SETButtons";

    private HttpClient httpClient;

    public SetButtonRestOperation(Context context){
        this.context = context;
        HttpParams httpParams = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParams);
    }

    public void Start(String router_id, Command button1, Command button2, Command button3, Command button4){
        try {
            new PostTask(router_id, button1, button2, button3, button4).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public class PostTask extends AsyncTask<String, Integer, String> {

        private String router_id;
        private Command button1, button2, button3, button4;

        PostTask(String router_id, Command button1, Command button2, Command button3, Command button4){
            this.router_id = router_id;
            this.button1 = button1;
            this.button2 = button2;
            this.button3 = button3;
            this.button4 = button4;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                performPost(router_id, button1, button2, button3, button4);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public Object performPost(String router_id, Command button1, Command button2, Command button3, Command button4) throws IOException, JSONException {

        HttpPost post = new HttpPost(RestPaths.PATH_SET_BUTTONS);

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.set.app",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        StringBuilder s = new StringBuilder();
        s.append("{\"token\":\"" + token +"\", \"router_id\":\""+router_id+"\"," +
                "\"buttons\":{" +
                "\"1\":{\"MAC\": \""+button1.getMACAddress()+"\", \"command\":\""+button1.getCommand()+"\"}," +
                "\"2\":{\"MAC\": \""+button2.getMACAddress()+"\", \"command\":\""+button2.getCommand()+"\"}," +
                "\"3\":{\"MAC\": \""+button3.getMACAddress()+"\", \"command\":\""+button3.getCommand()+"\"}," +
                "\"4\":{\"MAC\": \""+button4.getMACAddress()+"\", \"command\":\""+button4.getCommand()+"\"}" +
                "}" +
                "}");

        Log.i(TAG, s.toString());

        post.setEntity(new StringEntity(s.toString()));
        post.setHeader("Accept", "application/json");
        post.setHeader("content-type", "application/json");
        Log.i(TAG, "Executing post...");

        HttpResponse r = httpClient.execute(post);

        return null;
    }
}
