package app.android.scc331.rest_test.RoomMaker;

import java.io.Serializable;

/**
 * Created by Alex Stout on 13/02/2018.
 */

public class ElementData implements Serializable {

    public String label_name;
    public int[] type = new int[2];
    public int lm, rm, bm, tm;

}
