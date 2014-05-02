package com.qihoo.feiyang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.Region;

public class RoundedImageView extends ImageView {
	Path path;
	public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
	Paint paint;
	
	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundedImageView(Context context) {
		super(context);
		init();
	}
	
	public void init(){
		mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	protected void onDraw(Canvas cns) {
		float h = getMeasuredHeight()- 3.0f;
		float w = getMeasuredWidth()- 3.0f;
		if (path == null) {
			path = new Path();
	        path.addCircle(
	        		w/2.0f
	                , h/2.0f
	                , (float) Math.min(w/2.0f, (h / 2.0))
	                , Path.Direction.CCW);
	        path.close();
		}
		
		//设置红边框
		paint.setColor(Color.WHITE);
		cns.drawCircle(w/2.0f, h/2.0f,  Math.min(w/2.0f, h / 2.0f) + 1.5f, paint);
		 int saveCount = cns.getSaveCount();
        cns.save();
        paint.setColor(Color.WHITE);
        
        cns.setDrawFilter(mPaintFlagsDrawFilter);
        cns.clipPath(path,Region.Op.REPLACE);
        cns.setDrawFilter(mPaintFlagsDrawFilter);
        cns.drawColor(Color.WHITE);
		super.onDraw(cns);
		cns.restoreToCount(saveCount);
	}

}