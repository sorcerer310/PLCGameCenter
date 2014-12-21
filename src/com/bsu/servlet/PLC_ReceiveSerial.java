package com.bsu.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.commport.CommPortInstance;
import com.bsu.commport.SerialReaderListener;
import com.bsu.system.tool.PLCGameStatus;

/**
 * Servlet implementation class PLC_ReceiveSerial
 */
@WebServlet(description = "接收串口数据到程序中", urlPatterns = { "/PLC_ReceiveSerial" },loadOnStartup = 0)
public class PLC_ReceiveSerial extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CommPortInstance cpi = null;
	
	private final byte PLC_RECEIVE_BED_VIDEO = 1;									//躺床传第一个视频到手机
	private final byte PLC_RECEIVE_DRAWER_VIDEO = 2;								//床抽屉触发手机视频
	private final byte PLC_RECEIVE_KNOCK_DOOR_VIDEO = 3;							//敲门触发手机视频								
	private final byte PLC_RECEIVE_WATERING_VIDEO = 4;							//浇花触发手机视频
	private final byte PLC_RECEIVE_PLAY_VIDEO = 5;								//从plc处接到播放视频的指令
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PLC_ReceiveSerial() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		cpi = CommPortInstance.getInstance();
		cpi.initCommPort("COM2");
		cpi.getSerialReader().setSerialReaderListener(new SerialReaderListener(){

			@Override
			public void readCompleted(String command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void readCompleted(int command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void readCompleted(byte command) {
				switch(command){
				case PLC_RECEIVE_BED_VIDEO:									
					//躺床上视频
					PLCGameStatus.set_PLC_STATUS_BED(true);
					break;
				case PLC_RECEIVE_DRAWER_VIDEO:
					//抽屉视频
					PLCGameStatus.set_PLC_STATUS_DRAWER(true);
					break;
				case PLC_RECEIVE_KNOCK_DOOR_VIDEO:
					//敲门视频
					PLCGameStatus.set_PLC_STATUS_KNOCK_DOOR(true);
					break;
				case PLC_RECEIVE_WATERING_VIDEO:
					//浇花视频
					PLCGameStatus.set_PLC_STATUS_WATERING(true);
					break;
				case PLC_RECEIVE_PLAY_VIDEO:
					//要求播放视频命令
					PLCGameStatus.set_PLC_STATUS_PLAY_VIDEO(true);
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
