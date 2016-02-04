package com.mad.placesdisplay;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

public class PlacesDisplayApplication extends Application {

	private static LruCache<String, Bitmap> mMemoryCache;

	@Override
	public void onCreate() {
		super.onCreate();
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

}
