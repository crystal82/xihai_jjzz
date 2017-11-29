package chipsea.wifiplug.lib.model;

import java.io.Serializable;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * @Description 定时设置实体
 * @author lixun
 * @CreateDate 2016/5/31
 *
 */
public class TimerSetting implements Serializable {
	//定时器是否打开或关闭
	public boolean isOpen;
	//定时器序号
	public byte indexId;
	//是否循环控制
	public boolean isLoop;
	//循环控制设置
	public byte loopSetting;
	//年
	public int year;
	//月
	public byte month;
	//日
	public byte day;
	//时
	public byte hour;
	//分
	public byte minute;
	//控制命令
	public CmdReturn cmdReturn;
	
	//星期一
	public boolean isMon;
	//星期二
	public boolean isTue;
	//星期三
	public boolean isWed;
	//星期四
	public boolean isThurs;
	//星期五
	public boolean isFri;
	//星期六
	public boolean isSat;
	//星期天
	public boolean isSun;

	@Override
	public String toString() {
		if(isLoop){
			return "TimerSetting{" +
					"isOpen=" + isOpen +
					", indexId=" + indexId +
					", isMon=" + isMon +
					", isTue=" + isTue +
					", isWed=" + isWed +
					", isThurs=" + isThurs +
					", isFri=" + isFri +
					", isSat=" + isSat +
					", isSun=" + isSun +
					", hour=" + hour +
					", minute=" + minute +
					", cmd=" + cmdReturn.toString() +
					'}';
		}else{
			return "TimerSetting{" +
					"isOpen=" + isOpen +
					", indexId=" + indexId +
					", year=" + year +
					", month=" + month +
					", day=" + day +
					", hour=" + hour +
					", minute=" + minute +
					", cmd=" + cmdReturn.toString() +
					'}';
		}

	}

	/**
	 * 根据类定义内容生成二进制指令
	 * @return 二进制指令
	 */
	public byte[] generate(){
		byte[] bRet=new byte[9];
		String sIndex=BytesUtil.byteToBit(indexId);
		
		if(isOpen){
			sIndex="1" + sIndex.substring(1, 8);
		}else{
			sIndex="0" + sIndex.substring(1, 8);
		}
		bRet[0]=(byte)(Integer.parseInt(sIndex, 2));
		
		if(isLoop){
			String sLoop="";
			if(isSun){
				sLoop="1";
			}else{
				sLoop="0";
			}
			
			if(isSat)
				sLoop+="1";
			else
				sLoop+="0";
			if(isFri)
				sLoop+="1";
			else
				sLoop+="0";
			if(isThurs)
				sLoop+="1";
			else
				sLoop+="0";
			if(isWed)
				sLoop+="1";
			else
				sLoop+="0";
			if(isTue)
				sLoop+="1";
			else
				sLoop+="0";
			if(isMon)
				sLoop+="1";
			else
				sLoop+="0";
			sLoop+="1";
			bRet[1]=(byte)(Integer.parseInt(sLoop, 2));
			
			bRet[2]=0;
			bRet[3]=0;
			bRet[4]=0;
			bRet[5]=hour;
			bRet[6]=minute;
			bRet[7]=cmdReturn.port;
			bRet[8]=cmdReturn.cmd;
			
		}else{
			bRet[1]=0;
			
			bRet[2]=(byte)(year-2000);
			bRet[3]=month;
			bRet[4]=day;
			bRet[5]=hour;
			bRet[6]=minute;
			bRet[7]=cmdReturn.port;
			bRet[8]=cmdReturn.cmd;
		}
		
		return bRet;
	}

	
	
}
