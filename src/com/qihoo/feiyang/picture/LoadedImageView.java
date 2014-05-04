package com.qihoo.feiyang.picture;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LoadedImageView extends ImageView {
	private boolean loaded = true;
	
	public LoadedImageView(Context context) {
		super(context);
	}
	
	public LoadedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoadedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setLoaded(boolean isLoaded) {
		this.loaded = isLoaded;
	}
	
	public boolean isLoaded() {
		return loaded;
	}

}
