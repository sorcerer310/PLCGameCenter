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
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bsu.system.db.DBTransactionEvent;
import com.bsu.system.db.ODB;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.DataHelper;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 上传pk数据
 * 例子:INSERT INTO pk (gameid,zoneid,userid,pkvalue) VALUES(1,2,1,104) ON DUPLICATE KEY UPDATE pkvalue=104
 */
@WebServlet(description = "普通方式成绩上传", urlPatterns = { "/pksend" })
public class Pk_Send extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pk_Send() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!DataHelper.getInstance().hasNickName(request)){
			U.p(response, JSONMsg.info(1015));
			return;
		}
		
		String gameid = U.getSS(request, "gameid");
		String zoneid = U.getSS(request, "zoneid");
		String userid = U.getSS(request, "userid");
		String pksvalue = U.getRS(request, "pkvalue");

		//旧排名数据
		String oldsrank = U.getSS(request, "rank");
		int oldrank = U.string2Int(oldsrank, 0);
		
		//旧游戏成绩数据
		String oldspkvalue = U.getSS(request, "pkvalue")==null?"0":U.getSS(request, "pkvalue");		//获得用户旧数据
		int oldpkvalue = Integer.parseInt(oldspkvalue);

		//先转换要更新的成绩.
		int pkvalue = U.string2Int(pksvalue, 0);

		try{
			//1:先比较成绩,如果成绩较新,就更新成绩进去.
			//2:后比较名次,如果名次提升,则返回成绩数据
			
			
			//比较新成绩与旧成绩，如果新成绩比旧成绩分数高，更新该成绩到数据库中
			if(pkvalue>oldpkvalue ){
				insertPkvalue(gameid,zoneid,userid,pksvalue,response);										//插入或更新新成绩数据
				request.getSession(false).setAttribute("pkvalue", pkvalue);									//将新成绩更新到session中
			}

			CachedRowSetImpl rs = DataHelper.getInstance().getUserRankData(gameid, zoneid, userid, 3);		//查询自己与前后各一名的成绩
			
			JSONArray ja = new JSONArray();
			int keepCount = 0;								//保留的行数
			//获得3行数据.
			rs.beforeFirst();
			//把当前用户前面的数据都干掉,后面只保留一条数据
			while(rs.next()){
				int ruid = rs.getInt("userid");
				//如果当前为用户数据，获得用户数据，并开始计数
				if(String.valueOf(ruid).equals(userid)){
					
					JSONObject jo =  new JSONObject();
					jo.put("rank", rs.getInt("rank"));
					jo.put("pkvalue", rs.getInt("pkvalue"));
					jo.put("userid", rs.getInt("userid"));
					jo.put("nickname", rs.getString("nickname"));
					ja.put(jo);
					keepCount++;
					continue;
				}
				//如果开始了计数
				if(keepCount>0){
					JSONObject jo =  new JSONObject();
					jo.put("rank", rs.getInt("rank"));
					jo.put("pkvalue", rs.getInt("pkvalue"));
					jo.put("userid", rs.getInt("userid"));
					jo.put("nickname", rs.getString("nickname"));
					ja.put(jo);
					
					break;
				}
			}
			
			//过滤完数据后,判断名次是否上升,如果名次上升打印2条数据,否则打印1016错误信息
			JSONObject jo = (JSONObject) ja.get(0);
			int rank = jo.getInt("rank");
			
			//如果名次上升
			if(rank<oldrank || oldrank == 0){
				//将上升的名次数据加入到当前用户的数据中
				jo.put("raise", String.valueOf(oldrank-rank));

				DataHelper.getInstance().cacheRandValue(rs, userid, request);			//将当前的成绩缓存到session中
				U.p(response, ja.toString());											//名次上升数据
				return;
			}else{
				U.p(response, JSONMsg.info(1016));
				return;
			}
		}catch(Exception e){
			U.el(Pk_Switch_Send.class.getName(), e, response, (1008));
		}
	}

	/**
	 * 插入数据
	 * @param gameid		游戏id
	 * @param zoneid		大区id
	 * @param userid		用户id
	 * @param pkvalue		上传的成绩
	 * @param response		Response对象
	 * @throws Exception 
	 */
	private void insertPkvalue(String gameid,String zoneid,String userid,String pkvalue,HttpServletResponse response) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO pk (gameid,zoneid,userid,pkvalue) VALUES(?,?,?,?) ")
			.append("ON DUPLICATE KEY UPDATE pkvalue=?");
		
		ODB.update(sb.toString()
				, U.lcd(new String[]{"gameid","zoneid","userid","pkvalue","pkvalue"}
					,new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,}
					,new String[]{gameid,zoneid,userid,pkvalue,pkvalue}));
//		U.p(response, JSONMsg.info(3006));
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
