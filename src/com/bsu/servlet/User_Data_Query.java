package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
 * Servlet implementation class User_Data_Query
 */
@WebServlet(name = "userdataquery", urlPatterns = { "/userdataquery" })
public class User_Data_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Data_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = U.getSS(request, "userid");
		
		BlobOperate bo = new BlobOperate();
		try{
			byte[] bytes = bo.queryBlob("user", "binarydata", Long.parseLong(userid));
			ServletOutputStream sos= response.getOutputStream();
			sos.write(bytes);
			sos.flush();
		}catch(Exception e){
			U.el(User_Data_Query.class.getName(), e, response,1013 );
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
