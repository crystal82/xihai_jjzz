package chipsea.wifiplug.lib.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * @Description 历史运行信息解析器
 * @author lixun
 * @CreateDate 2016/5/31
 */
public class RunningStatusHistoryParser{
	
	/**
	 * 根据设备上报的二进制流解析出对应的历史运行信息
	 * @param payload
	 * @return 历史运行信息列表
	 */
	public static List<RunningStatus> parse(byte[] payload){
		List<RunningStatus> lstRet=new ArrayList<RunningStatus>();
		
		if(payload.length>11){
			byte bPort=payload[0];
			
			for(int i=1;i<payload.length;i=i+11){
				RunningStatus runningStatus=new RunningStatus();
				runningStatus.port=bPort;
				
				int iYear=2000 + payload[i];
				String sDate=String.format("%s-%s-%s %s:%s:00", iYear,payload[i+1],payload[i+2],payload[i+3],payload[i+4]);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dReport=null;
				try {
					dReport = formatter.parse(sDate);
				} catch (ParseException e) {					
					e.printStackTrace();
					continue;
				}
				runningStatus.reportTime=dReport.getTime();
				if((payload[i+5] & 0x80)==0x80){
					runningStatus.currentState=true;
				}
				String sTmp=BytesUtil.byteToBit(payload[i+5]);
				//电流高3位
				String sEleCurrent=sTmp.substring(1, 4);
				//电压高4位
				String sVoltage=sTmp.substring(4, 8);
				
				sVoltage=sVoltage + BytesUtil.byteToBit(payload[i+6]);
				runningStatus.voltage=Integer.parseInt(sVoltage, 2);
				
				sEleCurrent=sEleCurrent + BytesUtil.byteToBit(payload[i+7]);
				runningStatus.electricCurrent=Integer.parseInt(sEleCurrent, 2);

				byte[] power=BytesUtil.subBytes(payload,i+8,2);
				runningStatus.instantaneousPower=BytesUtil.bytesToInt(power);

				runningStatus.hasFault =false;
				if((payload[i+10]&0x80)==0x80){
					runningStatus.hasFault =true;
				}
				runningStatus.powerConsumption= payload[i+10] & 0x7F;
				lstRet.add(runningStatus);
			}
		}
		
		return lstRet;
	}
}
