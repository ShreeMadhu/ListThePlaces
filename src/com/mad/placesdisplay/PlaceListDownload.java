package com.mad.placesdisplay;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class PlaceListDownload {

	private Context context;
	private String url;
	private JSONObject placeObj;
	private Handler callingHandler;

	public PlaceListDownload(Context context, String url, Handler callingHandler) {
		this.context = context;
		this.url = url;
		this.callingHandler = callingHandler;
	}

	public void downloadAndIntimate() {
		new DownloadTask().execute();
	}

	private String downloadUrl(String strUrl) throws IOException {
		String result = null;
		InputStream iStream = null;
		ByteArrayOutputStream fos1 = null;
		try {
			URL url = new URL(strUrl);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.connect();

			iStream = new BufferedInputStream(urlConnection.getInputStream());
			byte[] resultBuffer = new byte[1024];
			fos1 = new ByteArrayOutputStream();
			int len1 = 0;
			while ((len1 = iStream.read(resultBuffer)) > 0) {

				fos1.write(resultBuffer, 0, len1);

			}
			result = fos1.toString();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			fos1.close();
		}
		return result;
	}

	private class DownloadTask extends AsyncTask<String, Integer, JSONArray> {

		@Override
		protected JSONArray doInBackground(String... params) {
			String placeList = null;
			JSONArray sortedPlaceArray = null;
			try {
				placeList = downloadUrl(url);
				if (placeList != null) {
					placeObj = new JSONObject(placeList);
					sortedPlaceArray = new SortPlaceList()
							.getSortedPlaceList(placeList);
				}
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return sortedPlaceArray;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			JSONObject resultToBeSent = null;
			if (result != null) {
				resultToBeSent = new JSONObject();
				try {
					resultToBeSent.put(Constants.ORIGINAL_PLACE_LIST_KEY,
							placeObj);
					resultToBeSent.put(Constants.SORTED_PLACE_LIST_KEY, result);
					callingHandler.obtainMessage(
							Constants.PLACE_DOWNLOAD_SUCCESS, resultToBeSent)
							.sendToTarget();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				callingHandler.obtainMessage(Constants.PLACE_DOWNLOAD_FAILURE,
						null).sendToTarget();
			}
		}
	}

}
