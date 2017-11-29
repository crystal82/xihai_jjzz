package chipsea.wifiplug.lib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * @Description 定时设置解析器
 * @author lixun
 * @CreateDate 2016/5/31
 */
public class TimerSettingParser {
	
	/**
	 * 根据设备上报的二进制流解析出对应的定时设置
	 * @param payload 二进制流
	 * @return 定时设置列表
	 */
	public static List<TimerSetting> parse(byte[] payload){
		List<TimerSetting> lstRet=new ArrayList<TimerSetting>();
		if(payload==null){
			return lstRet;
		}
		if(payload.length>8){
			for(int i=0;i<payload.length;i=i+9){
				TimerSetting timer=new TimerSetting();
				
				if((payload[i] & 0x80)==0x80){
					timer.isOpen=true;
				}		
				timer.indexId= (byte)(payload[i] & 0x7f);
				timer.loopSetting=payload[i+1];
				
				if((payload[i+1] & 1)==1){
					timer.isLoop=true;
									
					String sSet=BytesUtil.byteToBit(payload[i+1]);
					if(sSet.substring(6, 7).equals("1")){
						timer.isMon=true;
					}
					
					if(sSet.substring(5, 6).equals("1")){
						timer.isTue=true;			
					}
					
					if(sSet.substring(4, 5).equals("1")){
						timer.isWed=true;				
					}
					
					if(sSet.substring(3, 4).equals("1")){
						timer.isThurs=true;				
					}
					
					if(sSet.substring(2, 3).equals("1")){
						timer.isFri=true;		
					}
					
					if(sSet.substring(1, 2).equals("1")){
						timer.isSat=true;				
					}
					
					if(sSet.substring(0, 1).equals("1")){
						timer.isSun=true;					
					}
					
					timer.year=0;
					timer.month=0;
					timer.day=0;
					timer.hour=payload[i+5];
					timer.minute=payload[i+6];
				}else{
					timer.isLoop=false;
					timer.year=payload[i+2] + 2000;
					timer.month=payload[i+3];
					timer.day=payload[i+4];
					timer.hour=payload[i+5];
					timer.minute=payload[i+6];				
				}
				
				timer.cmdReturn=new CmdReturn();
				timer.cmdReturn.port=payload[i+7];
				timer.cmdReturn.cmd=payload[i+8];

				lstRet.add(timer);
			}
		}
		
		return lstRet;
	}
	
	
	public static HashMap<Byte,ControlCmdEnum> cmdReturn2Map(CmdReturn cmdReturn){
		HashMap<Byte,ControlCmdEnum> dicCommand=new HashMap<Byte,ControlCmdEnum>();
		
		String sPort=BytesUtil.byteToBit(cmdReturn.port);
		String sCmd=BytesUtil.byteToBit(cmdReturn.cmd);
		
		for(int i=0;i<8;i++){
			if(sPort.substring(i, i+1).equals("1"))
			{
				ControlCmdEnum cmdEnum;
				if(sCmd.substring(i, i+1).equals("1")){
					cmdEnum=ControlCmdEnum.Open;
				}else{
					cmdEnum=ControlCmdEnum.Close;
				}
				dicCommand.put((byte)(8-i),cmdEnum);
			}
		}
		
		return dicCommand;
	}
	
}
