package app.android.scc331.rest_test.RoomMaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.android.scc331.rest_test.R;

public class SensorRouterBar extends FrameLayout {

    private TextView label;

    public static final int ROUTER = 1;
    public static final int SENSOR = 2;

    private String name;

    private Context context;

    public SensorRouterBar(@NonNull Context context, int type , String name) {
        super(context);

        this.name = name;
        this.context = context;

        label = new TextView(context);
        label.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        label.setTextColor(Color.WHITE);
        label.setPadding(dpToPx(10),0,dpToPx(10),0);
        label.setTypeface(null, Typeface.BOLD);
        label.setText(name);
        label.setTextSize(16);

        this.addView(label);

        this.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(25)));

        Drawable d = getResources().getDrawable(R.drawable.rounded_box);

        if(type == ROUTER) {
            d.setTint(Color.parseColor("#4444FF"));
        }else if(type == SENSOR){
            d.setTint(Color.parseColor("#FF4444"));
        }
        this.setBackground(d);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.getLayoutParams();
        layoutParams.leftMargin = 15;
        layoutParams.rightMargin = 8;
        this.setLayoutParams(layoutParams);
    }

    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
