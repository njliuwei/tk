package com.fhsoft.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName:com.fhsoft.util.DownloadUtil
 * @Description:文件下载
 * 
 * @Author:liyi
 * @Date:2015年4月14日下午2:42:48
 * 
 */
public class DownloadUtil {

	public static void downLoad(String filePath, HttpServletResponse response) throws Exception {
		File file = new File(filePath);
		String filename = file.getName();
		InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		response.reset();
		// 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "iso8859-1"));
		response.addHeader("Content-Length", "" + file.length());
		OutputStream os = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		os.write(buffer);// 输出文件
		os.flush();
		os.close();
	}
}
