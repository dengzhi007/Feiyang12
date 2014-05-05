package com.qihoo.feiyang.share;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qihoo.feiyang.R;
import com.qihoo.yunpan.sdk.android.http.action.LinkGetShareNodeList;
import com.qihoo.yunpan.sdk.android.http.action.LinkShareDumpFile;
import com.qihoo.yunpan.sdk.android.http.action.ShareInfoByShorturl;
import com.qihoo.yunpan.sdk.android.http.model.GeneralInfo;
import com.qihoo.yunpan.sdk.android.http.model.LinkShareInfo;
import com.qihoo.yunpan.sdk.android.http.model.ShareNodeInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindFragment extends Fragment{
	//private GridView gv_menu;
	private Animation animation = null;
	private Context context = null;
	private SMSBroadcastReceiver smsBroadcastReceiver = null;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";  
	
	private ImageView image_share_wating= null;
	private TextView text_share_search= null;
	private TextView btn_share= null;
	private Boolean isSearch = false;
	
	IntentFilter filterMessage = null;
	
	private boolean f = true;
	
	public FindFragment(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.share_receive_file, container, false);
		
		image_share_wating = (ImageView)view.findViewById(R.id.share_search_wating);
		text_share_search = (TextView)view.findViewById(R.id.share_search_text);
		btn_share = (TextView)view.findViewById(R.id.share_button_share);
		
		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_loading_anim);
		animation.setInterpolator(new LinearInterpolator());
		image_share_wating.startAnimation(animation);
		
		smsBroadcastReceiver = new SMSBroadcastReceiver();
		
		btn_share.setOnClickListener(new OnBtnStartSearchClick());
		image_share_wating.setVisibility(View.GONE);
		
		//dumpFiles();

		filterMessage = new IntentFilter();
		filterMessage.addAction(ACTION);
		filterMessage.setPriority(Integer.MAX_VALUE);
		//
		
		return view;
	}

	private class OnBtnStartSearchClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (!isSearch){
				getActivity().registerReceiver(smsBroadcastReceiver, filterMessage);
				btn_share.setText("歇歇");
				text_share_search.setText("正在查收小伙伴的分享...");
				image_share_wating.setVisibility(View.VISIBLE);
				isSearch ^= true;
			}else{
				getActivity().unregisterReceiver(smsBroadcastReceiver);
				btn_share.setText("搞起");
				text_share_search.setText("开始查收小伙伴的分享?");
				image_share_wating.setVisibility(View.GONE);
				isSearch ^= true;
			}
			
		}
		
	}
	
	public class SMSBroadcastReceiver extends BroadcastReceiver {
	    public void onReceive(Context context, Intent intent) {
	        SmsMessage msg = null;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object p : pdusObj) {
                    msg= SmsMessage.createFromPdu((byte[]) p);
                    String messageContent =msg.getMessageBody();
                    String messageSender = msg.getOriginatingAddress();
                    
                    //好友AAA用360云盘给你分享文件 http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
                    if (messageContent.contains("用360云盘给你分享文件 http://yunpan.cn/")){
                		Intent intentConfirm = new Intent();
                		intentConfirm.putExtra("messageContent", messageContent);
                		intentConfirm.putExtra("messageSender", messageSender);
                		intentConfirm.setClass(getActivity(), FileReceivedConfrim.class);
                		getActivity().startActivity(intentConfirm);
                    }
                    

                }
	        }
	    }
	}
	
	
	private void dumpFiles(){
		//http://yunpan.cn/QifV27TDAXNVq （提取码：d11c）
		//http://yunpan.cn/Qiyynphmgtfvn （提取码：aca5）
		//http://yunpan.cn/QiyypsjHpGcpr （提取码：e413）
		//http://yunpan.cn/Qiyy7rjJDPzGF （提取码：dbcf）
		String host = "http://yunpan.cn/";
		String surl = "Qiyynphmgtfvn";
		String pwd = "2f43";
		Log.i("Share", "shareNodeList");
		ArrayList<String> dumpList = new ArrayList<String>();
		ShareInfoByShorturl shareInfo = new ShareInfoByShorturl();
		LinkShareInfo linkShareInfo = shareInfo.getLinkShareInfoByShorturl(surl);
		
		LinkGetShareNodeList shareNodeList = new LinkGetShareNodeList();
		
		ShareNodeInfo shareNode = shareNodeList.getShareNodeInfo(linkShareInfo.shareInfo.sid);
		
		LinkShareDumpFile dumpFile = new LinkShareDumpFile();
		
		
		for (int i = 0; i != shareNode.nodelist.size(); ++i){
			dumpList.add(shareNode.nodelist.get(i).nid);
			Log.i("Share", shareNode.nodelist.get(i).name);
		}
		
		GeneralInfo info = dumpFile.dumpFiles(dumpList, surl, "/feiyang12/");
		Log.i("Share", "fasfds");
	}
}