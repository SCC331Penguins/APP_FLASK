package app.android.scc331.rest_test.Objects;

import java.util.ArrayList;

public class CurrentZone {

    ArrayList<ZoneListener> zoneListeners = new ArrayList<>();

    private String zone;

    public CurrentZone(String zone){
        this.zone = zone;
    }

    public void setZoneListener(ZoneListener zoneListener){
        zoneListeners.add(zoneListener);
    }

    public void removeListener(ZoneListener zoneListener){
        zoneListeners.remove(zoneListener);
    }

    public void setZone(String zone){
        if(zone.equals(this.zone))
            return;
        this.zone = zone;
        for(ZoneListener zoneListener : zoneListeners)
            zoneListener.onZoneChange(zone);
    }
}
