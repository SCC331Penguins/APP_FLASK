package app.android.scc331.rest_test.RoomMaker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.UUID;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.R;

/**
 * Created by Alex Stout on 13/02/2018.
 */

public class Element extends FrameLayout implements Serializable{

    public static final int[] SMALL_BOX = new int[]{200,200};
    public static final int[] MEDIUM_BOX = new int[]{350,350};
    public static final int[] LARGE_BOX = new int[]{450,450};

    public static final int[] SMALL_RECT = new int[]{200,400};
    public static final int[] MEDIUM_RECT = new int[]{250,400};
    public static final int[] LARGE_RECT = new int[]{300,600};

    public static final int[] SMALL_RECT_R = new int[]{400,200};
    public static final int[] MEDIUM_RECT_R = new int[]{400,250};
    public static final int[] LARGE_RECT_R = new int[]{600,300};

    private int type;

    private String id = UUID.randomUUID().toString();

    private TextView label;

    public Element(Context context, int[] type){
        super(context);
        this.setLayoutParams(new FrameLayout.LayoutParams(type[0], type[1]));
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(5, 0xFF000000); //black border with full opacity
        this.setBackground(border);

        label = new TextView(context);
        label.setGravity(Gravity.CENTER);
        label.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        label.setTextColor(Color.BLACK);
        label.setTextSize(12);

        this.addView(label);

    }

    public ElementData dump(){
        ElementData elementData = new ElementData();
        int w = this.getLayoutParams().width;
        int h = this.getLayoutParams().height;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.getLayoutParams();
        int lm = layoutParams.leftMargin;
        int tm = layoutParams.topMargin;
        int rm = layoutParams.rightMargin;
        int bm = layoutParams.bottomMargin;

        elementData.label_name = label.getText().toString();
        elementData.type[0] = w;
        elementData.type[1] = h;
        elementData.bm = bm;
        elementData.lm = lm;
        elementData.rm = rm;
        elementData.tm = tm;

        return elementData;
    }

    public boolean equals(Element element){
        Log.d("EQUALS", ""+this.id + " | " + element.id);
        return (this.id.equals(element.id));
    }

    public static Element initFromSave(Context context, ElementData elementData){
        //Set size
        Element e = new Element(context, elementData.type);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) e.getLayoutParams();
        layoutParams.leftMargin = elementData.lm;
        layoutParams.rightMargin = elementData.rm;
        layoutParams.topMargin = elementData.tm;
        layoutParams.bottomMargin = elementData.bm;
        e.setLabel(elementData.label_name);
        return e;
    }

    public void setLabel(String label){
        this.label.setText(label);
    }

    public String getlabel(){
        return this.label.getText().toString();
    }
}
