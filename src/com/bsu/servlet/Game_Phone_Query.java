package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Game_Phone_Query
 */
@WebServlet("/gamephonequery")
public class Game_Phone_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Game_Phone_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gameid = U.getSS(request, "gameid");
		String sb_sql = "select phone from game where id = ?";
		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb_sql, U.lcd(new String[]{"gameid"}, new int[]{Types.INTEGER}, new String[]{gameid}));
			U.p(response, JSONParser.parseJson(rs));
		} catch (Exception e) {
			U.el(Pk_Query.class.getName(),e,response,1014);
		}
	}
}
