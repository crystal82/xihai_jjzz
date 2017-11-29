package chipsea.wifiplug.lib.model;

public class CmdWordDefine {
	/*直接控制*/
	public static int CONTROL=70; 
	/*定时设置*/
	public static int TIMER_SET=71;
	public static int TIMER_REMOVE=72;
	/*定时查询*/
	public static int TIMER_QUERY=73;
	
	/*运行状态查询*/
	public static int RUNNING_QUERY=78;
	
	public static int OPENCLOSE_TIMER=80;

	/*红外控制相关指令*/
	public static int IR_CONTROL=84;
	/*控台控制*/
	public static int AIR_CONTROL=86;
}
