package app.android.scc331.rest_test.Objects;

import android.view.View;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Nikola on 14/02/2018.
 */

public class TriggerCondition implements Serializable
{
    public String sensorName;
    public String metric;
    public String relationalOperator;
    public String threshold;
    public String logicalOperator = null;
    public String id = UUID.randomUUID().toString();
    public TriggerCondition next = null;
    public TriggerCondition previous = null;
    public View view = null;
}
