package chipsea.wifiplug.lib.model;

import java.io.Serializable;

/**
 * @Description 控制命令
 * @author lixun
 * @CreateDate 2016/5/31
 *
 */
public class CmdReturn implements Serializable {
	/** 端口 (bit0到bit7分别对应端口1到端口8)*/
	public byte port;
	/** 开关 (bit0到bit7分别对应端口1到端口8的开关命令  1-打开 0-关闭)*/
	public byte cmd;

	@Override
	public String toString() {
		return "CmdReturn{" +
				"port=" + port +
				", cmd=" + cmd +
				'}';
	}
}
