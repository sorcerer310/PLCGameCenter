package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
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
 * Servlet implementation class Chat_Query
 * 聊天内容浏览
 */
@WebServlet(name = "chatquery", urlPatterns = { "/chatquery" })
public class Chat_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chat_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gameid = U.getSS(request, "gameid");
		String zoneid = U.getSS(request, "zoneid");
		String userid = U.getSS(request, "userid");
		StringBuffer sb = new StringBuffer();

		sb.append("select * from chat where gameid=? and zoneid=? and userid=? ")
			.append(" order by time desc limit 20");
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString(), U.lcd(new String[]{"gameid","zoneid","userid"}
				, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER}
				, new String[]{gameid,zoneid,userid}));
			U.p(response, JSONParser.parseJson(rs));
		}catch(Exception e){
			U.el(Chat_Query.class.getName(), e,response,1005);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
