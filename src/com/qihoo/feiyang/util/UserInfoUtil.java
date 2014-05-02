package com.qihoo.feiyang.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.qihoo.yunpan.sdk.android.http.model.UserDetail;

public final class UserInfoUtil {
	private static final String userAvatarFile = "avatar.jpg";
	
	private UserInfoUtil() {
		
	} 
	
	public static void saveUserDeatil(Activity activity, String qid, UserDetail detail) {
		SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
		
	}
	
	public static void saveUserAvatar(Bitmap bitmap) {
		
	}
	
	public static void clearUserInfo() {
		
	}
	
	
}
