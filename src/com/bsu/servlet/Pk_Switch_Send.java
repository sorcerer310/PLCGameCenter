package com.bsu.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.DBTransactionEvent;
import com.bsu.system.db.ODB;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 发送pk成绩,并与某人进行比较,如果成绩高于比较人,则交换两人排名位置
 */
@WebServlet(name = "pksend", urlPatterns = { "/pkswitchsend" })
public class Pk_Switch_Send extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pk_Switch_Send() {
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
		String pkvalue = U.getRS(request, "pkvalue");
		String objuserid = U.getRS(request, "objuserid");
		
		StringBuffer sb_obj = new StringBuffer();
		sb_obj.append("select pkvalue,rank from pk where gameid=? and zoneid=? and userid = ?");
		CachedRowSetImpl rs_obj = null;
		CachedRowSetImpl rs_user = null;
		try {
			//对手的名次数据
			rs_obj = ODB.query(sb_obj.toString(), U.lcd(new String[]{"gameid","zoneid","userid"}
					, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER}
					, new String[]{gameid,zoneid,objuserid}));
			rs_user = ODB.query(sb_obj.toString(),U.lcd(new String[]{"gameid","zoneid","userid"}
					, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER}
					, new String[]{gameid,zoneid,userid}));
			
//			int objpkvalue = 0;																		
//			int objrank = 0;																				
//			int userpkvalue = 0;																		
//			int userrank = 0;																			
			if(rs_obj.first() && rs_user.first()){
				final int objpkvalue = rs_obj.getInt("pkvalue");								//对手的pk值
				final int objrank = rs_obj.getInt("rank");										//对手的名次
				final int userpkvalue = Integer.parseInt(pkvalue);								//自己的pk值
				final int userrank = rs_user.getInt("rank");									//自己的名次
				final String fgameid = gameid;
				final String fzoneid = zoneid;
				final String fuserid = userid;
				final String fobjuserid = objuserid;
				final HttpServletResponse res = response;
				 if(userpkvalue>objpkvalue && userrank > objrank){
					 ODB.moreTransactionOperate( new DBTransactionEvent(){
						@Override
						public void operate(Connection conn) throws SQLException, Exception {
							StringBuffer sb_updaterank = new StringBuffer();
							sb_updaterank.append("update pk set rank = ?,pkvalue=? where gameid = ? and zoneid = ? and userid = ?");
							//玩家更新名次
							ODB.update(sb_updaterank.toString(), U.lcd(new String[]{"rank","pkvalue","gameid","zoneid","userid"}
									, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER}
									, new String[]{String.valueOf(objrank),String.valueOf(userpkvalue),fgameid,fzoneid,fuserid}), conn);
							
							//对手更新名次
							ODB.update(sb_updaterank.toString(),  U.lcd(new String[]{"rank","pkvalue","gameid","zoneid","userid"}
									, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER}
									, new String[]{String.valueOf(userrank),String.valueOf(objpkvalue),fgameid,fzoneid,fobjuserid}), conn);
							U.p(res, JSONMsg.info(3006));
							return;
						}
					 });
				 }
			}
//			U.p(response, JSONMsg.info(1009));
		} catch (Exception e) {
			U.el(Pk_Switch_Send.class.getName(), e, response, (1008));
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
