package app.android.scc331.rest_test.Fragements;


import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.michaelflisar.dragselectrecyclerview.DragSelectTouchListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.Objects.TriggerCondition;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.SetScriptRestOperation;
import app.android.scc331.rest_test.Util.RecyclerViewAdapter;


/**
 * Created by Nikola on 14/02/2018.
 */

public class TriggerConditionManagerFragment extends Fragment implements ServiceDialogFragment.NoticeDialogListener {

    public static ArrayList<TriggerCondition> conditions = new ArrayList<>();
    private ArrayList<String> actuators = new ArrayList<>();
    private String triggerConditionString= "";
    private TextView tv;
    public static String router_id;
    public static TriggerCondition first;
    public static TriggerCondition second;
    public static boolean firstDrop = true;
    public static boolean group = false;
    public static DragSelectTouchListener mDragSelectTouchListener;
    private String actuator;
    private String feedback ;
    private RecyclerView tcList;
    private TextView bin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_trigger_condition_manager, container, false);
        Bundle bundle = this.getArguments();
        tcList = view.findViewById(R.id.tc_list_view);
		bin = view.findViewById(R.id.bin);
		displayList();
		DragSelectTouchListener.OnAdvancedDragSelectListener onDragSelectionListener = new DragSelectTouchListener.OnAdvancedDragSelectListener()
		{
			@Override
			public void onSelectChange(int start, int end, boolean isSelected)
			{

				if(isSelected)
				{
					conditions.get(start).view.setBackgroundColor(Color.MAGENTA);
					conditions.get(start).view.invalidate();
				}
				else
				{
					conditions.get(start).view.setBackgroundColor(Color.TRANSPARENT);
					conditions.get(start).view.invalidate();
				}
			}

			@Override
			public void onSelectionStarted(int start)
			{
				conditions.get(start).view.setBackgroundColor(Color.MAGENTA);
				conditions.get(start).view.invalidate();
			}

			@Override
			public void onSelectionFinished(int end) {
				System.out.println("Finished drag: " + end);
				group = false;
				for (TriggerCondition tc :
						conditions)
				{
					tc.view.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		};

		mDragSelectTouchListener = new DragSelectTouchListener()
				// check region OnDragSelectListener for more infos
				.withSelectListener(onDragSelectionListener)
				// following is all optional
				.withMaxScrollDistance(16)    // default: 16; 	defines the speed of the auto scrolling
				.withTopOffset(0)       // default: 0; 		set an offset for the touch region on top of the RecyclerView
				.withBottomOffset(0)    // default: 0; 		set an offset for the touch region on bottom of the RecyclerView
				.withScrollAboveTopRegion(true)  // default: true; 	enable auto scrolling, even if the finger is moved above the top region
				.withScrollBelowTopRegion(true)  // default: true; 	enable auto scrolling, even if the finger is moved below the top region
				.withDebug(false);
		tcList.addOnItemTouchListener(mDragSelectTouchListener);


        if (bundle != null)
        {
            router_id = bundle.getString("router_id", "o");
            System.out.println(router_id);
        }
        view.findViewById(R.id.addItem).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	registerForContextMenu(v);
            	v.showContextMenu();

            }
        });

        view.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	update(conditions);
                SetScriptRestOperation setScriptRestOperation = new SetScriptRestOperation(getContext());
                /*String encodeURL = null;
                try {
                    encodeURL = URLEncoder.encode( triggerConditionString, "UTF-8" );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                setScriptRestOperation.Start(encodeURL, router_id);*/
            }
        });

        bin = view.findViewById(R.id.bin);
        bin.setOnDragListener(new View.OnDragListener()
		{
			@Override
			public boolean onDrag(View view, DragEvent dragEvent)
			{
				// Defines a variable to store the action type for the incoming event
				final int action = dragEvent.getAction();
				// Handles each of the expected events
				switch(action)
				{
					case DragEvent.ACTION_DRAG_STARTED:
						return true;

					case DragEvent.ACTION_DRAG_ENTERED:

						view.setBackgroundColor(Color.RED);
						// Invalidate the view to force a redraw in the new tint
						view.invalidate();
						return true;

					case DragEvent.ACTION_DRAG_LOCATION:
						return true;

					case DragEvent.ACTION_DRAG_EXITED:

						view.setBackgroundColor(Color.TRANSPARENT);
						// Invalidate the view to force a redraw in the new tint
						view.invalidate();
						return true;

					case DragEvent.ACTION_DROP:
						if(first.previous != null)
							first.previous.next = null;
						conditions.remove(first);
						if(conditions.size() <= 1)
							firstDrop = true;
						displayList();
						view.setBackgroundColor(Color.TRANSPARENT);
						// Invalidates the view to force a redraw
						view.invalidate();
						// Returns true. DragEvent.getResult() will return true.
						return true;

					case DragEvent.ACTION_DRAG_ENDED:
						view.setVisibility(View.GONE);
						// Turns off any color tinting
						view.setBackgroundColor(Color.TRANSPARENT);
						// Invalidates the view to force a redraw
						view.invalidate();
						// returns true; the value is ignored.
						return true;

					// An unknown action type was received.
					default:
						break;
				}
				return false;
		}
	});
        tv = view.findViewById(R.id.tv);
        tv.setText(feedback);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        return view;
    }

    public void update(ArrayList<TriggerCondition> updatedConditions)
    {
        conditions = updatedConditions;
        triggerConditionString = "while True:\n" +
                "    if(";
        TriggerCondition head = conditions.get(0);
        if(head.next != null)
		{
			do
			{
				if (head.logicalOperator != null)
					triggerConditionString += " " + head.logicalOperator + " ";
				triggerConditionString += "sensors[\"" + head.sensorName + "\"].";
				triggerConditionString += head.metric + head.relationalOperator + head.threshold;
				head = head.next;
			} while (head != null);
		}
        triggerConditionString += "):\n" +
                "        print (\"" + actuator+"\")\n" +
                "        time.sleep(3)\n";
        if(tv != null)
			tv.setText(triggerConditionString);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        //group item order
        if(v.getId() == R.id.addItem)
		{
			menu.setHeaderTitle("Select item to add");
			menu.add(0, 0, ContextMenu.NONE,"Trigger Condition");
			menu.add(0, 1, ContextMenu.NONE,"Actuator Action");
			menu.add(0, 2, ContextMenu.NONE,"Preset High Level Service");
			menu.add(0, 3, ContextMenu.NONE,"Group Conditions Together");
		}
		else
		{
			menu.setHeaderTitle("Select action");
			menu.add(1, 0, ContextMenu.NONE,"And together");
			menu.add(1, 1, ContextMenu.NONE,"Or together");
		}
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
		int itemid  = item.getItemId();
    	if(item.getGroupId() == 1)
		{
			switch (itemid)
			{
				case 0:
					first.logicalOperator = "and";
					break;
				case 1:
					first.logicalOperator = "or";
					break;
			}

			first.previous = second;
			second.next = first;
			first.next = null;
			if (firstDrop)
			{
				Collections.swap(conditions, conditions.indexOf(second), 0);
				Collections.swap(conditions, conditions.indexOf(first), 1);
				firstDrop = false;
			}
			else
			{
				Collections.swap(conditions, conditions.indexOf(first), conditions.indexOf(second) + 1);
			}
			conditions.get(0).logicalOperator = null;
			update(conditions);
			displayList();
		}
		else
		{
			ServiceDialogFragment newFragment = new ServiceDialogFragment();
			Bundle bundle = new Bundle();
			switch (itemid)
			{
				case 0:
					((TriggerConditionsMainFragment)getParentFragment()).replaceFragment(new TriggerConditionBuilderFragment());
					break;
				case 1:
					if(MainActivity.actuators != null)
					{
						for (int i = 0; i < MainActivity.actuators.size(); i++)
							actuators.add(MainActivity.actuators.get(i).getType());
						bundle.putStringArray("values",Arrays.copyOf(actuators.toArray(), actuators.toArray().length, String[].class));
						bundle.putString("title", "Select an actuator");
						newFragment.setArguments(bundle);
						newFragment.show(getChildFragmentManager(), "service");
					}
					break;
				case 2:
					bundle.putStringArray("values", new String[]{"Smart Lights"});
					bundle.putString("title", "Select a service");
					newFragment.setArguments(bundle);
					newFragment.show(getChildFragmentManager(), "service");
					break;
				case 3:
					group = true;
					break;
			}
		}
        return true;
    }


    @Override
    public void onDialogClick(int mode, String result)
    {
        if(mode == 0)
        {
            ServiceDialogFragment newFragment = new ServiceDialogFragment();
            Bundle bundle = new Bundle();
            ArrayList<String> sensors = new ArrayList<>();
            if(MainActivity.sensors != null) {
                for (int i = 0; i < MainActivity.sensors.size(); i++) {
                    if(MainActivity.sensors.get(i).getConfig()[0])
                        sensors.add(MainActivity.sensors.get(i).getId());
                }
            }
            bundle.putStringArray("values", Arrays.copyOf(sensors.toArray(), sensors.toArray().length, String[].class));
            bundle.putString("title", "Choose a sensor to use for service");
            newFragment.setArguments(bundle);
            newFragment.show(getChildFragmentManager(), "service");
        }
        else if (mode == 1)
        {
            String triggerConditionString1 = "while True:\n" +
                    "    if(";
            triggerConditionString1 += "sensors[\"" + result + "\"].";
            triggerConditionString1 += "light" + "<" + "30";

            triggerConditionString1 += "):\n" +
                    "        print (\"lights turn on\")\n" +
                    "        time.sleep(3)";

            SetScriptRestOperation setScriptRestOperation = new SetScriptRestOperation(getContext());
            String encodeURL = null;
            try
			{
                encodeURL = URLEncoder.encode( triggerConditionString1, "UTF-8" );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), triggerConditionString1, Toast.LENGTH_LONG).show();
            setScriptRestOperation.Start(encodeURL, router_id);
        }
        else if (mode == 2)
        {
            feedback += " actuator " + result + " triggers\n";
            tv.setText(feedback);
        }
    }

    public void displayList()
	{
        /*TriggerConditionListAdapter triggerConditionListAdapter = new TriggerConditionListAdapter(getActivity());
            tcList.setAdapter(triggerConditionListAdapter);
            triggerConditionListAdapter.notifyDataSetChanged();*/


		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		tcList.setHasFixedSize(true);

		// use a linear layout manager
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		tcList.setLayoutManager(mLayoutManager);
		tcList.setAdapter( new RecyclerViewAdapter(bin, this, (TriggerConditionsMainFragment) getParentFragment()));

	}

	public void showMenu()
	{
		registerForContextMenu(tcList);
		tcList.showContextMenu();
	}
}