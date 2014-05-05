package com.qihoo.feiyang;

import java.io.InputStream;
import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.contact.ContactMainActivity;
import com.qihoo.feiyang.directory.DirectoryActivity;
import com.qihoo.feiyang.favorite.FavoriteFileActivity;
import com.qihoo.feiyang.picture.PictureClassifyActivity;
import com.qihoo.feiyang.share.ShareActivity;
import com.qihoo.feiyang.util.AlbumUtil;
import com.qihoo.feiyang.util.DBUtil;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.GlobalsUtil;
import com.qihoo.feiyang.util.LoginUtil;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.model.IYunpanInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IYunpanInterface {
    /** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash);
        
        LoginUtil.setYunDiskAuth(this);
        
        
        DBUtil.init(this, 1);
        FileUtil.init();
        AlbumUtil.init(this);
        StrongBoxAndFavoriteUtil.init(this, 1);
        

        
        if(LoginUtil.switchUserEnvironment(DBUtil.getQid())){
        	
        	LoginUtil.getUserDetail();

        	setContentView(R.layout.main);
        	
        	RoundedImageView avatar=(RoundedImageView) findViewById(R.id.mainavatar);
        	avatar.setImageBitmap(GlobalsUtil.mainAvatar);
        	
        	TextView name=(TextView) findViewById(R.id.mainnickname);
        	name.setText(GlobalsUtil.nickName);
        	
        	TextView size=(TextView) findViewById(R.id.mainyunsize);
        	size.setText("云盘空间：" + GlobalsUtil.usedSize +"  / total " + GlobalsUtil.totalSize);
        	
        	
        }else{
        	
        	setContentView(R.layout.login);
        }
        
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getContacts();
				
			}
		});
		thread.start();
    }

	@Override
	public void onNewUserToken(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("new user token");
		
	}


	@Override
	public void onUserCookieInvalid(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("user cookie invalid");
		
	}
    
	
	
    //login button callback function 
	public void onClickOfLogin(View source){
		//Toast.makeText(this, "login btn clicked", 5);
		String user="snser@qq.com";
		String pwd="qihoo271828";
		//String user=null;
		//String pwd=null;
		EditText username=(EditText) findViewById(R.id.usernameET);
		EditText password=(EditText) findViewById(R.id.passwordET);
		
		//user=username.getText().toString().trim();
		//pwd=password.getText().toString().trim();
		
		if(LoginUtil.login(user,pwd)){
			//Toast.makeText(this, "login success", 50).show();
			LoginUtil.getUserDetail();
			setContentView(R.layout.main);
			
			RoundedImageView avatar=(RoundedImageView) findViewById(R.id.mainavatar);
        	avatar.setImageBitmap(GlobalsUtil.mainAvatar);
        	
        	TextView name=(TextView) findViewById(R.id.mainnickname);
        	name.setText(GlobalsUtil.nickName);
        	
        	TextView size=(TextView) findViewById(R.id.mainyunsize);
        	size.setText("云盘空间：" + GlobalsUtil.usedSize +"  / total " + GlobalsUtil.totalSize);
			
		}else{
			Toast.makeText(this, "login fail", 50).show();
		}
		
	}
	
	
	
	public void onClickOfMainAvatar(View source){
		AlertDialog.Builder ad=new AlertDialog.Builder(this);   
		ad.setTitle("是否退出登录");  
		ad.setPositiveButton("是", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginUtil.clearUserEnvironment();
				DBUtil.saveQid("");
				setContentView(R.layout.login);
			}
			});  
		ad.setNegativeButton("否", null);  
		ad.show();  
	}
	
	// photo/contact/share button callback function 
	public void onClickOfPCS(View source){
		System.out.println("btn click");
		Intent intent=null;
		switch (source.getId()) {
		case R.id.photo:
			System.out.println("picture btn click");
			intent=new Intent(MainActivity.this,PictureClassifyActivity.class);
			startActivity(intent);
			break;
		case R.id.contact:
			System.out.println("contact btn click");
			intent=new Intent(MainActivity.this,ContactMainActivity.class);
			startActivity(intent);
			break;
		case R.id.allfile:
			System.out.println("allfile btn click");
			intent=new Intent(MainActivity.this,DirectoryActivity.class);
			startActivity(intent);
			break;
		case R.id.share:
			System.out.println("share btn click");
			intent=new Intent(MainActivity.this,ShareActivity.class);
			startActivity(intent);
			break;
		case R.id.like:
			System.out.println("favorite btn click");
			intent = new Intent(MainActivity.this, FavoriteFileActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
		
		
	}
	
	private void getContacts(){
		if(GlobalsUtil.contactNames==null){
			 
			System.out.println("getting contacts in  main activity...");

			GlobalsUtil.contactIds=new ArrayList<Long>();
			GlobalsUtil.contactNames=new ArrayList<String>();
			GlobalsUtil.phoneNums=new ArrayList<String>();
			GlobalsUtil.avatars=new ArrayList<Bitmap>();
			
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
			    GlobalsUtil.phoneNums.add(phoneNumber);
			    GlobalsUtil.avatars.add(avatar);
			    GlobalsUtil.contactIds.add(contactid);
				

			}
			cursor.close();
			GlobalsUtil.contactGot=true;
            
            System.out.println("got contacts in main activity");
		}
	}
    
}