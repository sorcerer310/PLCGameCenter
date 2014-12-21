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
 * Servlet implementation class User_Modify_Nickname
 * 修改用户昵称
 */
@WebServlet(name = "modifynickname", urlPatterns = { "/modifynickname" })
public class User_Modify_Nickname extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Modify_Nickname() {
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
		String nickname = U.getRS(request, "nickname");
		
		StringBuffer sb = new StringBuffer();
		sb.append("update `user` set nickname = ? where gameid = ? and zoneid=? and id=?");
		
		try {
			int c = ODB.update(sb.toString(), U.lcd(new String[]{"nickname","gameid","zoneid","id"}
								,new int[]{Types.VARCHAR,Types.INTEGER,Types.INTEGER,Types.INTEGER}
								, new String[]{nickname,gameid,zoneid,userid}));
			if(c>0){
				request.getSession(false).setAttribute("nickname", nickname);			//向session中增加用户昵称
				U.p(response, JSONMsg.info(3007));
			}
			else
				U.p(response, JSONMsg.info(3007, "未找到id为"+userid+"的用户"));
		} catch ( Exception e) {
			U.el(User_Modify_Nickname.class.getName(),e,response,1010);
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
