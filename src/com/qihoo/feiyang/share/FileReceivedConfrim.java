package com.qihoo.feiyang.share;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.DBUtil;
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
	String host = "http://login.360.cn/";
	String referer = "http://yunpan.360.cn/";
	static String username = "";
	static String password = "";
	static List<String> nids = new ArrayList<String>();
	static String shareLink = "";
	static String previewSign = "";
	static String shorturl = "";
	static String linkPassword = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_file_received_confirm);
		
		Intent intentRecv = getIntent();
        messageContent = intentRecv.getStringExtra("messageContent");
        //String messageSender = intentRecv.getStringExtra("messageSender");
        TextView text_recieve_file_info = (TextView)findViewById(R.id.share_received_file_info);
        
        //好友AAA用360云盘给你分享文件 http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
        btn_dump = (TextView)findViewById(R.id.share_file_received_dump);
        btn_ignore = (TextView)findViewById(R.id.share_file_received_ignore);
        btn_dump.setOnClickListener(new OnDumpClickListener());
        btn_ignore.setOnClickListener(new OnIgnoreClickListener());
        
		shorturl = subStr(messageContent, "360云盘给你分享文件 http://yunpan.cn/", " ，");
        //shorturl = subStr(messageContent, "360云盘给你分享文件 http://yunpan.cn/", " ");
		linkPassword = subStr(messageContent, "提取码：", "【");
        
		try {
			httpLogin();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileDescribe = getFileDescribe(shorturl, linkPassword);
		String confirmInfo = messageContent.substring(0, messageContent.indexOf("文件"));
		
		text_recieve_file_info.setText(confirmInfo + fileDescribe);
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
    
    public String MD5Encrypt(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
            	hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    
    private static HashMap<String,String> cookieContiner = new HashMap<String,String>() ;
    
	private String httpGetMethod(String url, String referer){
		HttpGet getMethod = new HttpGet(url);
		getMethod.setHeader("Referer", referer);
		HttpParams paramNotRedirect = new BasicHttpParams();
		paramNotRedirect.setParameter("http.protocol.handle-redirects", false);
		getMethod.setParams(paramNotRedirect);
		
		//add cookie in header
		if (cookieContiner != null && !cookieContiner.isEmpty()){
	        StringBuilder sb = new StringBuilder();
	        Iterator iter = cookieContiner.entrySet().iterator();
	        while (iter.hasNext()) {
	          Map.Entry entry = (Map.Entry) iter.next();
	          String key = entry.getKey().toString();
	          String val = entry.getValue().toString();
	          sb.append(key);
	          sb.append("=");
	          sb.append(val);
	          sb.append(";");
	        }
	        getMethod.addHeader("cookie", sb.toString());
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
		    HttpResponse response = httpClient.execute(getMethod);
		    //save cookie from server
		    Header[] headers = response.getHeaders("Set-Cookie");
	    	String headerstr=headers.toString();
	        if (headers != null){
		        for(int i=0;i<headers.length;i++)
		        {
		        	String cookie=headers[i].getValue();
		        	String[]cookievalues=cookie.split(";");
		        	for(int j=0;j<cookievalues.length;j++)
		        	{
		        		String[] keyPair=cookievalues[j].split("=");
		        		String key=keyPair[0].trim();
		        		String value=keyPair.length>1?keyPair[1].trim():"";
		        		cookieContiner.put(key, value);
		        		Log.i("Share", "[key]" + key + " [value]" + value);
		        	}
		        }
	        }
	        if (url.contains("http://yunpan.cn/")){
	        	return response.getLastHeader("Location").getValue();
	        }
		    return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return "Error";
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return "Error";
		}
	}
	
	public void httpLogin() throws NoSuchAlgorithmException{

		//http://yunpan.cn/QiJFnkSzEPxnF （提取码：6eae）
		username = DBUtil.getKey("username");
		password = DBUtil.getKey("password");
		long currentTime = System.currentTimeMillis();
		String currentTimeStr = String.valueOf(currentTime);
		String nextTimeStr = String.valueOf(currentTime + 1);
		
		//get login token
		List<BasicNameValuePair> paramsGetToken = new LinkedList<BasicNameValuePair>();
		paramsGetToken.add(new BasicNameValuePair("o", "sso"));
		paramsGetToken.add(new BasicNameValuePair("m", "getToken"));
		paramsGetToken.add(new BasicNameValuePair("requestScema", "http"));
		paramsGetToken.add(new BasicNameValuePair("func", "QHPass.loginUtils.tokenCallback"));
		paramsGetToken.add(new BasicNameValuePair("userName", username));
		paramsGetToken.add(new BasicNameValuePair("rand", "0.6017216255422682"));
		paramsGetToken.add(new BasicNameValuePair("callback", "QiUserJsonP" + currentTimeStr));
		String paramGetToken = URLEncodedUtils.format(paramsGetToken, "UTF-8");
		String tokenPage = httpGetMethod(host + "?" + paramGetToken, referer);
		String tokenLogin = subStr(tokenPage, "token\":\"", "\"");
		Log.i("Share", tokenLogin);
		
		//login
		List<BasicNameValuePair> paramsLogin = new LinkedList<BasicNameValuePair>();
		paramsLogin.add(new BasicNameValuePair("o", "sso"));
		paramsLogin.add(new BasicNameValuePair("m", "login"));
		paramsLogin.add(new BasicNameValuePair("requestScema", "http"));
		paramsLogin.add(new BasicNameValuePair("from", "pcw_cloud"));
		paramsLogin.add(new BasicNameValuePair("rtype", "data"));
		paramsLogin.add(new BasicNameValuePair("func", "QHPass.loginUtils.loginCallback"));
		paramsLogin.add(new BasicNameValuePair("userName", username));
		paramsLogin.add(new BasicNameValuePair("pwdmethod", "1"));
		paramsLogin.add(new BasicNameValuePair("isKeepAlive", "0"));
		paramsLogin.add(new BasicNameValuePair("token", tokenLogin));
		paramsLogin.add(new BasicNameValuePair("captFlag", "1"));
		paramsLogin.add(new BasicNameValuePair("captId", "i360"));
		paramsLogin.add(new BasicNameValuePair("captCode", ""));
		paramsLogin.add(new BasicNameValuePair("lm", "0"));
		paramsLogin.add(new BasicNameValuePair("validatelm", "0"));
		paramsLogin.add(new BasicNameValuePair("password", MD5Encrypt(password)));
		paramsLogin.add(new BasicNameValuePair("r", System.currentTimeMillis() + ""));
		paramsLogin.add(new BasicNameValuePair("callback", "QiUserJsonP" + nextTimeStr));
		String paramLogin = URLEncodedUtils.format(paramsLogin, "UTF-8");
		String loginPage = httpGetMethod(host + "?" + paramLogin, referer);
		Log.i("Share", loginPage);
	}
	
	private String getFileDescribe(String shortUrl, String linkPassword){
		//get actual url
		shareLink = httpGetMethod("http://yunpan.cn/" + shortUrl, referer);
		Log.i("Share", shareLink);
		if (!linkPassword.equals("")){
			//verify share password
			List<BasicNameValuePair> paramsVerifyPassword = new LinkedList<BasicNameValuePair>();
			paramsVerifyPassword.add(new BasicNameValuePair("shorturl", shortUrl));
			paramsVerifyPassword.add(new BasicNameValuePair("linkpassword", linkPassword));
			String paramVerifyPassword = URLEncodedUtils.format(paramsVerifyPassword, "UTF-8");
			String verifyHost = "http" + subStr(shareLink, "http", "lk/") + "share/verifyPassword";
			String verifyPasswordPage = httpGetMethod(verifyHost + "?" + paramVerifyPassword, referer);
			Log.i("Share", verifyPasswordPage);
		}
		//get share file info
		String shareFilePage = httpGetMethod(shareLink, shareLink);
		previewSign = subStr(shareFilePage, "previewSign: '", "'");
		//analyse nids
		
		String nid = subStr(shareFilePage, "nid : '", "'");
		if (!nid.equals("")){//share only on file
			nids.add(nid);
		}else{// share mutilpy files
			int sIndex = 0;
			String prefix = "nid\": \"";
			String postfix = "\"";
			while (true){
				sIndex = shareFilePage.indexOf(prefix, sIndex) + prefix.length();
				int eIndex = 0;
				int len = 0;
				if (sIndex > prefix.length() - 1)
				{
					eIndex = shareFilePage.indexOf(postfix, sIndex);
					len = eIndex - sIndex;
					sIndex = eIndex;
				}         
				nid = (len > 0) ? shareFilePage.substring(eIndex - len, eIndex) : "";
				if (!nid.equals("")){
					nids.add(nid);
				}else{
					break;
				}
			}
		}
		String shareDescribe = subStr(shareFilePage, "name : '", "',");
		Log.i("Share", "shareDescribe: " + shareDescribe);
		
		return shareDescribe;
	}
	
	private void dumpFiles(String shortUrl, String linkPassword){
		String savePath = "/";
		//get dump token
		long  currentTime = System.currentTimeMillis();
		String currentTimeStr = String.valueOf(currentTime);
		String nextTimeStr;
		//nextTimeStr = String.valueOf(currentTime + 1);
		List<BasicNameValuePair> paramsGetDumpToken = new LinkedList<BasicNameValuePair>();
		paramsGetDumpToken.add(new BasicNameValuePair("cross_domain_callback", "QWJsonp" + currentTimeStr));
		paramsGetDumpToken.add(new BasicNameValuePair("t", "0.6011234255422682"));
		String paramGetDumpToken = URLEncodedUtils.format(paramsGetDumpToken, "UTF-8");/////
		String getDumpTokenHost = "http://c24.yunpan.360.cn/user/yplogin";
		String dumpTokenPage = httpGetMethod(getDumpTokenHost + "?" + paramGetDumpToken, shareLink);
		
		String tokenDump = subStr(dumpTokenPage, "token\":\"", "\",\"qid");
		Log.i("Share", tokenDump);
		
		//dump files
		for (int i = 0; i != nids.size(); ++i){
			nextTimeStr = String.valueOf(currentTime + i + 1);
			List<BasicNameValuePair> paramsDump = new LinkedList<BasicNameValuePair>();
			paramsDump.add(new BasicNameValuePair("token", tokenDump));
			paramsDump.add(new BasicNameValuePair("surl", shortUrl));
			paramsDump.add(new BasicNameValuePair("path", savePath));
			paramsDump.add(new BasicNameValuePair("ajax", "1"));
			paramsDump.add(new BasicNameValuePair("preview_sign", previewSign));
			paramsDump.add(new BasicNameValuePair("t", System.currentTimeMillis() + ""));
			paramsDump.add(new BasicNameValuePair("cross_domain_callback", "QWJsonp" + nextTimeStr));
			paramsDump.add(new BasicNameValuePair("t", "0.6011234256122682"));
			String paramDump = URLEncodedUtils.format(paramsDump, "UTF-8");
			String paramDumpFile = paramDump + "&nids%5B%5D=" + nids.get(i);
			String paramDumpFolder = paramDump + "&nid=" + nids.get(i);
			String dumpFilesHost = "http://c24.yunpan.360.cn/share/savefiles";
			String dumpFolderHost = "http://c24.yunpan.360.cn/share/savedir";
			String dumpFilesPage = httpGetMethod(dumpFilesHost + "?" + paramDumpFile, shareLink);
			String dumpFolderPage = "dump has done。。。";
			if (!dumpFilesPage.contains("errno\":0,\"")){
				dumpFolderPage = httpGetMethod(dumpFolderHost + "?" + paramDumpFolder, shareLink);
			}
			Log.i("Share", dumpFilesPage);
			Log.i("Share", dumpFolderPage);
		}
	}
	
	private class OnDumpClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//转存到我的网盘
			//好友AAA用360云盘给你分享文件 http://yunpan.cn/cccccccccc，提取码：ZZZZ【360】
			//Toast.makeText(FileReceivedConfrim.this, surl + "\n" + pwd, Toast.LENGTH_LONG).show();
			Toast.makeText(FileReceivedConfrim.this, "正在努力的转存中...", Toast.LENGTH_LONG).show();
			Log.i("Share", "surl=" + shorturl);
			Log.i("Share", "pwd=" + linkPassword);
			dumpFiles(shorturl, linkPassword);
			FileReceivedConfrim.this.finish();
			Toast.makeText(FileReceivedConfrim.this, "转存成功", Toast.LENGTH_LONG).show();
		}
	}
	
	private class OnIgnoreClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			FileReceivedConfrim.this.finish();
		}	
	}
}
