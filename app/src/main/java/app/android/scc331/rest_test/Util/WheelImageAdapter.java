package app.android.scc331.rest_test.Util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.android.scc331.rest_test.R;
import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * Created by nikolayotov on 28/02/2018.
 */

public class WheelImageAdapter extends CursorWheelLayout.CycleWheelAdapter
{
	private Context context;
	private List<ImageData> menuItems;
	private LayoutInflater inflater;
	private int gravity;

	public WheelImageAdapter(Context context, List<ImageData> menuItems)
	{
		this.context = context;
		this.menuItems = menuItems;
		gravity = Gravity.CENTER;
		inflater = LayoutInflater.from(context);
	}

	public WheelImageAdapter(Context context, List<ImageData> menuItems, int gravity)
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
		ImageData imageData = menuItems.get(position);
		View root = inflater.inflate(R.layout.wheel_image_layout, null , false);
		ImageView image = root.findViewById(R.id.wheel_image);
		image.setVisibility(View.VISIBLE);
		image.setImageResource(imageData.imageResource);
		return root;
	}

	@Override
	public ImageData getItem(int position)
	{
		return menuItems.get(position);
	}
}
