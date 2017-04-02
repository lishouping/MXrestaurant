package com.mx.sy.utils;

import com.mx.sy.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Cont extends View{

	public Cont(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Paint paint = new Paint();//画笔
//		//
//		paint.setColor(Color.BLACK);
//		paint.setStrokeWidth(1);
//		canvas.drawPoint(100, 20, paint);
//		
//		canvas.drawText("这是text",100, 150, paint);
//		 
//		
//		canvas.drawCircle(100, 200, 30, paint);
//		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		canvas.drawBitmap(bitmap, 100, 300, paint);
		
	}
}
