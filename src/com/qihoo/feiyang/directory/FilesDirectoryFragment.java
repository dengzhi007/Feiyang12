package com.qihoo.feiyang.directory;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
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
import android.widget.Toast;

public class FilesDirectoryFragment extends Fragment implements OnClickListener {
	private ListView lv_files;
	private static List<FileDirectoryItem> fileList = new ArrayList<FileDirectoryItem>();
	private static ImageView btnFolderBackward = null;
	private static ImageView btnFavorite = null;
	private Context context;
	private static String currentFolderPath = "/";
	private static FileDirectoyItemAdapter adapter = null;
	private Boolean isSetFavoriteFlag = false;
	
	class favoriteItem{
		String path;
		String nid;
		String pid;
		
		public favoriteItem(String path, String nid, String pid){
			this.path = path;
			this.nid = nid;
			this.pid = pid;
		}

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if (((favoriteItem)o).path.equals(this.path)
			    && ((favoriteItem)o).nid.equals(this.nid)
			    && ((favoriteItem)o).pid.equals(this.pid)){
				return true;
			}
			return false;
		}
	}
	
	List<favoriteItem> shareFileList = new ArrayList<favoriteItem>();
	
	public Boolean isSetFavorite(List<favoriteItem> shareFileList){
		if (shareFileList.isEmpty()){
			return true;
		}
		for (int i = 0; i != shareFileList.size(); ++i){
			if (!StrongBoxAndFavoriteUtil.ifFileIsFavorite(shareFileList.get(i).nid)){
				return true;
			}
		}
		return false;
	}
	
	public FilesDirectoryFragment(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.directory_fragment_files, container, false);
		findView(view);
		btnFolderBackward = (ImageView)getActivity().findViewById(R.id.directory_backward);
		btnFavorite = (ImageView)getActivity().findViewById(R.id.directory_button_favorite);
		btnFolderBackward.setOnClickListener(new FolderBackwardListener());
		btnFavorite.setOnClickListener(new BtnShareListener());
		
		
		adapter = FileDirectoyItemAdapter.instance(getActivity(), fileList, this);
		refreshFileList(currentFolderPath);
		lv_files.setOnItemClickListener(new FolderClickListener());
		lv_files.setAdapter(adapter);
		
		return view;
	}

	private void findView(View v) {
		lv_files = (ListView) v.findViewById(R.id.directory_files);
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
			String nid = yunpanFileList.get(i).nid;
			String pid = yunpanFileList.get(i).pid;
			String fileInfo = "";
			if (!isFolder){
				fileInfo = fileSize + " 修改日期:" + fileMTime;
			}
			fileList.add(new FileDirectoryItem(fileIcon, fileName, fileInfo, isFolder, filePath, nid, pid, false));
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
			
			for (int i = 0; i != shareFileList.size(); ++i){
				String path = shareFileList.get(i).path;
				String nid = shareFileList.get(i).nid;
				String pid = shareFileList.get(i).pid;
				if (isSetFavoriteFlag){
					StrongBoxAndFavoriteUtil.addFileIntoFavorite(path, nid, pid);
				}else{
					StrongBoxAndFavoriteUtil.removeFileFromFavorite(nid);
				}
			}
			refreshFileList(currentFolderPath);
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_favorite);
			if (isSetFavoriteFlag){
				Toast.makeText(getActivity(), "添加收藏成功！", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onClick(View v) {//OnCheckboxClick
		fileList.get((Integer)v.getTag()).isChecked ^= true;
		CheckBox checkbox = (CheckBox)v.findViewById(R.id.directory_file_item_favorite);
		int index = (Integer)v.getTag();
		String path = fileList.get(index).fileName;
		String nid = fileList.get(index).nid;
		String pid = fileList.get(index).pid;
		
		if (fileList.get(index).isChecked){//add
			shareFileList.add(new favoriteItem(path, nid, pid));
			checkbox.setBackgroundResource(R.drawable.share_file_item_selected);
			
		}else{//del
			shareFileList.remove(new favoriteItem(path, nid, pid));
			checkbox.setBackgroundResource(R.drawable.share_file_item_unselected);
		}
		
		if (isSetFavorite(shareFileList)){
			isSetFavoriteFlag = true;
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_favorite);
		}
		else{
			isSetFavoriteFlag = false;
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_notfavorite);
		}
	}

	
	
	private void showPopupWindows(View v) {
		// menu.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}