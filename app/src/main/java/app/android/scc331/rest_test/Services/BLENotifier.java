package app.android.scc331.rest_test.Services;

import android.util.Log;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.CurrentZone;

public class BLENotifier implements RangeNotifier, MonitorNotifier {

    String apiToken, routerID = null;

    public BLENotifier(String apiToken){
        this.apiToken = apiToken;
        this.routerID = "";
    }

    public BLENotifier(String apiToken, String routerID){
        this.apiToken = apiToken;
        this.routerID = routerID;
    }

    public void setRouterID(String routerID){
        this.routerID = routerID;
    }
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Beacon closestbeacon = null;
        for (Beacon beacon : beacons) {
            if(closestbeacon == null)
                closestbeacon = beacon;
            else if (closestbeacon.getDistance() > beacon.getDistance())
                closestbeacon = beacon;
        }
        if(closestbeacon != null) {
            String hexStr = closestbeacon.getId1().toString().substring(2);
            try {
                doRequest(new String(Hex.decodeHex(hexStr.toCharArray())));
            } catch (DecoderException e) {
                e.printStackTrace();
            }

        }
    }
    public void doRequest(String zone){
        Map<String, String > params = new HashMap<>();
        MainActivity.currentZone.setZone(zone);
        params.put("router_id",this.routerID);
        params.put("token",this.apiToken);
        params.put("sensor_id",zone);
        new HttpRequest(RestPaths.PATH_PHONE_LOCATION,params).execute();
    }

    @Override
    public void didEnterRegion(Region region) {
        System.out.println("Enter");
        System.out.println(region);
    }

    @Override
    public void didExitRegion(Region region) {
        System.out.println("Exit");
        System.out.println(region);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        System.out.println("Determine");
        System.out.println(i);
        System.out.println(region);
    }
}
