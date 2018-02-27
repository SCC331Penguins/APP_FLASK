package app.android.scc331.rest_test.Objects;

import java.io.Serializable;

public class Actuator implements Serializable {

    public static final String LIGHT = "LIGHT";
    public static final String KETTLE = "KETTLE";
    public static final String PLUG = "PLUG";

    //TODO Add functions of each actuator
    public static final String[] LIGHT_FUNCTIONS = new String[]{};
    public static final String[] KETTLE_FUNCTIONS = new String[]{};
    public static final String[] PLUG_FUNCTIONS = new String[]{};

    private String id;
    private String type;

    public Actuator(String id, String type)
    {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
