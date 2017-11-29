package chipsea.wifiplug.lib.model;


/**
 * @Description 运行实体
 * @author lixun
 * @CreateDate 2016/5/31
 *
 */
public class RunningStatus {
	/** 端口 */
	public byte port;
	/** 上报时间 */
	public long reportTime;
	/** 电压 */
	public int voltage;
	/** 电流 */
	public int electricCurrent;
	/** 电能消耗 */
	public int powerConsumption;
	/** 瞬时功率 */
	public int instantaneousPower;

	/** 当前状态 */
	public boolean currentState;
	/** 当前是否存在故障 */
	public boolean hasFault;

	@Override
	public String toString() {
		return "RunningStatus{" +
				"port=" + port +
				", reportTime=" + reportTime +
				", voltage=" + voltage +
				", electricCurrent=" + electricCurrent +
				", powerConsumption=" + powerConsumption +
				", instantaneousPower=" + instantaneousPower +
				", currentState=" + currentState +
				", hasFault=" + hasFault +
				'}';
	}
}
