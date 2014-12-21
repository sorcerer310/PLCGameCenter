package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.DataHelper;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 查看用户的当前排行榜数据,查出用户数据后,将用户的当前数据库中的名次与成绩缓存到session中
 * 使用的sql语句,在此备注:
 *				select CAST(rank AS SIGNED) as  rank,pkvalue,userid,u.nickname,headicon,equipicon1,equipicon2 from
				(select (@rank:=@rank+1) as rank,dd.pkvalue,dd.userid from 
					(
						select pkvalue,userid from 
						(select pkvalue,userid from pk
						where gameid=1 and zoneid = 1
						order by pkvalue asc) ad,
						(select (@userpkvalue:=pkvalue) from pk where gameid=1 and zoneid=1 and userid=1)c
						where pkvalue>@userpkvalue 
						limit 10 -- 此处limit表示高于自己分数的显示多少
						union all
						select pkvalue,userid from 
							(select pkvalue,userid from pk
							where gameid=1 and zoneid = 1
							order by pkvalue desc) ad,
							(select (@userpkvalue:=pkvalue) from pk where gameid=1 and zoneid=1 and userid=1)c
							where pkvalue<=@userpkvalue 
					) dd
					,(select (@rank:=0)) r
					order by pkvalue desc
					limit 20 -- 此处limit表示一共显示多少行分数
				) od
				inner join `user` u on u.id = od.userid
				order by rank asc
 */
@WebServlet(description = "普通pk数据查看", urlPatterns = { "/pkquery" })
public class Pk_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pk_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//判断用户是否填写了昵称
		if(!DataHelper.getInstance().hasNickName(request)){
			U.p(response, JSONMsg.info(1015));
			return;
		}
		
		String gameid = U.getSS(request, "gameid");
		String zoneid = U.getSS(request, "zoneid");
		String userid = U.getSS(request, "userid");
		
		String limitstr = U.getRS(request, "limit")==null?"20":U.getRS(request, "limit");			//如果有limit参数，使用limit参数，否则默认20条
		if(limitstr.equals("-1"))
			limitstr = "20";
		int limit = Integer.parseInt(limitstr);

		try{
			//获得用户的排行数据
			CachedRowSetImpl rs = DataHelper.getInstance().getUserRankData(gameid, zoneid, userid, limit);
			if(rs.size()==0)
				rs = getTop20Rank(response,gameid,zoneid,limitstr);
			else{
				//如果有数据，取出当前用户对应的数据行,将名次与成绩缓存到session中
				DataHelper.getInstance().cacheRandValue(rs,userid,request);
			}
			
			U.p(response, JSONParser.parseJson(rs));
		}catch(Exception e){
			U.el(Pk_Query.class.getName(),e,response,1007);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * 当没有数据返回时,返回排行榜前20名数据,使用sql语句如下:
	 * select (@rank:=@rank+1) as rank,pkvalue,userid,nickname,headicon,equipicon1,equipicon2 from
		(select pkvalue,rank,userid,nickname,headicon,equipicon1,equipicon2 from pk p
		inner join user u on p.userid = u.id
		where p.gameid=2 and p.zoneid=1 ) dd,(select (@rank:=0))r
		order by pkvalue desc
		limit 20
	 * @return
	 */
	private CachedRowSetImpl getTop20Rank(HttpServletResponse response,String gameid,String zoneid,String limit){
		CachedRowSetImpl rs = null;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select (@rank:=@rank+1) as rank,pkvalue,userid,nickname,headicon,equipicon1,equipicon2 from ")
				.append("(select pkvalue,rank,userid,nickname,headicon,equipicon1,equipicon2 from pk p ")
				.append("inner join user u on p.userid = u.id ")
				.append("where p.gameid=? and p.zoneid=? ) dd,(select (@rank:=0))r ")
				.append("order by pkvalue desc ")
				.append("limit ?");
			ArrayList<ColumnData> ldata = U.lcd(new String[]{"gameid","zoneid","limit"}
					,new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER} , new String[]{gameid,zoneid,limit});
			rs = ODB.query(sb.toString(),ldata);
		}catch(Exception e){
			U.el(Pk_Query.class.getName(),e,response,1007);
		}
		return rs;
	}
	
}
