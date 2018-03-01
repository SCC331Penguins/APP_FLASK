package app.android.scc331.rest_test.Util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import app.android.scc331.rest_test.R;
import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * Created by nikolayotov on 28/02/2018.
 */

public class WheelTextAdapter extends CursorWheelLayout.CycleWheelAdapter
{
	private Context context;
	private List<MenuItemData> menuItems;
	private LayoutInflater inflater;
	private int gravity;

	public WheelTextAdapter(Context context, List<MenuItemData> menuItems)
	{
		this.context = context;
		this.menuItems = menuItems;
		gravity = Gravity.CENTER;
		inflater = LayoutInflater.from(context);
	}

	public WheelTextAdapter(Context context, List<MenuItemData> menuItems, int gravity)
	{

		this.context = context;
		this.menuItems = menuItems;
		this.gravity = gravity;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return menuItems.size();
	}

	@Override
	public View getView(View parent, int position)
	{
		MenuItemData itemData = getItem(position);
		View root = inflater.inflate(R.layout.wheel_text_layout, null , false);
		TextView text = root.findViewById(R.id.wheel_text);
		text.setVisibility(View.VISIBLE);
		text.setTextColor(Color.BLACK);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		text.setText(itemData.title);
		if(text.getLayoutParams() instanceof FrameLayout.LayoutParams)
			((FrameLayout.LayoutParams) text.getLayoutParams()).gravity = gravity;
		return root;
	}

	@Override
	public MenuItemData getItem(int position)
	{
		return menuItems.get(position);
	}
}
