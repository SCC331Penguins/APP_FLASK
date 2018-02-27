package app.android.scc331.rest_test.RoomMaker;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.Objects.Sensor;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetSensorRestOperation;

import static app.android.scc331.rest_test.Services.RestPaths.TAG;

public class MapDesignFragment extends Fragment {

    private static final int MAX_DURATION = 1000;

    private static final String ARG_PARAM1 = "router_id";

    private FrameLayout mainFrame;

    private boolean saved_router;

    private int xDelta;
    private int yDelta;

    private boolean deleteMode;

    private Element lastTouched;

    private Button saveRoom;

    private long startTime;
    private int clicks;

    private String router_id;

    private LinearLayout sensorRouterBar = null;

    //Small, medium, large rectangles
    private LinearLayout sb, mb, lb, sr, mr, lr, srr, mrr, lrr;
    private TextView tsb, tmb, tlb, tsr, tmr, tlr, tsrr, tmrr, tlrr;
    private ImageView isb, imb, ilb, isr, imr, ilr, isrr, imrr, ilrr;
    private LinearLayout[] blockItems = new LinearLayout[]{sb, mb, lb, sr, mr, lr, srr, mrr, lrr};

    private ArrayList<Element> elements = new ArrayList<>();

    private RouterElement routerElementSave;

    private ArrayList<SensorElement> sensorElements = new ArrayList<>();

    private ArrayList<Sensor> sensors = new ArrayList<>();

    private Animation shake;

    public MapDesignFragment() {
    }

    public static MapDesignFragment newInstance(String router_id) {
        MapDesignFragment fragment = new MapDesignFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, router_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            router_id = getArguments().getString("router_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_design, container, false);

        //Initiate the main frame (what we are drawing on)
        mainFrame = v.findViewById(R.id.map_main_frame);

        saveRoom = v.findViewById(R.id.save_room);

        saveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ElementData> elementData = new ArrayList<>();
                for (Element element : elements)
                    elementData.add(element.dump());

                ArrayList<RouterSensorElementData> sensorElementData = new ArrayList<>();

                for (SensorElement sensorElement : sensorElements) {
                    sensorElementData.add(sensorElement.dump());
                }

                if (routerElementSave != null) {
                    MainActivity.savedState.saveRouterSensorElements(router_id, routerElementSave.dump());
                } else {
                    MainActivity.savedState.saveRouterSensorElements(router_id, null);
                }


                if (sensorElements != null)
                    MainActivity.savedState.saveSensorElements(router_id, sensorElementData);

                MainActivity.savedState.saveElements(router_id, elementData);
                MainActivity.savedState.save(getContext());


            }
        });

        shake = AnimationUtils.loadAnimation(

                getContext(), R.anim.shake);

        sensorRouterBar = v.findViewById(R.id.sensor_router_bar);

        //v.findViewById(R.id.small_box).findViewById(R.id.box_element_list).setOnClickListener(onBoxClick(Element.SMALL_BOX));

        sb = v.findViewById(R.id.small_box);
        sb.setOnClickListener(

                onBoxClick(Element.SMALL_BOX));
        tsb = sb.findViewById(R.id.element_name);
        tsb.setText("Small Box");
        isb = sb.findViewById(R.id.element_image);
        isb.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.small_box));

        mb = v.findViewById(R.id.medium_box);
        mb.setOnClickListener(

                onBoxClick(Element.MEDIUM_BOX));
        tmb = mb.findViewById(R.id.element_name);
        tmb.setText("Med Box");
        imb = mb.findViewById(R.id.element_image);
        imb.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.medium_box));

        lb = v.findViewById(R.id.large_box);
        lb.setOnClickListener(

                onBoxClick(Element.LARGE_BOX));
        tlb = lb.findViewById(R.id.element_name);
        tlb.setText("Large Box");
        ilb = lb.findViewById(R.id.element_image);
        ilb.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.large_box));

        sr = v.findViewById(R.id.small_rect);
        sr.setOnClickListener(

                onBoxClick(Element.SMALL_RECT));
        tsr = sr.findViewById(R.id.element_name);
        tsr.setText("Small Rect");
        isr = sr.findViewById(R.id.element_image);
        isr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.small_rect));

        mr = v.findViewById(R.id.medium_rect);
        mr.setOnClickListener(

                onBoxClick(Element.MEDIUM_RECT));
        tmr = mr.findViewById(R.id.element_name);
        tmr.setText("Med Rect");
        imr = mr.findViewById(R.id.element_image);
        imr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.medium_rect));

        lr = v.findViewById(R.id.large_rect);
        lr.setOnClickListener(

                onBoxClick(Element.LARGE_RECT));
        tlr = lr.findViewById(R.id.element_name);
        tlr.setText("Large Rect");
        ilr = lr.findViewById(R.id.element_image);
        ilr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.large_rect));

        srr = v.findViewById(R.id.small_rect_r);
        srr.setOnClickListener(

                onBoxClick(Element.SMALL_RECT_R));
        tsrr = srr.findViewById(R.id.element_name);
        tsrr.setText("Small Rect");
        isrr = srr.findViewById(R.id.element_image);
        isrr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.small_rect_r));

        mrr = v.findViewById(R.id.medium_rect_r);
        mrr.setOnClickListener(

                onBoxClick(Element.MEDIUM_RECT_R));
        tmrr = mrr.findViewById(R.id.element_name);
        tmrr.setText("Med Rect");
        imrr = mrr.findViewById(R.id.element_image);
        imrr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.medium_rect_r));

        lrr = v.findViewById(R.id.large_rect_r);
        lrr.setOnClickListener(

                onBoxClick(Element.LARGE_RECT_R));
        tlrr = lrr.findViewById(R.id.element_name);
        tlrr.setText("Large Rect");
        ilrr = lrr.findViewById(R.id.element_image);
        ilrr.setImageDrawable(

                getResources().

                        getDrawable(R.drawable.large_rect_r));

        final ArrayList<ElementData> elementData = MainActivity.savedState.getElements(router_id);
        final RouterSensorElementData routerSensorElementData = MainActivity.savedState.getRouterSensorElements(router_id);
        final ArrayList<RouterSensorElementData> sensorElementData = MainActivity.savedState.getSensorElements(router_id);

        if (elementData != null)
            for (ElementData element : elementData)
            {
                Log.d("ELEMENT LOAD", "Loading element: " + element.label_name);
                Element e = Element.initFromSave(getContext(), element);
                e.setOnTouchListener(onTouchListener(e));
                elements.add(e);
                mainFrame.addView(e);
            }
        if (routerSensorElementData != null)

        {
            saved_router = true;
            RouterElement routerElement = RouterElement.loadFromSave(getContext(), routerSensorElementData);
            routerElement.setOnTouchListener(onTouchListener(null));
            routerElementSave = routerElement;
            mainFrame.addView(routerElement);
        }

        if (sensorElementData != null)

        {
            for (RouterSensorElementData rsd : sensorElementData) {
                SensorElement se = SensorElement.loadFromSave(getContext(), rsd);
                se.setOnTouchListener(onTouchListener(null));
                sensorElements.add(se);
                mainFrame.addView(se);
            }
        }

        mainFrame.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick(View view) {
                deleteMode = true;
                for (Element element : elements) {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(Color.parseColor("#FF0000")); //white background
                    border.setStroke(5, 0xFF000000); //black border with full opacity
                    element.setBackground(border);
                }
                return true;
            }
        });

        mainFrame.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                deleteMode = false;
                for (Element element : elements) {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF); //white background
                    border.setStroke(5, 0xFF000000); //black border with full opacity
                    element.setBackground(border);
                }
            }
        });

        String router_name = MainActivity.savedState.getRouterName(router_id);
        if (router_name == null)

        {
            router_name = router_id;
        }

        if (routerElementSave == null)
            addBarElement(SensorRouterBar.ROUTER, router_name, null);

        GetSensorRestOperation getSensorRestOperation = new GetSensorRestOperation(getContext());
        sensors = (ArrayList<Sensor>) getSensorRestOperation.Start(router_id);

        if(sensors != null)
        for (Sensor s : sensors) {

            String s_id = s.getId();
            boolean exists = false;

            for (SensorElement sensorElement : sensorElements) {
                Log.d("CHECK SENSORS", "" + s_id + " | " + sensorElement.getSensor_id());
                if (s_id.equals(sensorElement.getSensor_id())) {
                    exists = true;
                }
            }

            if (!exists) {
                String sensor_name = MainActivity.savedState.getSensorName(s.getId());
                if (sensor_name == null)
                    sensor_name = s.getId();
                addBarElement(SensorRouterBar.SENSOR, sensor_name, s_id);
            }
        }

            return v;
        }

    private void addBarElement(int type, String name, String id) {
        Log.d("Adding elements", "" + name);
        SensorRouterBar sensorRouterBar = new SensorRouterBar(getContext(), type, name);
        sensorRouterBar.setOnClickListener(onBarClickListener(sensorRouterBar, type, name, id));
        this.sensorRouterBar.addView(sensorRouterBar);
    }

    private View.OnClickListener onBarClickListener(final View v, final int type, final String name, final String id) {
        if (type == SensorRouterBar.ROUTER) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RouterElement routerElement = new RouterElement(getContext(), name);
                    routerElement.setOnTouchListener(onTouchListener(null));
                    sensorRouterBar.removeView(v);
                    routerElementSave = routerElement;
                    mainFrame.addView(routerElement);
                }
            };
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SensorElement sensorElement = new SensorElement(getContext(), name, id);
                sensorElement.setOnTouchListener(onTouchListener(null));
                sensorRouterBar.removeView(v);
                sensorElements.add(sensorElement);
                mainFrame.addView(sensorElement);
            }
        };
    }

    private void addFrame(FrameLayout view, int[] type) {
        Element frame = new Element(getContext(), type);
        frame.setOnTouchListener(onTouchListener(frame));
        elements.add(frame);
        view.addView(frame);
    }

    private View.OnClickListener onBoxClick(final int[] type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFrame(mainFrame, type);
            }
        };
    }

    private View.OnTouchListener onTouchListener(final Element e) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams)
                                view.getLayoutParams();
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;

                        if (deleteMode) {
                            mainFrame.removeView(view);
                            elements.remove(view);
                            if (routerElementSave != null) {
                                if (view instanceof RouterElement) {
                                    addBarElement(SensorRouterBar.ROUTER, ((RouterElement) view).getName(), null);
                                    routerElementSave = null;
                                    mainFrame.removeView(view);
                                }
                            }
                            if (sensorElements.contains(view)) {
                                addBarElement(SensorRouterBar.SENSOR, ((SensorElement) view).getName(), ((SensorElement) view).getSensor_id());
                                sensorElements.remove(view);
                                mainFrame.removeView(view);
                            }
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        if (e == null)
                            break;
                        clicks++;
                        if (clicks == 1) {
                            lastTouched = (Element) view;
                            startTime = System.currentTimeMillis();
                        } else if (clicks == 2) {
                            if (!((Element) view).equals(lastTouched)) {
                                clicks = 0;
                                lastTouched = null;
                                break;
                            }

                            long duration = System.currentTimeMillis() - startTime;
                            if (duration <= 600) {
                                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                                final View vw = layoutInflater.inflate(R.layout.change_name_dialog, null);
                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setCancelable(false);
                                final EditText editname_text = (EditText) vw.findViewById(R.id.editname_edittext);
                                final Button save = vw.findViewById(R.id.save_button_dialog);
                                final Button cancel = vw.findViewById(R.id.cancel_button_dialog);

                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        e.setLabel(editname_text.getText().toString());
                                        alertDialog.cancel();
                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.cancel();
                                    }
                                });

                                alertDialog.setView(vw);
                                alertDialog.show();
                                clicks = 0;
                            } else {
                                clicks = 1;
                                startTime = System.currentTimeMillis();
                            }
                            break;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                                .getLayoutParams();

                        int leftMargin = x - xDelta;
                        int topMargin = y - yDelta;

                        layoutParams.leftMargin = leftMargin;
                        if ((leftMargin <= 0)) {
                            layoutParams.leftMargin = 0;
                        } else if (((leftMargin + view.getWidth()) >= mainFrame.getWidth()))
                            layoutParams.leftMargin = mainFrame.getWidth() - view.getWidth();

                        layoutParams.topMargin = topMargin;
                        if ((topMargin <= 0)) {
                            layoutParams.topMargin = 0;
                        } else if (((topMargin + view.getHeight()) >= mainFrame.getHeight()))
                            layoutParams.topMargin = mainFrame.getHeight() - view.getHeight();

                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;

                        Log.d("Move l/r", "" + (layoutParams.leftMargin + view.getWidth()));
                        Display d = getActivity().getWindowManager().getDefaultDisplay();

                        Log.d("Width", mainFrame.getWidth() + " : " + mainFrame.getHeight());

                        view.setLayoutParams(layoutParams);
                        break;
                }
                mainFrame.invalidate();
                return true;
            }
        };
    }

}
