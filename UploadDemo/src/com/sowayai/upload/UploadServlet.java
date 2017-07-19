package com.sowayai.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.IOUtils;


//上传要求很多，第一：form的提交方式必须是Post，第二Enctype必须是multipart/form-data
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取到客户端请求的流
		req.setCharacterEncoding("utf-8");
		//InputStream is = req.getInputStream();
		/*InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String content = br.readLine();
		content = new String(content.getBytes("ISO-8859-1"),"utf-8");*/
//		System.out.println(content);
		System.out.println("------------------");
		//1.首先先判断客户端是否提交过来了二进制的数据
		if(ServletFileUpload.isMultipartContent(req)){
			System.out.println("***************");
			//处理上传的数据
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			ServletContext servletContext = this.getServletConfig().getServletContext();
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				List<FileItem> items = upload.parseRequest(req);
				
				//2、处理上传项
				
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
				    FileItem item = iter.next();

				    if (item.isFormField()) {
				    	System.out.println("form-field:");
				        System.out.println(item.getFieldName() + ":" + item.getString("utf-8"));
				    } else {
				    	System.out.println("data-field:");
				    
				    	//System.out.println(item.getFieldName() + ":" + item.getString());
				    	//将上传的二进制数据进行存储
				    	//File destFile = new File("uploads/aaa.jpg");
				    	
				    	String fileName = Long.toString(new java.util.Date().getTime()) + item.getName();
				    	String destFilePath = req.getServletContext().getRealPath("uploads/" + fileName);
				    	File destFile = new File(destFilePath);
				    	FileOutputStream fos = new FileOutputStream(destFile);
				    	IOUtils.copy(item.getInputStream(), fos);
				    	fos.close();
				    	
				    }
				}
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
}
