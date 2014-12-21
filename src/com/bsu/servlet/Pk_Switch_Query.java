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
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 浏览pk数据,第一次浏览时如无当前userid的数据,则添入当前userid数据
 */
@WebServlet(name = "pkquery", urlPatterns = { "/pkswitchquery" })
public class Pk_Switch_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pk_Switch_Query() {
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
		
		//获得当前用户的名次数据
		StringBuffer sb = new StringBuffer();
//		sb.append("select id,userid,rank,pkvalue from pk p1 where p1.gameid=? and p1.zoneid = ? ")
//			.append(" and (p1.rank >=(select rank from pk where gameid=? and zoneid=? and userid=?)-10 ")
//			.append(" and p1.rank <= (select rank from pk where gameid=? and zoneid=? and userid=?)+10) ");
		
		
		sb.append("select pk.id,pk.userid,pk.rank,pk.pkvalue,headicon,equipicon1,equipicon2 FROM ")
			.append("(select p1.id,p1.userid,p1.rank,p1.pkvalue from pk p1 ") 
			.append("where p1.gameid=? and p1.zoneid = ? ")
			.append("and p1.rank >=(select rank from pk where gameid=? and zoneid=? and userid=?)-10 ")
			.append("and p1.rank <= (select rank from pk where gameid=? and zoneid=? and userid=?)+10 ")
			.append(") pk ")
			.append("INNER JOIN `user` ud on pk.userid = ud.id ");
		
		
		CachedRowSetImpl rs = null;																		//获得竞技场数据
		try {
			//获得当前id前后各10名的排名数据
			rs = ODB.query(sb.toString(), U.lcd(new String[]{"gameid","zoneid","gameid","zoneid","userid","gameid","zoneid","userid"}
					, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,}
					, new String[]{gameid,zoneid,gameid,zoneid,userid,gameid,zoneid,userid}));
			//如果未获得排名数据,插入当前用户的数据
			if(rs.size()<=0){
			
				int rank = getMaxRankByUser(response,gameid,zoneid);									//当前最大名词,如果没有该游戏该区的内容,则设名次为0
				StringBuffer sb_insert = new StringBuffer();
				sb_insert.append("insert into pk (gameid,zoneid,userid,rank) values (?,?,?,?)");
				ODB.insert(sb_insert.toString(), U.lcd(new String[]{"gameid","zoneid","userid","rank"}
								, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER}
								, new String[]{gameid,zoneid,userid,String.valueOf(++rank)}));
			}
			//再次获得当前userid前后各10名的排名数据
			rs = ODB.query(sb.toString(), U.lcd(new String[]{"gameid","zoneid","gameid","zoneid","userid","gameid","zoneid","userid"}
				, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,}
				, new String[]{gameid,zoneid,gameid,zoneid,userid,gameid,zoneid,userid}));
			
			U.p(response, JSONParser.parseJson(rs));
			
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			U.el(Pk_Switch_Query.class.getName(), e, response, 1007);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * 获得排行榜中指定游戏指定区域的最大名次
	 * @param response		用于打印的response
	 * @param gameid		游戏id
	 * @param zoneid		区域id
	 * @return
	 * @throws Exception
	 */
	private int getMaxRankByUser(HttpServletResponse response, String gameid,String zoneid) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select max(rank) as maxrank from pk where gameid=? and zoneid=? ");
		CachedRowSetImpl rs = null;
		rs = ODB.query(sb.toString(), U.lcd(new String[]{"gameid","zoneid"}
							,new int[]{Types.INTEGER,Types.INTEGER}
							, new String[]{gameid,zoneid}));
		
		if(rs.size()<=0 || !rs.first())
			return 0;
		
		return rs.getInt("maxrank");
	}
	
}
