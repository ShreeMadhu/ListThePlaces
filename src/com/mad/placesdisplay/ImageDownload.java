package com.mad.placesdisplay;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownload {

	private Context context;
	private String url;
	private ImageView placeImageView;

	public ImageDownload(Context context, ImageView imageview) {
		this.context = context;
		placeImageView = imageview;
	}

	public void downloadAndSetImage() {
		new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private Bitmap downloadUrl(String strUrl) throws IOException {
		Bitmap bitmap = null;

		bitmap = PlacesDisplayApplication.getBitmapFromMemCache(strUrl);
		if (bitmap != null) {
			return bitmap;
		}

		InputStream iStream = null;
		try {
			URL url = new URL(strUrl);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.connect();

			iStream = urlConnection.getInputStream();
			bitmap = BitmapFactory.decodeStream(iStream);
			PlacesDisplayApplication.addBitmapToMemoryCache(strUrl, bitmap);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
		}
		return bitmap;
	}

	private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
		Bitmap bitmap = null;

		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Drawable stub = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				stub = context.getResources().getDrawable(
						R.drawable.ic_action_picture, null);
			} else {
				stub = context.getResources().getDrawable(
						R.drawable.ic_action_picture);
			}
			placeImageView.setImageDrawable(stub);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				url = placeImageView.getTag().toString();
				bitmap = downloadUrl(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (url.equals(placeImageView.getTag().toString())) {
				placeImageView.setImageDrawable(null);
				placeImageView.setImageBitmap(bitmap);
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(
			HttpURLConnection urlConnection, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		InputStream is = null;
		try {
			is = urlConnection.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BitmapFactory.decodeStream(is, null, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		InputStream secondIs = null;
		try {
			secondIs = urlConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(secondIs, null, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
