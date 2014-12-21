package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Get_Zone_List
 * 获得游戏分区列表
 */
@WebServlet(name = "zonelistquery", urlPatterns = { "/zonelistquery" })
public class Zone_List_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Zone_List_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from zone where gameid = ?");
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString(), U.lcd(request, "zone", U.cc(request)));
			U.p(response, JSONParser.parseJson(rs));
		}catch(Exception e){
			U.el(Zone_List_Query.class.getName(), e, response, 1004);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
