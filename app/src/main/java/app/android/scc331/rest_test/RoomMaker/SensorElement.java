package app.android.scc331.rest_test.RoomMaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.android.scc331.rest_test.R;

public class SensorElement extends FrameLayout {

    private static final int ROUTER = 1;
    private static final int SENSOR = 2;

    private Context context;

    private LinearLayout linearLayout;

    private ImageView imageView;

    private TextView textView;

    private String sensor_id;

    public SensorElement(@NonNull Context context, String sensor_name, String sensor_id) {
        super(context);

        this.context = context;

        this.sensor_id = sensor_id;

        LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();

        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40)));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.sensor));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        imageView.setLayoutParams(param);

        linearLayout.addView(imageView);

        textView = new TextView(context);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        textView.setLayoutParams(params2);
        textView.setText(sensor_name);
        textView.setTextSize(12);
        textView.setTextColor(Color.RED);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        linearLayout.addView(textView);

        this.addView(linearLayout);
    }

    public static SensorElement loadFromSave(Context context, RouterSensorElementData elementData){
        SensorElement e = new SensorElement(context, elementData.name, elementData.id);
        LayoutParams layoutParams = (LayoutParams) e.getLayoutParams();
        layoutParams.leftMargin = elementData.lm;
        layoutParams.rightMargin = elementData.rm;
        layoutParams.topMargin = elementData.tm;
        layoutParams.bottomMargin = elementData.bm;
        return e;
    }

    public RouterSensorElementData dump(){
        RouterSensorElementData elementData = new RouterSensorElementData(sensor_id, ROUTER, textView.getText().toString());
        LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();
        int lm = layoutParams.leftMargin;
        int tm = layoutParams.topMargin;
        int rm = layoutParams.rightMargin;
        int bm = layoutParams.bottomMargin;

        elementData.bm = bm;
        elementData.lm = lm;
        elementData.rm = rm;
        elementData.tm = tm;

        return elementData;
    }

    public String getSensor_id(){
        return sensor_id;
    }

    public String getName(){
        return textView.getText().toString();
    }

    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
