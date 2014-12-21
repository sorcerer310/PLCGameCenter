package com.bsu.system.tool;

public class PLCGameStatus {
	private static boolean PLC_STATUS_BED = false;				//躺床状态
	private static boolean PLC_STATUS_DRAWER = false;				//抽屉状态
	private static boolean PLC_STATUS_KNOCK_DOOR = false;			//敲门状态
	private static boolean PLC_STATUS_WATERING = false;			//浇花状态
	private static boolean PLC_STATUS_PLAY_VIDEO = false;			//播放视频状态
	
	public static synchronized void set_PLC_STATUS_BED(boolean b){
		PLC_STATUS_BED = b;
	}
	
	public static boolean get_PLC_STATUS_BED(){
		return PLC_STATUS_BED;
	}
	
	
	public static synchronized void set_PLC_STATUS_DRAWER(boolean b){
		PLC_STATUS_DRAWER = b;
	}
	
	public static boolean get_PLC_STATUS_DRAWER(){
		return PLC_STATUS_DRAWER;
	}
	
	
	public static synchronized void set_PLC_STATUS_KNOCK_DOOR(boolean b){
		PLC_STATUS_KNOCK_DOOR = b;
	}
	
	public static boolean get_PLC_STATUS_KNOCK_DOOR(){
		return PLC_STATUS_KNOCK_DOOR;
	}
	
	
	
	public static synchronized void set_PLC_STATUS_WATERING(boolean b){
		PLC_STATUS_WATERING = b;
	}
	
	public static boolean get_PLC_STATUS_WATERING(){
		return PLC_STATUS_WATERING;
	}
	
	
	public static synchronized void set_PLC_STATUS_PLAY_VIDEO(boolean b){
		PLC_STATUS_PLAY_VIDEO = b;
	}
	
	public static boolean get_PLC_STATUS_PLAY_VIDEO(){
		return PLC_STATUS_PLAY_VIDEO;
	}
}
