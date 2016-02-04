package com.mad.placesdisplay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomToolBar {

	private static AppCompatActivity activity;
	private static Toolbar toolbar;
	private static Context toolbarContext;
	private static ImageView back;
	private static ImageView menu;

	public static void applyCustomToolBar(Context context) {
		toolbarContext = context;
		activity = (AppCompatActivity) context;
		toolbar = (Toolbar) activity.findViewById(R.id.toolBar);
		activity.setSupportActionBar(toolbar);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customToolbar = inflater.inflate(R.layout.custom_tool_bar, null,
				false);
		toolbar.addView(customToolbar);
	}

	public static Toolbar getToolBar() {
		return toolbar;
	}

	public static void removeLeftImage() {
		back = (ImageView) activity.findViewById(R.id.leftImage);
		back.setVisibility(View.GONE);
	}

	public static void removeRightImage() {
		menu = (ImageView) activity.findViewById(R.id.rightImage);
		menu.setVisibility(View.GONE);
	}

	public static void enableRightImage() {
		menu = (ImageView) activity.findViewById(R.id.rightImage);
		menu.setVisibility(View.VISIBLE);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void applyRightImage(Drawable drawable) {
		menu = (ImageView) activity.findViewById(R.id.rightImage);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			menu.setBackground(drawable);
		} else {
			menu.setBackgroundDrawable(drawable);
		}
	}

	public static void setTitle(String title) {
		TextView titleText = (TextView) activity.findViewById(R.id.pageHeading);
		titleText.setText(title);
	}

	public static void setClickListener(View.OnClickListener onclick) {
		back = (ImageView) activity.findViewById(R.id.leftImage);
		back.setOnClickListener(onclick);
	}

	public static ImageView getMenu() {
		menu = (ImageView) activity.findViewById(R.id.rightImage);
		return menu;
	}

	public static void setRightClickListener(View.OnClickListener onclick) {
		menu = (ImageView) activity.findViewById(R.id.rightImage);
		menu.setOnClickListener(onclick);
	}

}
