package com.qihoo.feiyang;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.contact.ContactMainActivity;
import com.qihoo.feiyang.photo.PhotoActivity;
import com.qihoo.feiyang.share.ShareActivity;
import com.qihoo.feiyang.util.DBUtil;
import com.qihoo.feiyang.util.LoginUtil;
import com.qihoo.yunpan.sdk.android.model.IYunpanInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements IYunpanInterface {
    /** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        //浜戠洏鍒濆鍖�        
        LoginUtil.setYunDiskAuth(this);
        
        //鏁版嵁搴撳垵濮嬪寲
        DBUtil.init(this, 1);
        
        //test
        //setContentView(R.layout.main);
        
        
        if(LoginUtil.switchUserEnvironment(DBUtil.getQid())){
        	
        	LinearLayout ll=(LinearLayout) findViewById(R.id.layoutcontactphoto);
        	int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();
        	int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        	System.out.println("screenwidth: "+screenWidth+"  screen height: "+screenHeight);
        	//ll.setLayoutParams(new LayoutParams(100, 100));
        	//int height=ll.getLayoutParams().height;
        	//ll.setLayoutParams(new LayoutParams((screenWidth-100)/2, height));
        	setContentView(R.layout.main);
        	//setContentView(R.layout.login);
        }else{
        	setContentView(R.layout.login2);
        }
        
        
    }

	@Override
	public void onNewUserToken(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUserCookieInvalid(String arg0) {
		// TODO Auto-generated method stub
		
	}
    
	
	
    //login button callback function 
	public void onClickOfLogin(View source){
		//Toast.makeText(this, "login btn clicked", 5);
		String user="181431178@qq.com";
		String pwd="dzy123456";
		EditText username=(EditText) findViewById(R.id.usernameET);
		EditText password=(EditText) findViewById(R.id.passwordET);
		
		//user=username.getText().toString().trim();
		//pwd=password.getText().toString().trim();
		
		if(LoginUtil.login(user,pwd)){
			//Toast.makeText(this, "login success", 50).show();
			setContentView(R.layout.main);
		}else{
			Toast.makeText(this, "login fail", 50).show();
		}
		
	}
	
	
	
	// photo/contact/share button callback function 
	public void onClickOfPCS(View source){
		System.out.println("btn click");
		Intent intent=null;
		switch (source.getId()) {
		case R.id.photo:
			System.out.println("photo btn click");
			intent=new Intent(MainActivity.this,PhotoActivity.class);
			break;
		case R.id.contact:
			System.out.println("contact btn click");
			intent=new Intent(MainActivity.this,ContactMainActivity.class);
			break;
		case R.id.share:
			System.out.println("share btn click");
			intent=new Intent(MainActivity.this,ShareActivity.class);
			break;	
		default:
			break;
		}
		
		startActivity(intent);
		
	}
	
	
    
}