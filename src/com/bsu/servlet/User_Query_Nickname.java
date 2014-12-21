package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.bsu.system.db.BlobOperate;
import com.bsu.system.db.ODB;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class User_Query_Nickname
 */
@WebServlet(description = "查看用户呢称", urlPatterns = { "/querynickname" })
public class User_Query_Nickname extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Query_Nickname() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nickname = U.getSS(request, "nickname");
		try {
			U.p(response, JSONParser.parseSimpleJson("nickname", nickname));
		} catch (JSONException e) {
			U.el(Zone_List_Query.class.getName(), e, response, 1111);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
