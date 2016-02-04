package com.mad.placesdisplay;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray sortedPlaceArray;
	private LayoutInflater inflater;

	public PlaceListAdapter(Context context, JSONArray sortedPlaceArray) {
		this.context = context;
		this.sortedPlaceArray = sortedPlaceArray;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return sortedPlaceArray.length();
	}

	@Override
	public JSONObject getItem(int position) {
		JSONObject placeData = null;
		try {
			placeData = sortedPlaceArray.getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return placeData;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PlaceListViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.place_list_adapter, null);
			holder = new PlaceListViewHolder();

			holder.placeLogo = (ImageView) convertView
					.findViewById(R.id.placeLogo);
			holder.placeName = (TextView) convertView
					.findViewById(R.id.placeName);
			holder.offers = (TextView) convertView.findViewById(R.id.offers);
			holder.categories = (TextView) convertView
					.findViewById(R.id.categories);
			holder.distanceDetails = (TextView) convertView
					.findViewById(R.id.distanceDetails);
			holder.position = position;

			convertView.setTag(holder);
		} else {
			holder = (PlaceListViewHolder) convertView.getTag();
		}

		JSONObject placeDetails = getItem(position);
		String name = "";
		String offerNumber = "";
		String logoUrl = "";
		String distance = "";
		JSONArray categoriesList = null;

		try {
			name = placeDetails.getString(Constants.PLACE_LIST_NAME_KEY);
			offerNumber = placeDetails
					.getString(Constants.PLACE_LIST_OFFERS_KEY);
			double distanceInDouble = placeDetails
					.getDouble(Constants.PLACE_LIST_DISTANCE_KEY);
			double distanceInM = distanceInDouble % 1000;
			DecimalFormat newFormat = new DecimalFormat("####");
			distance = String.valueOf(Integer.valueOf(newFormat
					.format(distanceInM)));

			logoUrl = placeDetails.getString(Constants.PLACE_LIST_LOGO_KEY);
			holder.placeLogo.setTag(logoUrl);
			categoriesList = placeDetails
					.getJSONArray(Constants.PLACE_LIST_CATEGORIES_KEY);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		holder.placeName.setText(name);
		holder.offers.setText(offerNumber + " Offers");
		holder.distanceDetails.setText(distance
				+ " m from your current location");
		holder.categories.setText(renderTextAndImage(categoriesList));

		new ImageDownload(context, holder.placeLogo).downloadAndSetImage();

		return convertView;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public SpannableStringBuilder renderTextAndImage(JSONArray categories) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		try {
			if (categories != null && categories.length() > 0) {
				for (int i = 0; i < categories.length(); i++) {

					JSONObject categoryObj = categories.getJSONObject(i);

					String categoryParentId = categoryObj
							.getString(Constants.PLACE_LIST_CATEGORY_PARENT_ID_KEY);

					if (!categoryParentId.equals("null")) {
						String categoryName = categoryObj
								.getString(Constants.PLACE_LIST_CATEGORY_NAME_KEY);

						int builderLength = 0;
						if (builder.length() > 0) {
							builderLength = builder.length() - 1;
						} else {
							builder.append(" ");
							builderLength = builder.length() - 1;
						}

						int currentapiVersion = android.os.Build.VERSION.SDK_INT;
						Drawable d = null;
						if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
							d = context.getResources().getDrawable(
									R.drawable.gray_bullet, null);
						} else {
							if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
								d = context.getResources().getDrawable(
										R.drawable.gray_bullet, null);
							} else {
								d = context.getResources().getDrawable(
										R.drawable.gray_bullet);
							}
						}

						Bitmap icon = drawableToBitmap(d);

						builder.setSpan(new ImageSpan(context, icon,
								ImageSpan.ALIGN_BASELINE), builderLength,
								builder.length(), 0);
						builder.append(" ");
						builder.append(categoryName + "   ");
					}
				}
			} else {
				builder.append("Not Avaible");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return builder;
	}

	static class PlaceListViewHolder {
		public ImageView placeLogo;
		public TextView placeName;
		public TextView offers;
		public TextView categories;
		public TextView distanceDetails;
		public int position;

	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
