package app.android.scc331.rest_test.Objects;

/**
 * Created by alexs on 07/02/2018.
 */

public class Router {

    private boolean online;
    private String id;

    public Router(String id, boolean online){
        this.id = id;
        this.online = online;
    }

    @Override
    public String toString() {
        return "Router ID: " + id + ", Online: " + online;
    }

    public String getId(){
        return id;
    }

    public Boolean isOnline(){
        return online;
    }
