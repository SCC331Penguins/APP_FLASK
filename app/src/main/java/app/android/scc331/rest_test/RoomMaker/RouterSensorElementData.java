package app.android.scc331.rest_test.RoomMaker;

import java.io.Serializable;

public class RouterSensorElementData implements Serializable {

    public int lm,rm,tm,bm;
    String id;
    int type;
    String name;

    RouterSensorElementData(String id, int type) {
        this.id = id;
        this.type = type;
    }

    RouterSensorElementData(String id, int type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getElementId() {
        return id;
    }

}
