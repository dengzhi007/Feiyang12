package com.qihoo.feiyang.picture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.StrongBoxFile;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.task.TransferStatus;
import com.qihoo.yunpan.sdk.android.task.TransferStatus.ActionType;
import com.qihoo.yunpan.sdk.android.task.UploadTask;

public class FullScreenStrongBoxActivity extends Activity {
	public static List<StrongBoxFile> boxFiles = null;
	private List<View> picts = new ArrayList<View>();
	private int curIndex = 0;
	private TextView photo_name = null;
	private TextView photo_index = null;
	private View unencrypt = null;
	private ExecutorService exec = Executors.newFixedThreadPool(5);
	private ViewPager viewPager = null;
	private AnimationController controller = new AnimationController();
	private View backward = null;
	private MyPagerAdapter adapter = null;
	private UploadPictHandler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_strongbox_full);
		curIndex = getIntent().getIntExtra("index", 0);
		photo_name = (TextView) findViewById(R.id.photo_name);
		photo_index = (TextView) findViewById(R.id.photo_index);
		unencrypt = findViewById(R.id.photo_unencrypt);
		backward = findViewById(R.id.photo_return);
		viewPager = (ViewPager) findViewById(R.id.photo_full_imageView);
		adapter = new MyPagerAdapter();
		handler = new UploadPictHandler(this);
		
		initView();
		setBackwardClick();
		setViewPagerChangeEvent();
		setUnencryptClick();
		
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(curIndex);
		setTileInfo(curIndex);
	}
	
	private void setUnencryptClick() {
		unencrypt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 首先传输图片，成功删除数据库
				//但是马上删除View
				if(boxFiles.size() == 0) {
					return;
				}
				
				StrongBoxFile file = boxFiles.get(curIndex);
				String fullName = file.getFullName();
				UploadTask uploadTask = new UploadTask(FileUtil.getAppDownloadPath() + fullName, fullName, handler);
				handler.addUploadFile(file);
				exec.execute(uploadTask);
				Toast.makeText(FullScreenStrongBoxActivity.this, 
						"正在移动", Toast.LENGTH_SHORT).show();
				deleteViewInViewPager(curIndex);
			}
		});
	}
	
	private void deleteViewInViewPager(int index) {
		deleteViewInViewPager(picts.get(index));
	}
	
	private void deleteViewInViewPager(View v) {
		int pageIndex = adapter.removeView(viewPager, v);
		if (pageIndex == adapter.getCount()) pageIndex--;
		curIndex = pageIndex;
		viewPager.setCurrentItem(pageIndex);
		if (picts.size() != 0) {
			setTileInfo(curIndex);
		} else {
			Toast.makeText(FullScreenStrongBoxActivity.this, 
					"已经没有图片了", Toast.LENGTH_SHORT).show();
			setTitleInfoEmpty();
		}
	}
	
	private void setViewPagerChangeEvent() {
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				curIndex = index;
				setTileInfo(index);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void setBackwardClick() {
		assert backward != null;
		backward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FullScreenStrongBoxActivity.this.finish();
			}
		});
	}
	
	private void setTileInfo(int index) {
		String fullName = boxFiles.get(index).getFullName();
		photo_name.setText(FileUtil.getFileShortName(fullName));
		photo_index.setText((index+1) + "/" + boxFiles.size());
	}
	
	private void setTitleInfoEmpty() {
		photo_name.setText("");
		photo_index.setText("");
	}
	
	private void initView() {
		for (StrongBoxFile bf : boxFiles) {
			View view = getLayoutInflater().inflate(R.layout.photo_full_list_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.photo_full_item);
			Bitmap map = FileUtil.getLocalPictureFromStrongBox(bf.getFullName());
			imageView.setImageBitmap(map);
			picts.add(view);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		picts = null;
		super.onDestroy();
	}
	
	/**
	 * viewerPager adapter
	 * @author zhangshixin
	 *
	 */
	private class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return picts.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(picts.get(position));
			return picts.get(position);
		}
		
		@Override
		public void destroyItem(View container, int position, Object view) {
			((ViewPager) container).removeView(picts.get(position));
		}
		
		@Override
		public void finishUpdate(View container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
		}
		
		@Override
		public void startUpdate(View container) {
			// TODO Auto-generated method stub
			super.startUpdate(container);
		}
		
		@Override
		public int getItemPosition(Object object) {
			int index = picts.indexOf(object);
			if (index == -1) {
				return POSITION_NONE;
			}
			return index;
		}
		
		public int removeView (ViewPager pager, View v)
		{
			return removeView (pager, picts.indexOf (v));
		}
		
		public int removeView (ViewPager pager, int position) {
			pager.setAdapter (null);
			picts.remove (position);
			boxFiles.remove(position);
			pager.setAdapter (this);
			return position;
		}
		
		public View getView (int position)
		{
		    return picts.get (position);
		}
	}
}

class UploadPictHandler extends Handler {
	private Context context;
	private List<StrongBoxFile> uploadings = new ArrayList<StrongBoxFile>();
	
	public UploadPictHandler(Context context) {
		this.context = context;
	}
	
	public void addUploadFile(StrongBoxFile file) {
		uploadings.add(file);
	}
	
	@Override
	public void handleMessage(Message msg) {
		TransferStatus status = (TransferStatus) msg.obj;
		if (status.actionType == ActionType.UPLOAD_COMPLETE) {
			for (StrongBoxFile file : uploadings) {
				String fullName = file.getFullName();
				if (fullName.equals(status.remoteFileName)) {
					StrongBoxAndFavoriteUtil.removePictureFromStrongBox(file.getNid());
					uploadings.remove(file);
					Toast.makeText(context, 
							"恢复"+ status.remoteFileName + "成功", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			Toast.makeText(context, 
					"恢复"+ status.remoteFileName + " 失败，检查网络或", Toast.LENGTH_SHORT).show();
		}
	}
}





