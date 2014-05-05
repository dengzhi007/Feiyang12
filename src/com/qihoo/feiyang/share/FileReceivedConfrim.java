package com.qihoo.feiyang.share;

import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.yunpan.sdk.android.http.action.LinkGetShareNodeList;
import com.qihoo.yunpan.sdk.android.http.action.LinkShareDumpFile;
import com.qihoo.yunpan.sdk.android.http.action.ShareInfoByShorturl;
import com.qihoo.yunpan.sdk.android.http.model.GeneralInfo;
import com.qihoo.yunpan.sdk.android.http.model.LinkShareInfo;
import com.qihoo.yunpan.sdk.android.http.model.ShareNodeInfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FileReceivedConfrim extends Activity{

	TextView btn_dump = null;
	TextView btn_ignore = null;
	String messageContent = null;
	//String shorturl = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_file_received_confirm);
		
		Intent intentRecv = getIntent();
        messageContent = intentRecv.getStringExtra("messageContent");
        //String messageSender = intentRecv.getStringExtra("messageSender");
        TextView text_recieve_file_info = (TextView)findViewById(R.id.share_received_file_info);
        text_recieve_file_info.setText(messageContent);
        //好友AAA用360云盘给你分享文件 http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
        btn_dump = (TextView)findViewById(R.id.share_file_received_dump);
        btn_ignore = (TextView)findViewById(R.id.share_file_received_ignore);
        btn_dump.setOnClickListener(new OnDumpClickListener());
        btn_ignore.setOnClickListener(new OnIgnoreClickListener());
	}
	
    private String subStr(String str, String prefix, String postfix)
    {
        int sIndex = str.indexOf(prefix, 0) + prefix.length();
        int eIndex = 0;
        if (sIndex > prefix.length() - 1)
        {
            eIndex = str.indexOf(postfix, sIndex);
        }

        return (eIndex > sIndex) ? str.substring(sIndex, eIndex) : "";
    }
	
	private class OnDumpClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//转存到我的网盘
			//好友AAA用360云盘给你分享文件 http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
			String surl = subStr(messageContent, "360云盘给你分享文件 http://yunpan.cn/", " ，");
			String pwd = subStr(messageContent, "提取码：", "【360】");
			//Toast.makeText(FileReceivedConfrim.this, surl + "\n" + pwd, Toast.LENGTH_LONG).show();
			
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
			
			
			FileReceivedConfrim.this.finish();
		}
		
	}
	
	private class OnIgnoreClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			FileReceivedConfrim.this.finish();
		}	
	}
}
