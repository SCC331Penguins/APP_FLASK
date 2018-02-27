package app.android.scc331.rest_test.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import app.android.scc331.rest_test.Fragements.TriggerConditionBuilderFragment;
import app.android.scc331.rest_test.Fragements.TriggerConditionManagerFragment;
import app.android.scc331.rest_test.Fragements.TriggerConditionsMainFragment;
import app.android.scc331.rest_test.Objects.TriggerCondition;
import app.android.scc331.rest_test.R;

/**
 * Created by nikolayotov on 27/02/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
	private TriggerConditionManagerFragment managerFragment;
	private TriggerConditionsMainFragment mainFragment;
	TextView bin;


	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		// each data item is just a string in this case
		TextView id;
		TextView metric;
		TextView condition;
		ImageView icon;
		TextView logical;
		Activity context;
		View row;
		public ViewHolder(View row, Context context)
		{
			super(row);
			id = row.findViewById(R.id.sensor_id);
			metric = row.findViewById(R.id.metric_name);
			condition = row.findViewById(R.id.tv_condition);
			icon = row.findViewById(R.id.tc_image);
			logical = row.findViewById(R.id.tv_logical_operator);
			this.context = (Activity) context;
			this.row = row;
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public RecyclerViewAdapter(TextView bin, TriggerConditionManagerFragment manager, TriggerConditionsMainFragment main)
	{
		this.bin = bin;
		managerFragment = manager;
		mainFragment = main;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.trigger_condition_item, parent, false);
		//((Activity)parent.getContext()).registerForContextMenu(row);
		ViewHolder vh = new ViewHolder(row, parent.getContext());
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{

		final TriggerCondition triggerCondition = TriggerConditionManagerFragment.conditions.get(holder.getAdapterPosition());
		if (triggerCondition.metric.equals("light"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_light_sensor));
		else if (triggerCondition.metric.equals("sound"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_sound_sensor));
		else if (triggerCondition.metric.equals("motion"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_motion_sensor));
		else if (triggerCondition.metric.equals("tilt"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_tilt_sensor));
		else if (triggerCondition.metric.equals("humidity"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_humidity_sensor));
		else if (triggerCondition.metric.equals("ir"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_ir_sensor));
		else if (triggerCondition.metric.equals("temperature"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_temperature_sensor));
		else if (triggerCondition.metric.equals("uv"))
			holder.icon.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_uv_sensor));
		triggerCondition.view = holder.itemView;
		holder.id.setText(triggerCondition.sensorName);
		holder.metric.setText(triggerCondition.metric);
		holder.condition.setText(triggerCondition.relationalOperator + " " +triggerCondition.threshold);

		if(triggerCondition.logicalOperator != null)
		{
			holder.logical.setText(triggerCondition.logicalOperator);
			holder.logical.setVisibility(View.VISIBLE);
		}
		final ViewHolder temp = holder;
		holder.row.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Bundle bundle = new Bundle();
				bundle.putSerializable("tc", triggerCondition);
				TriggerConditionBuilderFragment TriggerConditionBuilderFragment = new TriggerConditionBuilderFragment();
				TriggerConditionBuilderFragment.setArguments(bundle);
				mainFragment.replaceFragment(TriggerConditionBuilderFragment);
			}
		});

		holder.row.setOnDragListener(new View.OnDragListener()
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

						// Determines if this View can accept the dragged data
						if ((!TriggerConditionManagerFragment.first.id.equals(triggerCondition.id) && triggerCondition.next == null && triggerCondition.previous != null) || (TriggerConditionManagerFragment.firstDrop && !TriggerConditionManagerFragment.first.id.equals(triggerCondition.id)))
						{
							view.setBackgroundColor(Color.GREEN);
							// Invalidate the view to force a redraw in the new tint
							view.invalidate();
							// returns true to indicate that the View can accept the dragged data.
							return true;
						}
						// Returns false. During the current drag and drop operation, this View will
						// not receive events again until ACTION_DRAG_ENDED is sent.
						return false;

					case DragEvent.ACTION_DRAG_ENTERED:

						view.setBackgroundColor(Color.BLUE);
						// Invalidate the view to force a redraw in the new tint
						view.invalidate();
						return true;

					case DragEvent.ACTION_DRAG_LOCATION:

						// Ignore the event
						return true;

					case DragEvent.ACTION_DRAG_EXITED:

						view.setBackgroundColor(Color.GREEN);
						// Invalidate the view to force a redraw in the new tint
						view.invalidate();
						return true;

					case DragEvent.ACTION_DROP:

						TriggerConditionManagerFragment.second = triggerCondition;

						view.setBackgroundColor(Color.TRANSPARENT);
						// Invalidates the view to force a redraw
						view.invalidate();
						managerFragment.showMenu();
						// Returns true. DragEvent.getResult() will return true.
						return true;

					case DragEvent.ACTION_DRAG_ENDED:

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
		holder.row.setOnLongClickListener(new View.OnLongClickListener()
		{

			public boolean onLongClick(View v)
			{
				if(TriggerConditionManagerFragment.group)
					TriggerConditionManagerFragment.mDragSelectTouchListener.startDragSelection(temp.getLayoutPosition());
				else
				{
					if (triggerCondition.next == null)
					{
						View.DragShadowBuilder myShadow = new View.DragShadowBuilder(temp.row)
						{
							// The drag shadow image, defined as a drawable thing
							private Drawable shadow = new ColorDrawable(Color.LTGRAY);

							@Override
							public void onProvideShadowMetrics(Point size, Point touch)
							{
								// Defines local variables
								int width, height;
								width = getView().getWidth();
								height = getView().getHeight();
								shadow.setBounds(0, 0, width, height);
								size.set(width, height);
								touch.set(width / 2, height / 2);
							}

							@Override
							public void onDrawShadow(Canvas canvas)
							{
								shadow.draw(canvas);
							}
						};
						TriggerConditionManagerFragment.first = triggerCondition;
						bin.setVisibility(View.VISIBLE);
						v.startDrag(null, myShadow, null, 0);
						return true;
					} else
						return false;
				}
				return true;
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return TriggerConditionManagerFragment.conditions.size();
	}
}