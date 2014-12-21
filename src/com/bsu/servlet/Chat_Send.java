package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class Chat_Send
 * 聊天内容发送
 */
@WebServlet(name = "chatsend", urlPatterns = { "/chatsend" })
public class Chat_Send extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chat_Send() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gameid = U.getSS(request, "gameid");
		String zoneid = U.getSS(request, "zoneid");
		String userid = U.getSS(request, "userid");
		String content = U.getRS(request, "content");
		StringBuffer sb = new StringBuffer();
		sb.append("insert into chat (gameid,zoneid,userid,content) values (?,?,?,?)");
		
		try {
			int c = ODB.insert(sb.toString(), U.lcd(new String[]{"gameid","zoneid","userid","content"}
				, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.VARCHAR}
				, new String[]{gameid,zoneid,userid,content}));
			if(c>0)
				U.p(response, JSONMsg.info(3005));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			U.el(Chat_Send.class.getName(), e, response, 1006);
		}
	}

}
