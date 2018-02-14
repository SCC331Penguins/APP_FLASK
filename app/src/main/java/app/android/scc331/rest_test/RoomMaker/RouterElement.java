package app.android.scc331.rest_test.RoomMaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;

public class RouterElement extends FrameLayout {

    private static final int ROUTER = 1;
    private static final int SENSOR = 2;

    private Context context;

    private LinearLayout linearLayout;

    private ImageView imageView;

    private TextView textView;

    private int id;

    public RouterElement(@NonNull Context context, String router_name) {
        super(context);

        this.context = context;

        FrameLayout.LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();

        this.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40)));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.router));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        imageView.setLayoutParams(param);

        linearLayout.addView(imageView);

        textView = new TextView(context);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        textView.setLayoutParams(params2);
        textView.setText(router_name);
        textView.setTextSize(12);
        textView.setTextColor(Color.CYAN);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        linearLayout.addView(textView);

        this.addView(linearLayout);
    }

    public static RouterElement loadFromSave(Context context, RouterSensorElementData elementData){
        RouterElement e = new RouterElement(context, elementData.id);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) e.getLayoutParams();
        layoutParams.leftMargin = elementData.lm;
        layoutParams.rightMargin = elementData.rm;
        layoutParams.topMargin = elementData.tm;
        layoutParams.bottomMargin = elementData.bm;
        return e;
    }

    public RouterSensorElementData dump(){
        RouterSensorElementData elementData = new RouterSensorElementData(textView.getText().toString(), ROUTER);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.getLayoutParams();
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

    public int getUID(){
        return id;
    }

    public void setUID(int id){
        this.id = id;
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
