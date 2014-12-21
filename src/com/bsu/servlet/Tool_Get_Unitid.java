package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class Tool_Get_Unitid
 */
@WebServlet("/tool_get_unitid")
public class Tool_Get_Unitid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tool_Get_Unitid() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		U.p(response, JSONMsg.info(3021,request.getSession(false).getAttribute("unitid").toString()));
	}

}
