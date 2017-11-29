package chipsea.wifiplug.lib.model;

import java.util.HashMap;
import java.util.Iterator;



/**
 * @Description 控制命令生成器
 * @author lixun
 * @CreateDate 2016/5/31
 *
 */
public class CmdGenerator {
	
	private HashMap<Byte,ControlCmdEnum> _dicCommand;
	
	public CmdGenerator(){
		_dicCommand=new HashMap<Byte,ControlCmdEnum>();
	}
	
	public void addCmd(byte port,ControlCmdEnum cmd){
		if(!_dicCommand.containsKey(port)){
			_dicCommand.put(port, cmd);
		}
	}
	
	/**
	 * 根据Dictionary的控制内容生成CmdReturn
	 * @return 二进制的CmdReturn对象
	 */
	public CmdReturn generate(){
		CmdReturn ret=new CmdReturn();
		ret.port=0;
		ret.cmd=0;
		
		StringBuffer sbPort=new StringBuffer("00000000");
		StringBuffer sbCmd=new StringBuffer("00000000");
		
		
		Iterator iterator = _dicCommand.keySet().iterator();
		while (iterator.hasNext()) {
			byte port=(byte)iterator.next();
			ControlCmdEnum cmd=_dicCommand.get(port);
			
			if(port<8){
				int iStart=8-port;
				sbPort.replace(iStart, iStart+1, "1");
				if(cmd==ControlCmdEnum.Open){
					sbCmd.replace(iStart, iStart+1, "1");
				}
			}
		}

		ret.port=(byte)(Integer.parseInt(sbPort.toString(), 2));
		ret.cmd=(byte)(Integer.parseInt(sbCmd.toString(), 2));
		return ret;
		
	}

}
