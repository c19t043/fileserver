package com.fileupload;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import sun.misc.BASE64Decoder;

@SuppressWarnings("serial")
public class FileUpload extends HttpServlet {
	/**
	 * 请求文件base64key:图片
	 */
	private final static String REQUEST_IMAGE_BASE64_KEY = "imgBase64";
	/**
	 * 上传文件保存路径
	 */
	private final static String REQUEST_FILE_STORE_PATH_KEY = "imgPath";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			// request
			String imgBase64 = request.getParameter(REQUEST_IMAGE_BASE64_KEY);
			String imgPath = request.getParameter(REQUEST_FILE_STORE_PATH_KEY);
			// handle
			if(null!=imgBase64&&!"".equals(imgBase64)){
				String filePath = request.getServletContext().getRealPath("/");
				filePath+=imgPath;
				//System.out.println(filePath);
				imgBase64 = imgBase64.substring(30);
				imgBase64 = URLDecoder.decode(imgBase64, "UTF-8");
				byte[] data = decode(imgBase64);
				File file = new File(filePath);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				FileUtils.writeByteArrayToFile(file, data);
			}
			ResponseUtils.renderText(response, "成功");
		}catch(Exception e){
			System.out.println("上传文件错误");
		}
	}
	
	/**
	 * 解析base64编码
	 * @param imageData
	 * @return
	 * @throws IOException
	 */
	private static byte[] decode(String imageData) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] data = decoder.decodeBuffer(imageData);
		for (int i = 0; i < data.length; ++i) {
			if (data[i] < 0) {
				data[i] += 256;// 调整异常数据
			}
		}
		return data;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
