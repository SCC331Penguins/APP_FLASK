package app.android.scc331.rest_test.Services.LiveData;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import app.android.scc331.rest_test.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class LiveDataService extends Service {

    private OkHttpClient client;
    private static String TAG = "LiveService";

    public LiveDataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Created");
        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.1.200:8000").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG,"Starting");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Destroyed");
        super.onDestroy();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's Alex !");;
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d(TAG,"Receiving : " + text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.d(TAG,"Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.d(TAG,"Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.d(TAG,"Error : " + t.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
