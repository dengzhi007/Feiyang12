package com.qihoo.feiyang.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	
	public static Boolean isConnected(Context context) {  
        
	       if(getNetType(context)==-1){
	    	   return false;
	       }else{
	    	   return true;
	       }
	         
	    }  
	
	
	public static Boolean isWifiAvailable(Context context){
		if(getNetType(context)==ConnectivityManager.TYPE_WIFI)
			return true;
		else
			return false;
	}
	

	private static int getNetType(Context context){
		int type=-1;
		
		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
        
	       if (manager == null) {  
	           return type;  
	       }  
	        
	       NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
	        
	       if (networkinfo == null || !networkinfo.isAvailable()) {  
	    	   return type;  
	       }else{
	    	   return networkinfo.getType();
	       }

	}
}
