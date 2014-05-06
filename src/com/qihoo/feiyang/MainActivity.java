package com.qihoo.feiyang;

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
import com.qihoo.feiyang.util.NetworkUtil;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.model.IYunpanInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IYunpanInterface {
    /** Called when the activity is first created. */

	Boolean needCaptcha=false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //initialing...
        if(!NetworkUtil.isConnected(this)){
			Toast.makeText(this, "网络未连接", 50).show();
        }
        
        LoginUtil.setYunDiskAuth(this);
        DBUtil.init(this, 1);
        FileUtil.init();
        AlbumUtil.init(this);
        StrongBoxAndFavoriteUtil.init(this, 1);
        GlobalsUtil.init();
        
        registerReceiver(GlobalsUtil.smsBroadcastReceiver, GlobalsUtil.filterMessage);
        
       

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
        
        
    }

	@Override
	public void onNewUserToken(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("new user token");
		Intent intent=new Intent();
		intent.setAction("login");
		
		startActivity(intent);
	}


	@Override
	public void onUserCookieInvalid(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("user cookie invalid");
		
		Intent intent=new Intent();
		intent.setAction("login");
		
		startActivity(intent);
	}
    
	
	
    //login button callback function 
	public void onClickOfLogin(View source){
		//Toast.makeText(this, "login btn clicked", 5);
//		String user="snser@qq.com";
//		String pwd="qihoo271828";
		//String user=null;
		//String pwd=null;
		EditText username=(EditText) findViewById(R.id.usernameET);
		EditText password=(EditText) findViewById(R.id.passwordET);
		
		String user=username.getText().toString().trim();
		String pwd=password.getText().toString().trim();
		
		if(user==null||user.equals("")){
			user="snser@qq.com";
			pwd="qihoo271828";
		}
		
		if(!NetworkUtil.isConnected(this)){
			Toast.makeText(this, "网络未连接", 50).show();
			return;
		}
		
		if(LoginUtil.login(user,pwd)){
			//Toast.makeText(this, "login success", 50).show();
			LoginUtil.getUserDetail();
			setContentView(R.layout.main);
			
			InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(inputMethodManager!=null && getCurrentFocus() != null)
			{
				inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
			RoundedImageView avatar=(RoundedImageView) findViewById(R.id.mainavatar);
        	if(avatar!=null)
        		avatar.setImageBitmap(GlobalsUtil.mainAvatar);
        	
        	TextView name=(TextView) findViewById(R.id.mainnickname);
        	if(name!=null)
        		name.setText(GlobalsUtil.nickName);
        	
        	TextView size=(TextView) findViewById(R.id.mainyunsize);
        	if(size!=null)
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
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(GlobalsUtil.smsBroadcastReceiver);
		super.onDestroy();
	}
	
}