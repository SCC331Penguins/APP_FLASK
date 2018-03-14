package app.android.scc331.rest_test.Services;

public class RestPaths {

    //TODO Notification stuff
    //TODO Control Actuators

    public static final String TAG = "REST OP";
    public static final String IP = "192.168.0.114";
    //public static final String IP = "sccug-330-02.lancs.ac.uk";
    public static final String URL = "http://" + IP + ":5001";

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
    public static final String PATH_OPEN_CHANNEL = URL + "/api/requestLiveData";
    public static final String PATH_GET_HISTORIC = URL + "/historic/get_history";
    public static final String PATH_ACTUATOR_CONTROL = URL + "/api/actuator_control";
    public static final String PATH_SET_BUTTONS = URL + "/api/set_button";
    public static final String PATH_ARM_SYSTEM = URL + "/api/arm_system";
    public static final String PATH_PHONE_LOCATION = URL + "/api/phone_location";
    public static final String PATH_SET_ROOM = URL + "/api/set_sensor_rooms";
    public static final String GRANT_PERMISSION = URL + "/user/auth_user_add";
    public static final String REVOKE_PERMISSION = URL + "/user/auth_user_remove";
    public static final String GET_AUTH_USER = URL + "/user/get_auth_users";
}
