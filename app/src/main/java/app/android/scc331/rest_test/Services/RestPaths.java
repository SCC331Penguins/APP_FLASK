package app.android.scc331.rest_test.Services;

public class RestPaths {

    public static final String TAG = "REST OP";

    public static final String IP = "192.168.1.73";
    public static final String URL = "http://" + IP + ":5000";

    public static final String PATH_GET_ROUTER = URL + "/router/get_router";
    public static final String PATH_SET_ROUTER = URL + "/router/claim_router";
    public static final String PATH_LOGIN = URL + "/user/login";
    public static final String PATH_REGISTER = URL + "/user/register";
    public static final String PATH_TEST = URL + "/test";
    public static final String PATH_GET_SENSOR = URL + "/sensor/get_sensors";
    public static final String PATH_SET_SENSOR = URL + "/sensor/set_config";
    public static final String PATH_PING = URL + "/function/ping";
    public static final String PATH_UPDATE_TOKEN = URL + "/function/update_token";
    public static final String PATH_SET_SCRIPT = URL + "/router/set_script";
    public static final String PATH_GET_ACTUATOR = URL + "/router/get_actuators";

}
