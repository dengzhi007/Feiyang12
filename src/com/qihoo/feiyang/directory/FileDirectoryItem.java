package com.qihoo.feiyang.directory;

public class FileDirectoryItem {
	public int fileIcon;
	public String fileName;
	public String fileInfo;
	public Boolean isFolder;
	public String filePath;
	public String nid;
	public String pid;
	public Boolean isChecked;

	public FileDirectoryItem(int fileIcon, String fileName, String fileInfo, Boolean isFoolder, 
							 String filePath, String nid, String pid, Boolean isChecked) {
		this.fileIcon = fileIcon;
		this.fileName = fileName;
		this.fileInfo = fileInfo;
		this.isFolder = isFoolder;
		this.filePath = filePath;
		this.nid = nid;
		this.pid = pid;
		this.isChecked = isChecked;
		
	}
}