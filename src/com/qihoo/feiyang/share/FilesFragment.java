package com.qihoo.feiyang.share;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.qihoo.feiyang.R;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeList;
import com.qihoo.yunpan.sdk.android.http.action.LinkCreateFile;
import com.qihoo.yunpan.sdk.android.http.model.FileNodeList;
import com.qihoo.yunpan.sdk.android.http.model.LinkCreateFileData;
import com.qihoo.yunpan.sdk.android.http.model.YunFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class FilesFragment extends Fragment implements OnClickListener {
	private ListView lv_files;
	private static List<FileItem> fileList = new ArrayList<FileItem>();
	private static ImageView btnFolderBackward = null;
	private static ImageView btnShare = null;
	private Context context;
	private static String currentFolderPath = "/";
	private static FileItemAdapter adapter = null;
	
	public FilesFragment(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.share_fragment_files, container, false);
		findView(view);
		btnFolderBackward = (ImageView)getActivity().findViewById(R.id.share_backward);
		btnShare = (ImageView)getActivity().findViewById(R.id.share_button_share);
		btnFolderBackward.setOnClickListener(new FolderBackwardListener());
		btnShare.setOnClickListener(new BtnShareListener());
		
		
		adapter = FileItemAdapter.instance(getActivity(), fileList, this);
		refreshFileList(currentFolderPath);
		lv_files.setOnItemClickListener(new FolderClickListener());
		lv_files.setAdapter(adapter);
		
		return view;
	}

	private void findView(View v) {
		lv_files = (ListView) v.findViewById(R.id.lv_files);
	}
	
	private String formatFileSize(long fileSizeByte){
		double fileSizeByteDouble = (double)fileSizeByte;
		if (fileSizeByteDouble < (double)(1024 * 1024)){
			return String.format("%.1f", fileSizeByteDouble / (double)1024) + "KB";
		}
		else if (fileSizeByteDouble < (double)(1024 * 1024 * 1024)){
			return String.format("%.1f", fileSizeByteDouble / (double)(1024 * 1024)) + "MB";
		}
		else{
			return String.format("%.1f", fileSizeByteDouble / (double)(1024 * 1024 * 1024)) + "GB";
		}
	}
	
	private String getFileName(String filePath, Boolean isFolder){
		if (isFolder){
			String s = filePath.substring(0, filePath.length() - 1);
			return s.substring(s.lastIndexOf("/") + 1);
		}
		else{
			return filePath.substring(filePath.lastIndexOf("/") + 1);
		}
	}
	
	private int getFileIcon(String fileName, Boolean isFolder){
		if (isFolder){
			return R.drawable.share_folder;
		}
		else{
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (extension.equals("apk")){
				return R.drawable.share_apk;
			}
			else if (extension.equals("avi")){
				return R.drawable.share_avi;
			}
			else if (extension.equals("epub")){
				return R.drawable.share_epub;
			}
			else if (extension.equals("mkv")){
				return R.drawable.share_mkv;
			}
			else if (extension.equals("pdf")){
				return R.drawable.share_pdf;
			}
			else if (extension.equals("rar")){
				return R.drawable.share_rar;
			}
			else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")){
				return R.drawable.share_image;
			}
			else if (extension.equals("txt") || extension.equals("doc") || extension.equals("docx") || extension.equals("ini")){
				return R.drawable.share_doc;
			}
			else if (extension.equals("zip")){
				return R.drawable.share_zip;
			}
			else{
				return R.drawable.share_file;
			}
		}
	}
	
	private void refreshFileList(String rootNodePath) {
		fileList.clear();
		//遍历文件夹
		FileNodeList fileNodeList = new FileGetNodeList().getNodeList(rootNodePath);
		List<YunFile> yunpanFileList = fileNodeList.data.node_list;
		for (int i = 0; i != yunpanFileList.size(); ++i){
			String filePath = yunpanFileList.get(i).name;
			Boolean isFolder = (yunpanFileList.get(i).type == 1);
			String fileName = getFileName(filePath, isFolder);
			String fileSize = formatFileSize(yunpanFileList.get(i).count_size);
			int fileIcon = getFileIcon(fileName, isFolder);
			String fileMTime = yunpanFileList.get(i).mtime;
			String fileInfo = "";
			if (!isFolder){
				fileInfo = fileSize + " 修改日期:" + fileMTime;
			}
			fileList.add(new FileItem(fileIcon, fileName, fileInfo, isFolder, filePath, false));
		}
		adapter.notifyDataSetInvalidated();
	}
	
	
	
	class FolderClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (fileList.get(position).isFolder){
				currentFolderPath = fileList.get(position).filePath;
				refreshFileList(currentFolderPath);
			}
		}
	}
	
	class FolderBackwardListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String parentFolderPath = "/";
			if (!currentFolderPath.equals("/")){
				String s = currentFolderPath.substring(0, currentFolderPath.length() - 1);
				parentFolderPath = s.substring(0, s.lastIndexOf("/") + 1);
			}
			currentFolderPath = parentFolderPath;
			refreshFileList(currentFolderPath);
		}
	}
	
	class BtnShareListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Log.i("Share", "BtnShareListener");
			StringBuffer sb = new StringBuffer();
			List<String> shareFileList = new ArrayList<String>();
			String firstFileName = null;
			
			for (int i = 0; i != fileList.size(); ++i){
				if (fileList.get(i).isShare){
					sb.append(fileList.get(i).filePath + "\n");
					shareFileList.add(fileList.get(i).filePath);
					if (firstFileName == null){
						firstFileName = fileList.get(i).fileName;
					}
				}
			}
			LinkCreateFileData linkInfo = new LinkCreateFile().getFileLink(shareFileList);
			//好友AAA用360云盘给你分享文件http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
//			String msgContent = "我通过360云盘给你分享了 \"" + firstFileName + "\"";
//			if (shareFileList.size() > 1){
//				msgContent += ("等" + shareFileList.size() + "三项分享");
//			}
//			msgContent += ("，查看链接:http://yunpan.cn/" + linkInfo.data.shorturl + " 提取码:" + linkInfo.data.password);
			String msgContent = "我用360云盘给你分享文件 http://yunpan.cn/" + linkInfo.data.shorturl
								+ "，提取码：" + linkInfo.data.password + "【360】";
			Uri uri = Uri.parse("smsto:");
    		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);   		
    		intent.putExtra("sms_body", msgContent);
    		getActivity().startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {//OnCheckboxClick
		fileList.get((Integer)v.getTag()).isShare ^= true;
		CheckBox checkbox = (CheckBox)v.findViewById(R.id.share_file_item_share);
		if (fileList.get((Integer)v.getTag()).isShare){
			checkbox.setBackgroundResource(R.drawable.share_file_item_selected);
		}else{
			checkbox.setBackgroundResource(R.drawable.share_file_item_unselected);
		}
	}

	
	
	private void showPopupWindows(View v) {
		// menu.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}