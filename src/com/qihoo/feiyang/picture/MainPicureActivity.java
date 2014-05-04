package com.qihoo.feiyang.picture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.yunpan.sdk.android.TypeFileAction;
import com.qihoo.yunpan.sdk.android.TypeFileAction.CategoryType;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeList;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeList.OrderType;
import com.qihoo.yunpan.sdk.android.http.model.FileNodeList;
import com.qihoo.yunpan.sdk.android.http.model.YunFile;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;
import com.stay.pull.lib.PullToRefreshGridView;

/**
 * 不需要传入任何的数据
 * @author zhangshixin
 *
 */
public class MainPicureActivity extends Activity {
	
	private PullToRefreshGridView refreshGridView = null;
	private GridView gridView =null;
	private LoadDiskPictureThread thread = null;
	private Handler handler = null;
	private ThumbPictureAdapter adapter = null;
	private View backward = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_main);
		refreshGridView = (PullToRefreshGridView) findViewById(R.id.pict_gallery);
		gridView = refreshGridView.getRefreshableView();
		adapter = new ThumbPictureAdapter(this, new MainPictureViewAdd());
		backward = findViewById(R.id.photo_return);
		backward.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				MainPicureActivity.this.finish();
			}
		});
		//loadAllPictures();
		gridView.setAdapter(adapter);
		refreshGridView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				System.out.println("hehehe");
			}
			
		});
		handler = new ThumbHandler(adapter);
		thread = new LoadDiskPictureThread(handler);
		thread.start();
	}
	
	@Override
	protected void onDestroy() {
		if (thread != null) {
			thread.setStop();
		}
		super.onDestroy();
	}
}



class LoadDiskPictureThread extends Thread{
	private Handler handler;
	private volatile boolean isRun = true;
	
	public LoadDiskPictureThread(Handler handler) {
		this.handler = handler;
		setName("Download Thread");
	}
	
	@Override
	public void run() {
		loadAlbum();
	}
	
	private void loadAlbum() {
		TypeFileAction fileAction = new TypeFileAction();
		fileAction.setThumb(true);
		fileAction.setPreview(true);
		fileAction.setOrderParam(OrderType.mtime);
		
		FileNodeList fileNodeList = fileAction.getFileTypeList(CategoryType.ALBUM);
		List<YunFile> picts = fileNodeList.data.node_list;
		
		for (YunFile f : picts) {
			Bitmap map = FileUtil.getThumbBitMapIfNecessary(f);
			Message msg = handler.obtainMessage();
			Bundle data = new Bundle();
			
			data.putString("name", f.name);
			data.putString("pid", f.pid);
			data.putString("nid", f.nid);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	}
	
	public void setStop() {
		this.isRun = false;
	}
	
	private boolean isRuning() {
		return this.isRun;
	}
}
