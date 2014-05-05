package com.qihoo.feiyang.directory;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.contact.ContactActivity;
import com.qihoo.feiyang.favorite.FavoriteFileActivity;
import com.qihoo.feiyang.picture.PictureClassifyActivity;
import com.qihoo.feiyang.share.ShareActivity;
import com.qihoo.yunpan.sdk.android.config.YunpanSDKConfig;
import com.qihoo.yunpan.sdk.android.config.YunpanSDKConstants;
import com.qihoo.yunpan.sdk.android.http.action.UserIntfLogin;
import com.qihoo.yunpan.sdk.android.http.model.UserCenterInfo;
import com.qihoo.yunpan.sdk.android.http.model.UserLoginInfo;
import com.qihoo.yunpan.sdk.android.model.IYunpanInterface;
import com.qihoo.yunpan.sdk.android.task.LoginYunpanAction;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class DirectoryActivity extends FragmentActivity implements IYunpanInterface{
	private ViewPager view_file_list = null;
	private TextView text_dock_send = null;
	private TextView text_dock_receive = null;
	private TextView text_dock_history = null;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.directory_main);
        
        
        text_dock_send = (TextView) findViewById(R.id.directory_dock_send);
		
        view_file_list = (ViewPager) findViewById(R.id.directory_file_list);
        
        view_file_list.setAdapter(new ContentPagerDirecotryAdapter(getSupportFragmentManager(), this));
        view_file_list.setOnPageChangeListener(new OnDockChangeListener());
        
    }
	
    public void OnSwitch(View source){
    	Intent intentSwitch = null;
    	switch(source.getId()){
    		case R.id.directory_switch_share: 
    			intentSwitch = new Intent(DirectoryActivity.this, ShareActivity.class);
    			this.startActivity(intentSwitch);
    			break;
    		case R.id.directory_switch_contact: 
    			intentSwitch = new Intent(DirectoryActivity.this, ContactActivity.class);
    			this.startActivity(intentSwitch);
    			break;
    		case R.id.directory_switch_favorite: 
    			intentSwitch = new Intent(DirectoryActivity.this, FavoriteFileActivity.class);
    			this.startActivity(intentSwitch);
    			break;
    		case R.id.directory_switch_photo: 
    			intentSwitch = new Intent(DirectoryActivity.this, PictureClassifyActivity.class);
    			this.startActivity(intentSwitch);
    			break;
    	}
    }
    
    
    public class OnDockChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
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
	
}
