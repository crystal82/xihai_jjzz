package chipsea.wifiplug.lib.model;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * @Description 实时运行信息解析器
 * @author lixun
 * @CreateDate 2016/5/31
 */
public class RunningStatusParser {
	
	/**
	 * 根据设备上报的二进制流解析出对应的实时运行信息
	 * @param payload 二进制流
	 * @return 实时运行信息列表
	 */
	public static List<RunningStatus> parse(byte[] payload){
		List<RunningStatus> lstRet=new ArrayList<RunningStatus>();
		
		if(payload.length>6){
			for(int i=0;i<payload.length;i=i+7){
				RunningStatus runningStatus=new RunningStatus();
				runningStatus.port=payload[i];
				runningStatus.reportTime=System.currentTimeMillis();
				
				if((payload[i+1] & 0x80)==0x80){
					runningStatus.currentState=true;
				}
				String sTmp=BytesUtil.byteToBit(payload[i+1]);
				//电流高3位
				String sEleCurrent=sTmp.substring(1, 4);
				//电压高4位
				String sVoltage=sTmp.substring(4, 8);
				sVoltage=sVoltage + BytesUtil.byteToBit(payload[i+2]);
				runningStatus.voltage=Integer.parseInt(sVoltage, 2);
				sEleCurrent=sEleCurrent + BytesUtil.byteToBit(payload[i+3]);
				runningStatus.electricCurrent=Integer.parseInt(sEleCurrent, 2);
				//runningStatus.powerConsumption= (payload[i+4]);

				byte[] power=BytesUtil.subBytes(payload,i+4,2);
				runningStatus.instantaneousPower=BytesUtil.bytesToInt(power);

				runningStatus.hasFault =false;
				if((payload[i+6]&0x80)==0x80){
					runningStatus.hasFault =true;
				}
				runningStatus.powerConsumption=payload[i+6] & 0x7F;
				lstRet.add(runningStatus);
			}
		}
		
		return lstRet;
	}
}
