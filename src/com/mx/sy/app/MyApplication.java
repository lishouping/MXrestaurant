package com.mx.sy.app;

import com.mx.sy.utils.ImageLoader;
import com.mx.sy.utils.ImageLoader.Type;

import android.app.Application;
public class MyApplication extends Application {
	public static ImageLoader mLoader;
	@Override
	public void onCreate() {
		super.onCreate();
		mLoader = ImageLoader.getInstance(3, Type.LIFO);
	}

}
