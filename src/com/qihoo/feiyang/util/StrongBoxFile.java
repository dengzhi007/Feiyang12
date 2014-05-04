package com.qihoo.feiyang.util;

/**
 * 保险箱文件
 * @author zhangshixin
 *
 */
public class StrongBoxFile {
	String name = null;
	String fullName = null;
	String nid = null;
	String pid;
	
	public StrongBoxFile(String fullName, String nid, String pid) {
		super();
		this.fullName = fullName;
		this.nid = nid;
		this.pid = pid;
		this.name = FileUtil.getFileSimpleName(fullName);
	}
	
	public StrongBoxFile(String name, String fullName, String nid, String pid) {
		super();
		this.name = name;
		this.fullName = fullName;
		this.nid = nid;
		this.pid = pid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getNid() {
		return nid;
	}
	
	public void setNid(String nid) {
		this.nid = nid;
	}
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
}
