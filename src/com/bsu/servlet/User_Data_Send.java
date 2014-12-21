package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.BlobOperate;
import com.bsu.system.db.ODB;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 写入用户2进制存档数据
 */
@WebServlet(name = "userdatasend", urlPatterns = { "/userdatasend" })
public class User_Data_Send extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Data_Send() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = U.getSS(request, "userid");
		
		ServletInputStream is = request.getInputStream();					//此处获得的是post直接发送的数据
		int b = 0;															//临时变量保存当前字节
		ArrayList<Integer> lbytes = new ArrayList<Integer>();				//数组容器保存2进制数据
		while((b=is.read())!=-1){											//读取1个字节
			lbytes.add(b);													//将该字节数据放入到容器中
		}
		
		//将容器中的数据转换为byte数组
		int blength = lbytes.size();
		byte[] bytes = new byte[blength];
		for(int i=0;i<blength;i++){
			bytes[i] =lbytes.get(i).byteValue();
		}

		//数据库大数据写入
		BlobOperate bo = new BlobOperate();
		try {
			long upid = bo.updateData("user", "binarydata", bytes, String.valueOf(userid));
			System.out.println("upid"+upid);
			if(upid==-1)
				U.p(response, JSONMsg.info(1011, "写入2进制数据失败,未返回数据行的id"));
			else
				U.p(response, JSONMsg.info(3008));

		} catch (Exception e) {
			U.el(User_Data_Send.class.getName(), e, response, 1011);
		}
	}
}
