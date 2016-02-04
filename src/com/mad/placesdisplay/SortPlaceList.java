package com.mad.placesdisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SortPlaceList implements Comparator<JSONObject> {

	public JSONArray getSortedPlaceList(String placeData) {

		JSONArray resultArray = null;
		JSONArray placeArray = null;
		try {
			JSONObject dataObj = new JSONObject(placeData);
			placeArray = dataObj.getJSONArray(Constants.PLACE_LIST_KEY);

			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = 0; i < placeArray.length(); i++) {
				list.add(placeArray.getJSONObject(i));
			}
			Collections.sort(list, new SortPlaceList());

			resultArray = new JSONArray(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultArray;
	}

	@Override
	public int compare(JSONObject lhs, JSONObject rhs) {
		try {
			Double d1 = lhs.getDouble(Constants.PLACE_LIST_DISTANCE_KEY);
			Double d2 = rhs.getDouble(Constants.PLACE_LIST_DISTANCE_KEY);

			int greaterOrLesser = Double.compare(d1, d2);

			if (greaterOrLesser > 0) {
				return 1;
			} else if (greaterOrLesser < 0) {
				return -1;
			} else {
				return 0;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
