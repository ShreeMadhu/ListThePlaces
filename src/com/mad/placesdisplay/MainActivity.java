package com.mad.placesdisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

	private JSONObject placeDownloadObj;
	private CustomProgressDialog dialog;

	private ListView placeList;
	private PlaceListAdapter adapter;

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String stateSaved = savedInstanceState
				.getString(Constants.SORTED_PLACE_LIST_KEY);
		if (stateSaved != null) {
			try {
				placeDownloadObj = new JSONObject(stateSaved);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			releaseTheList();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (placeDownloadObj != null) {
			outState.putString(Constants.SORTED_PLACE_LIST_KEY,
					placeDownloadObj.toString());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CustomToolBar.applyCustomToolBar(this);
		CustomToolBar.setTitle("Restaurant Offers");
		CustomToolBar.removeLeftImage();

		placeList = (ListView) findViewById(R.id.placeList);

		if (placeDownloadObj == null) {
			dialog = new CustomProgressDialog(this);
			dialog.showDialog();

			new PlaceListDownload(this, Constants.PLACE_DOWNLOAD_URL,
					placeDownloadHandler).downloadAndIntimate();
		}
	}

	private Handler placeDownloadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int downloadStatus = msg.getData().getInt("what");
			if (downloadStatus == Constants.PLACE_DOWNLOAD_SUCCESS) {
				placeDownloadObj = (JSONObject) msg.obj;
				releaseTheList();
				dialog.dismissDialog();
			}
		}
	};

	public void releaseTheList() {
		JSONArray sortedArray = null;
		try {
			sortedArray = placeDownloadObj
					.getJSONArray(Constants.SORTED_PLACE_LIST_KEY);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (adapter == null) {
			adapter = new PlaceListAdapter(MainActivity.this, sortedArray);
		}

		placeList.setAdapter(adapter);

	}
}
