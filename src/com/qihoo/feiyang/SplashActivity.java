package com.qihoo.feiyang;

import java.io.InputStream;
import java.util.ArrayList;

import com.qihoo.feiyang.util.AlbumUtil;
import com.qihoo.feiyang.util.DBUtil;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.GlobalsUtil;
import com.qihoo.feiyang.util.LoginUtil;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		
		final Handler h=new Handler( ){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if(msg.what==0x1234){
					Intent intent=new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
				}
				
				
			}
		};
		
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					try {
				        
						
						Thread.sleep(2000);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				h.sendEmptyMessage(0x1234);
				getContacts();
			}
		});
		thread.start();
		
		
		
	}
	

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		finish();
		
		System.out.println("splash restart");
	}
	

	private void getContacts(){
		if(GlobalsUtil.contactNames==null){
			 
			System.out.println("getting contacts in  splash activity...");

			GlobalsUtil.contactIds=new ArrayList<Long>();
			GlobalsUtil.contactNames=new ArrayList<String>();
			GlobalsUtil.contactPhones=new ArrayList<String>();
			GlobalsUtil.contactAvatars=new ArrayList<Bitmap>();
			
			String phoneNumber=null;
			String contactName=null;
			Bitmap avatar=null;
			
			
			Cursor cursor=getContentResolver().query(Phone.CONTENT_URI, GlobalsUtil.contactInfo, null, null, null);
			
			while(cursor!=null&&cursor.moveToNext()){
				
				phoneNumber= cursor.getString(GlobalsUtil.PHONES_NUMBER_INDEX);  
				if(phoneNumber==null||phoneNumber.length()==0)
					continue;
				 
				contactName=cursor.getString(GlobalsUtil.PHONES_DISPLAY_NAME_INDEX);
								
				Long contactid = cursor.getLong(GlobalsUtil.PHONES_CONTACT_ID_INDEX);  
			    //得到联系人头像ID  
			    Long photoid = cursor.getLong(GlobalsUtil.PHONES_PHOTO_ID_INDEX);  
			    //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
			    if(photoid > 0 ) {  
			         Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
			         InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);  
			         avatar = BitmapFactory.decodeStream(input);  
			    }else{  
			         avatar = BitmapFactory.decodeResource(getResources(), R.drawable.contactlistitem_avatar);  
			    } 
				
				
			    GlobalsUtil.contactNames.add(contactName);
			    GlobalsUtil.contactPhones.add(phoneNumber);
			    GlobalsUtil.contactAvatars.add(avatar);
			    GlobalsUtil.contactIds.add(contactid);
				

			}
			cursor.close();
			GlobalsUtil.contactGot=true;
            
            System.out.println("got contacts in splash activity");
		}
	}

}
