/*
package chipsea.wifiplug.lib.util;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.chipsea.configuration.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.model.CmdGenerator;
import chipsea.wifiplug.lib.model.CmdReturn;
import chipsea.wifiplug.lib.model.CmdWordDefine;
import chipsea.wifiplug.lib.model.ControlCmdEnum;
import chipsea.wifiplug.lib.model.RunningStatus;
import chipsea.wifiplug.lib.model.RunningStatusParser;
import chipsea.wifiplug.lib.model.TimerSetting;
import chipsea.wifiplug.lib.model.TimerSettingParser;

public class csACUtil {
	public static int ERR_RESP=20001; //指令应答失败
	
	public static int SEND_DEVICE_OPTION=AC.CLOUD_FIRST;
	
	public static int OFFLINE=0; //不在线
	public static int NETWORK_ONLINE=1; //云端在线
	public static int LOCAL_ONLINE=2; //局域网在线
	public static int BOTH_ONLINE=3; //云端与局域网在线
	
	
	private static ACDeviceActivator activator;
	private static ACDeviceActivator getActivatorInstance(){
		if(activator==null){
			//activator=AC.deviceActivator(AC.DEVICE_MX);
			//activator=AC.deviceActivator(AC.DEVICE_AI6060H); //南方硅谷
			activator=AC.deviceActivator(AC.DEVICE_ESP8266); //上海乐鑫
		}
		return activator;
	}

	*/
/**
	 *
	 * @Description 初始化
	 *//*

	public static void init(Application app){
		if(Config.APP_MODE==0){
			AC.init(app, Config.MAJORDOAMIN,Config.MAJORDOMAINID,AC.TEST_MODE);
		}else{
			AC.init(app, Config.MAJORDOAMIN,Config.MAJORDOMAINID);
			AC.setRegional(Config.APP_REGIONAL);
		}

	}
	
	
	*/
/**
     * 
     * @Description 通过smartconfig技术，使设备连上wifi，每次只绑定一台设备 只支持配置手机当前连接的wifi
     * @param SSID             wifi的SSID
     * @param password         SSID对应的wifi密码
     * @return 通过回调callback，返回一个带physicalDeviceId和subDomainId（用来区分设备类型）的对象
     *//*

	public static void startAbleLink(String SSID, String password, PayloadCallback<List<ACDeviceBind>> callback){
		getActivatorInstance().startAbleLink(SSID, password, AC.DEVICE_ACTIVATOR_DEFAULT_TIMEOUT, callback);
	}
	
	*/
/**
	 * @Description 获取APP端当前连接wifi的SSID
	 * @return 端当前连接wifi的SSID
	 *//*

	public static String getSSID(){
		return getActivatorInstance().getSSID();
	}
	
	*/
/**
	 * @Description 是否处于smartConfig
	 * @return  是否处于smartConfig
	 *//*

	public static boolean isAbleLink(){
		return getActivatorInstance().isAbleLink();
	}
	
	*/
/**
	 * @Description 停止连接,使用场景：配置过程中用户主动取消配置
	 *//*

	public static void stopAbleLink(){
		getActivatorInstance().stopAbleLink();
	}
	
	
	
	*/
/**
	 * @Description 退订运行状态监控
	 * @param acTable	订阅表对象
	 * @param callback	结果回调
	 *//*

	public static void unSubscribe(ACPushTable acTable,VoidCallback callback){
		AC.pushMgr().unwatch(acTable, callback);
	}
	
	*/
/**
	 * @Description 开始接收运行状态监控数据
	 * @param callback	结果回调
	 *//*

	public static void receiveRunningState(PayloadCallback<ACPushReceive> callback){
		AC.pushMgr().onReceive(callback);
	}
	
	
	*/
/**
	 * @Description 修改绑定设备名称
	 * @param deviceId	设备的逻辑ID
	 * @param subDomainId 	子域ID
	 * @param name	要修改的名称
	 * @param callback	结果回调
	 *//*

	public static void changeName(long deviceId, long subDomainId,String name, VoidCallback callback){
		AC.bindMgr().changeName(Config.getSubDomainById(subDomainId), deviceId, name, callback);
	}
	
	
	*/
/**
	 * @Description 订阅运行状态监控
	 * @param physicalDeviceId	设备的物理ID
	 * @param port	设备的插孔编号
	 * @param callback	结果回调
	 *//*

	public static void subscribe(final String physicalDeviceId,final long port,final PayloadCallback<ACPushTable> callback){
		try {
			AC.pushMgr().connect(new VoidCallback() {
				@Override
				public void error(ACException e) {
					if (callback != null) {
						callback.error(e);
					}
				}

				@Override
				public void success() {
					final ACPushTable table = new ACPushTable();
					table.setClassName("cs_wifiplug_runningstate");
					//table.setColumes(new String[]{"currentState", "hasFault", "voltage", "electricCurrent"});
					ACObject primaryKey = new ACObject();
					primaryKey.put("physicalDeviceId", physicalDeviceId); //设置要监听的设备Id
					primaryKey.put("plugPort", port);
					table.setPrimaryKey(primaryKey);
					table.setOpType(ACPushTable.OPTYPE_CREATE | ACPushTable.OPTYPE_DELETE | ACPushTable.OPTYPE_REPLACE | ACPushTable.OPTYPE_UPDATE);
					AC.pushMgr().watch(table, new VoidCallback() {
						@Override
						public void error(ACException e) {
							if (callback != null) {
								callback.error(e);
							}
						}

						@Override
						public void success() {
							if (callback != null) {
								callback.success(table);
							}
						}

					});
				}

			});
		}catch (Exception ex){
			LogUtil.e(Config.TAG,ex.getMessage());
		}
	}

	*/
/**
	 * @Description 订阅插座定时器变化
	 * @param physicalDeviceId	设备的物理ID
	 * @param callback	结果回调
	 *//*

	public static void subscribeTimer(final String physicalDeviceId,final PayloadCallback<ACPushTable> callback){
		try {
			AC.pushMgr().connect(new VoidCallback() {
				@Override
				public void error(ACException e) {
					if (callback != null) {
						callback.error(e);
					}
				}

				@Override
				public void success() {
					final ACPushTable table = new ACPushTable();
					table.setClassName("cs_wifiplug_timerchange");
					ACObject primaryKey = new ACObject();
					primaryKey.put("physicalDeviceId", physicalDeviceId); //设置要监听的设备Id
					primaryKey.put("plugPort", 1);
					table.setPrimaryKey(primaryKey);
					table.setOpType(ACPushTable.OPTYPE_CREATE | ACPushTable.OPTYPE_REPLACE | ACPushTable.OPTYPE_UPDATE);
					AC.pushMgr().watch(table, new VoidCallback() {
						@Override
						public void error(ACException e) {
							if (callback != null) {
								callback.error(e);
							}
						}

						@Override
						public void success() {
							if (callback != null) {
								callback.success(table);
							}
						}

					});
				}

			});
		}catch (Exception ex){
			LogUtil.e(Config.TAG,ex.getMessage());
		}
	}


	*/
/**
	 * @Description 订阅空调运行状态监控
	 * @param physicalDeviceId	设备的物理ID
	 * @param callback	结果回调
	 *//*

	public static void subscribeAir(final String physicalDeviceId,final PayloadCallback<ACPushTable> callback){
		try {
			AC.pushMgr().connect(new VoidCallback() {
				@Override
				public void error(ACException e) {
					if (callback != null) {
						callback.error(e);
					}
				}

				@Override
				public void success() {
					final ACPushTable table = new ACPushTable();
					table.setClassName("cs_ac_status");
					ACObject primaryKey = new ACObject();
					primaryKey.put("physicalDeviceId", physicalDeviceId); //设置要监听的设备Id
					table.setPrimaryKey(primaryKey);
					table.setOpType(ACPushTable.OPTYPE_CREATE | ACPushTable.OPTYPE_REPLACE | ACPushTable.OPTYPE_UPDATE);
					AC.pushMgr().watch(table, new VoidCallback() {
						@Override
						public void error(ACException e) {
							if (callback != null) {
								callback.error(e);
							}
						}

						@Override
						public void success() {
							if (callback != null) {
								callback.success(table);
							}
						}

					});
				}

			});
		}catch (Exception ex){
			LogUtil.e(Config.TAG,ex.getMessage());
		}
	}
	
	*/
/**
	 * @Description 从云端获取所有设备列表和设备状态(sdk会自动保存列表到本地缓存中) 如果从云端获取失败会直接从本地缓存中获取设备列表和本地局域网状态
	 * @param callback	结果回调
	 *//*

	public static void listDevicesWithStatus(PayloadCallback<List<ACUserDevice>> callback){
		AC.bindMgr().listDevicesWithStatus(callback);
	}

	
	*/
/**
	 * @Description 使用芯海的解绑方法解绑设备（对于管理员的用户解绑调用此方法）
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	  子域id
	 * @param isForce   是否强制解绑 0-不强制，会检查当前用户是否是管理员 1-强制解绑
	 * @param callback	结果回调
	 *//*

	public static void csUnbindDevice(String physicalDeviceId,long subDomainId,int isForce,final VoidCallback callback){
		ACMsg req=new ACMsg();
		req.setName("handleUnbindDevice");
		req.put("physicalDeviceId", physicalDeviceId);
		req.put("isForce", isForce);
		AC.sendToService(null, Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
		//AC.sendToService(Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACMsg acMsg) {
				if(callback!=null){
					callback.success();
				}
			}
			
		});
	}

	
	*/
/**
	 * @Description 本地绑定后调用UDS在云端进行注册
	 * @param deviceId	设备的物理ID
	 * @param subDomainId	 子域id
	 * @param timezone	 时区偏移
	 * @param callback	结果回调
	 *//*

	public static void csRegBindDevice(long deviceId,long subDomainId,int timezone,final VoidCallback callback){
		ACMsg req=new ACMsg();
		req.setName("handleRegBindDevice");
		req.put("deviceId", deviceId);
		req.put("timezone",timezone);
		AC.sendToService(null,Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACMsg acMsg) {
				if(callback!=null){
					callback.success();
				}
			}

		});
	}

	*/
/**
	 * @Description 获取分享码（只有管理员可以获取 ）
	 * @param deviceId	设备的逻辑ID
	 * @param subDomainId
	 * @param callback	结果回调
	 *//*

	public static void getShareCode(long deviceId,long subDomainId, PayloadCallback<String> callback){
		AC.bindMgr().getShareCode(Config.getSubDomainById(subDomainId),deviceId,callback);
	}


	*/
/**
	 * @Description 通过分享码绑定设备
	 * @param shareCode	分享码
	 * @param callback	结果回调
	 *//*

	public static void bindDeviceWithShareCode(String shareCode,PayloadCallback<ACUserDevice> callback){
		AC.bindMgr().bindDeviceWithShareCode(shareCode,callback);
	}

	*/
/**
	 * 设备的管理员用户解除设备与非管理员用户的绑定关系。
	 * @param deviceId  设备的逻辑ID。
	 * @param subDomainId
	 * @param userid
	 * @throws Exception
	 *//*

	public static void unbindDeviceWithUser(long deviceId,long subDomainId,long userid,VoidCallback callback){
		AC.bindMgr().unbindDeviceWithUser(Config.getSubDomainById(subDomainId),userid,deviceId,callback);
	}

	*/
/**
	 * @Description 使用AbleClound的解绑方法解绑设备（对于非管理员的用户解绑调用此方法）
	 * @param deviceId	设备的逻辑ID
	 * @param callback	结果回调
	 *//*

	public static void acUnbindDevice(long deviceId,long subDomainId,VoidCallback callback){
		AC.bindMgr().unbindDevice(Config.getSubDomainById(subDomainId), deviceId, callback);
	}


	*/
/**
	 * @Description 列出当前设备所有用户列表
	 * @param deviceId
	 * @param subDomainId
	 * @param callback	结果回调
	 *//*

	public static void listUsers(long deviceId,long subDomainId, PayloadCallback<List<ACDeviceUser>> callback){
		AC.bindMgr().listUsers(Config.getSubDomainById(subDomainId),deviceId,callback);
	}
	

//	public static void csRemoveAllAcountf4Test(final VoidCallback callback){
//		ACMsg req=new ACMsg();
//		req.setName("handleCleanAllForTest");
//		AC.sendToService(Config.SUBDOMAIN, Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
//			@Override
//			public void error(ACException e) {
//				if(callback!=null){
//					callback.error(e);
//				}
//			}
//
//			@Override
//			public void success(ACMsg acMsg) {
//				if(callback!=null){
//					callback.success();
//				}
//			}
//
//		});
//	}
	
	*/
/**
	 * @Description 查询设备是否被绑定
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param callback	结果回调
	 *//*

	public static void isDeviceBound(String physicalDeviceId,long subDomainId, PayloadCallback<Boolean> callback){
		AC.bindMgr().isDeviceBound(Config.getSubDomainById(subDomainId), physicalDeviceId, callback);
	}
	
	
	*/
/**
	 * @Description 使用芯海的绑定方法绑定设备
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	 子域id
	 * @param callback	结果回调
	 *//*

	@Deprecated
	public static void csBindDevice(String physicalDeviceId,long subDomainId,final PayloadCallback<Long> callback){
		ACMsg req=new ACMsg();
		req.setName("handleBindDevice");
		req.put("physicalDeviceId", physicalDeviceId);
		req.put("deviceName", "");
		AC.sendToService(null,Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACMsg acMsg) {
				long deviceId=acMsg.get("deviceId");
				if(callback!=null){
					callback.success(deviceId);
				}
			}
			
		});
		
	}
	
	
	*/
/**
	 * @Description 使用ableCloud的绑定方法绑定设备
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域ID
	 * @param callback	结果回调
	 *//*

	public static void acBindDevice(String physicalDeviceId,long subDomainId,final PayloadCallback<Long> callback){
		AC.bindMgr().bindDevice(Config.getSubDomainById(subDomainId), physicalDeviceId, "", new PayloadCallback<ACUserDevice>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACUserDevice acUserDevice) {
				long deviceId=acUserDevice.deviceId;
				if(callback!=null){
					callback.success(deviceId);
				}
			}
			
		});
		
	}
	

	*/
/**
	 * @Description 打开wifi插座
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	 子域id
	 * @param port	设备的插孔编号
	 * @param isOpen	是否打开开关
	 * @param callback	结果回调
	 *//*

	public static void openOrClosePlug(String physicalDeviceId,long subDomainId,byte port,boolean isOpen,int status,final VoidCallback callback) {
		CmdGenerator generator=new CmdGenerator();
		if(isOpen){
			generator.addCmd(port, ControlCmdEnum.Open);
		}else{
			generator.addCmd(port, ControlCmdEnum.Close);
		}
		CmdReturn cmd=generator.generate();
		byte[] buffer=new byte[2];
		buffer[0]=cmd.port;
		buffer[1]=cmd.cmd;
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.CONTROL,buffer,"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);

		int iSendOption=SEND_DEVICE_OPTION;
		if(status==csACUtil.LOCAL_ONLINE){
			iSendOption=AC.LOCAL_FIRST;
		}

		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, iSendOption, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}

		});
	}

	*/
/**
	 * @Description 发送红外码库
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param data    数据内容
	 * @param callback	结果回调
	 *//*

	public static void IRControl(String physicalDeviceId, long subDomainId,byte[] data, final VoidCallback callback){
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.IR_CONTROL,data,"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);

		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}

		});
	}
	*/
/**
	 * @Description 发送
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param data    数据内容
	 * @param callback	结果回调
	 *//*

	public static void airControl(String physicalDeviceId, long subDomainId,byte[] data, final VoidCallback callback){
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.AIR_CONTROL,data,"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);

		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}

		});
	}
	*/
/**
	 * @Description 匹配空调
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param data    数据内容
	 * @param callback	结果回调
	 *//*

	public static void matchAir(String physicalDeviceId, long subDomainId,byte[] data, final PayloadCallback<byte[]> callback){
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.IR_CONTROL,data,"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);

		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				String sRet=BytesUtil.bytesToHexString(resp);
				Log.i("csACuUtil", "success: sRet=" + sRet);
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success(resp);
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}

		});
	}
	*/
/**
	 * @Description 查询当前设备的实时运行信息
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param callback	结果回调
	 *//*

	public static void queryRunningState(String physicalDeviceId, long subDomainId, final PayloadCallback<List<RunningStatus>> callback){
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.RUNNING_QUERY,new byte[1],"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);
		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {                    				
				byte[] resp = deviceMsg.getContent();
				List<RunningStatus> lstRunning=RunningStatusParser.parse(resp);
				if(callback!=null){
					callback.success(lstRunning);
				}
			}
    		
    	});
	}
	
	
	*/
/**
	 * @Description 查询指定设备的当前状态(建议周期性查询)
	 * @param physicalDeviceId	设备的物理ID
	 * @param callback	结果回调
	 *//*

	public static void queryDeviceStatus(final String physicalDeviceId,final long subDomainId,final PayloadCallback<Integer> callback){
		if(ACUtils.isNetworkConnected()){
			AC.bindMgr().isDeviceOnline(Config.getSubDomainById(subDomainId), 0, physicalDeviceId, new PayloadCallback<Boolean>(){
				@Override
				public void error(ACException e) {
					queryDeviceLocalStatus(physicalDeviceId,false,callback);
				}

				@Override
				public void success(Boolean bOnline) {
					queryDeviceLocalStatus(physicalDeviceId,bOnline,callback);
				}
				
			});
		}else{
			queryDeviceLocalStatus(physicalDeviceId,false,callback);
		}
	}
	
	
	private static void queryDeviceLocalStatus(final String physicalDeviceId,final boolean outerNetOnline, final PayloadCallback<Integer> callback){
		if(ACUtils.isWifiConnected()){
			AC.findLocalDevice(2000,new PayloadCallback<List<ACDeviceFind>>(){
				@Override
				public void error(ACException e) {
					if(callback!=null){
						if(outerNetOnline){
							callback.success(NETWORK_ONLINE);
						}else{
							callback.error(e);
						}
						
					}
				}

				@Override
				public void success(List<ACDeviceFind> deviceFinds) {
					if(deviceFinds.size()==0){
						if(outerNetOnline){
							callback.success(NETWORK_ONLINE);
						}else{
							callback.success(OFFLINE);
						}
					}else{
						for (ACDeviceFind deviceFind : deviceFinds) {
							if (deviceFind.getPhysicalDeviceId().equalsIgnoreCase(physicalDeviceId)) {
								if(callback!=null){
									if(outerNetOnline){
										callback.success(BOTH_ONLINE);
									}else{
										callback.success(LOCAL_ONLINE);
									}

								}
								break;
							}
						}
					}
				}
				
			});
		}else{
			if(callback!=null){
				if(outerNetOnline){
					callback.success(NETWORK_ONLINE);
				}else{
					callback.success(OFFLINE);
				}
			}
		}
	}
	
	
	
	*/
/**
	 * @Description 删除指定的定时器
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域Id
	 * @param indexId			要删除的定时器序号 0为删除所有
	 * @param callback	结果回调
	 *//*

	public static void removeTimer(String physicalDeviceId,long subDomainId,byte indexId, final VoidCallback callback){
		byte buffer[]=new byte[1];
    	buffer[0]=indexId;
    	ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.TIMER_REMOVE,buffer,"");
    	deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);
    	AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){

			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(new ACException(ERR_RESP));
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {                    				
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}
    		
    	});
	}
	
	
	*/
/**
	 * @Description 查询所有的定时器
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId 	子域id
	 * @param indexId			定时器序号 0为查询所有
	 * @param callback	结果回调
	 *//*

	public static void queryTimer(String physicalDeviceId,long subDomainId,byte indexId,final PayloadCallback<List<TimerSetting>> callback){
		byte buffer[]=new byte[1];
    	buffer[0]=indexId;
    	ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.TIMER_QUERY,buffer,"");
    	deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);
    	AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				List<TimerSetting> allTimer=TimerSettingParser.parse(resp);
				if(callback!=null){
					callback.success(allTimer);
				}
			}
    		
    	});
	}
	
	*/
/**
	 * @Description 设置定时器（包括 新增和修改）
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	 子域ID
	 * @param setting			定时器设置
	 * @param callback	结果回调
	 *//*

	public static void setTimer(String physicalDeviceId,long subDomainId,TimerSetting setting,final VoidCallback callback){
		byte[] buffer=setting.generate();
    	ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.TIMER_SET,buffer,"");
    	deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);
    	AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}
    		
    	});
	}


	*/
/**
	 * @Description 上传文件
	 * @param filePath	文件路径
	 * @param fileName	文件名
	 * @param callback	结果回调
	 *//*

	public static void uploadFile(String filePath,String fileName,VoidCallback callback){
		ACFileMgr fileMgr = AC.fileMgr();
		ACFileInfo fileInfo=new ACFileInfo(Config.DEFAULTSUBDOMAIN,fileName);
		fileInfo.setFile(new File(filePath,fileName));
		fileMgr.uploadFile(fileInfo,null,callback);
	}

	*/
/**
	 * @Description 下载文件
	 * @param filePath	文件路径
	 * @param fileName	文件名
	 * @param callback	结果回调
	 *//*

	public static void downloadFile(final String filePath, final String fileName, final VoidCallback callback){
		final ACFileMgr fileMgr = AC.fileMgr();
		ACFileInfo fileInfo=new ACFileInfo(Config.DEFAULTSUBDOMAIN,fileName);
		fileMgr.getDownloadUrl(fileInfo, 24 * 60 * 60, new PayloadCallback<String>() {
			@Override
			public void success(String s) {
				ACUtils.createSDDir(filePath);
				File file = null;
				try {
					file = ACUtils.createSDFile(filePath + fileName);
					fileMgr.downloadFile(file, s, 0, null, callback);
				} catch (IOException e) {
				}
			}

			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}
		});
	}


	*/
/**
	 * @Description 打开或关闭定时器
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	 子域id
	 * @param indexId			定时器序号 0--操作所有定时器
	 * @param action			1-表示打开 0--表示关闭
	 * @param callback	结果回调
	 *//*

	public static void openOrCloseTimer(String physicalDeviceId, long subDomainId, byte indexId,byte action,final VoidCallback callback){
		byte[] buffer=new byte[2];
		buffer[0]=indexId;
		buffer[1]=action;
		ACDeviceMsg deviceMsg=new ACDeviceMsg(CmdWordDefine.OPENCLOSE_TIMER,buffer,"");
		deviceMsg.setSecurityMode(ACDeviceSecurityMode.DYNAMIC_ENCRYPTED);
		AC.bindMgr().sendToDeviceWithOption(Config.getSubDomainById(subDomainId), physicalDeviceId, deviceMsg, SEND_DEVICE_OPTION, new PayloadCallback<ACDeviceMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACDeviceMsg deviceMsg) {
				byte[] resp = deviceMsg.getContent();
				if (resp[0] == 1) {
					if(callback!=null){
						callback.success();
					}
				}else{
					if(callback!=null){
						callback.error(new ACException(ERR_RESP));
					}
				}
			}
    		
    	});
	}

	/*/
/************************************************报表**************************************************************************
	*/
/**
	 * @Description 查询天小时报表
	 * @param physicalDeviceId	设备的物理ID
	 * @param subDomainId	 子域id
	 * @param startTs
	 * @param endTs
	 * @param callback	结果回调
	 *//*

	public static void QueryHourOfDayReport(String physicalDeviceId,long subDomainId,long startTs,long endTs,final PayloadCallback<List<ACObject>> callback){
		ACMsg req=new ACMsg();
		req.setName("handleQueryHourRpt");
		req.put("physicalDeviceId", physicalDeviceId);
		req.put("startDay", startTs);
		req.put("endDay", endTs);
		AC.sendToService(null,Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}
			
			@Override
			public void success(ACMsg acMsg) {
				List<ACObject> result=acMsg.get("result");
				if(callback!=null){
					callback.success(result);
				}
			}
			
		});
		
	}


	/*/
/************************************************报表**************************************************************************
	*/
/**
	 * @Description 根据脂定的日期查询天报表
	 * @param subDomainId	 子域id
	 * @param physicalDeviceId	设备的物理ID
	 * @param startTs
	 * @param endTs
	 * @param callback	结果回调
	 *//*

	public static void QueryDayReport(String physicalDeviceId,long subDomainId,long startTs,long endTs,final PayloadCallback<List<ACObject>> callback){
		ACMsg req=new ACMsg();
		req.setName("handleQueryDayRpt");
		req.put("physicalDeviceId", physicalDeviceId);
		req.put("startDay", startTs);
		req.put("endDay", endTs);
		//AC.sendToService(Config.getSubDomainById(subDomainId),Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
		AC.sendToService(null,Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACMsg acMsg) {
				List<ACObject> result=acMsg.get("result");
				if(callback!=null){
					callback.success(result);
				}
			}

		});

	}
	
	
	/*/
/************************************************用户中心**************************************************************************


	public static void getAccountInfo(long accountId,final PayloadCallback<ACObject> callback){
		ACMsg req=new ACMsg();
		req.setName("handleQueryAccount");
		req.put("accountId", accountId);
		//AC.sendToService(Config.DEFAULTSUBDOMAIN, Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
		AC.sendToService(null, Config.UDSSERVICENAME, Config.UDSSERVICEVERSION, req, new PayloadCallback<ACMsg>(){
			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}

			@Override
			public void success(ACMsg acMsg) {
				ACObject result=acMsg.get("result");
				if(callback!=null){
					callback.success(result);
				}
			}

		});

	}

	*/
/**
	 * @Description		修改名字
	 * @param nickName  帐号名，注册时候email或phone任选其一
	 * @param callback 返回结果的监听回调
	 *//*

	public static void changeNickName(String nickName, VoidCallback callback){
		AC.accountMgr().changeNickName(nickName,callback);
	}

	*/
/**
     * @Description		用户登录 	
     * @param account  帐号名，注册时候email或phone任选其一
     * @param password 用户密码
     * @param callback 返回结果的监听回调
     *//*

	public static void login(String account, String password, PayloadCallback<ACUserInfo> callback){
		AC.accountMgr().login(account, password, callback);
	}


	*/
/**
	 * @Description		设置用户头像
	 * @param data  头像二进制对像
	 * @param callback 返回结果的监听回调
	 *//*

	public static void setAvatar(byte[] data ,final PayloadCallback<String> callback){
		AC.accountMgr().setAvatar(data,callback);
	}

	*/
/**
	 * @Description		获取用户头像
	 * @param accountId  帐号Id
	 * @param callback 返回结果的监听回调
	 *//*

	public static void getUserAvatar(long accountId, final PayloadCallback<String> callback){
		List<Long> lstAccount=new ArrayList<>();
		lstAccount.add(accountId);

		AC.accountMgr().getPublicProfiles(lstAccount, new PayloadCallback<List<ACObject>>() {
			@Override
			public void success(List<ACObject> acObjects) {
				String sUrl="";

				if(acObjects.size()>0){
					if(acObjects.get(0).contains("_avatar")){
						sUrl=acObjects.get(0).getString("_avatar");
					}
				}
				if(callback!=null){
					callback.success(sUrl);
				}
			}

			@Override
			public void error(ACException e) {
				if(callback!=null){
					callback.error(e);
				}
			}
		});
	}
	
	
	*/
/**
     * 
     * @Description		注册一个新用户
     * @param email      用户邮箱，与phone任选其一，或都提供
     * @param phone      用户电话，或email任选其一，或都提供
     * @param password   用户密码
     * @param name       名字
     * @param verifyCode 验证码
     * @param callback   返回结果的监听回调
     *//*

    public static void register(String email, String phone, String password, String name, String verifyCode, PayloadCallback<ACUserInfo> callback){
    	AC.accountMgr().register(email, phone, password, name, verifyCode, callback);
    }
    
    
    */
/**
     * @Description		发送验证码
     * @param account  用户电话或email任选其一
     * @param template 短信内容模板
     * @param callback 返回结果的监听回调
     *//*

    public static void sendVerifyCode(String account, int template, VoidCallback callback){
    	AC.accountMgr().sendVerifyCode(account, template, callback);
    }
    
    
    */
/**
     * 
     * @Description		验证验证码
     * @param account    用户电话或email任选其一
     * @param verifyCode 验证码
     * @param callback   返回结果的监听回调
     *//*

    public static void checkVerifyCode(String account, String verifyCode, PayloadCallback<Boolean> callback){
    	AC.accountMgr().checkVerifyCode(account, verifyCode, callback);
    }
    
    
    */
/**
     * @Description 注销
     *//*

    public static void logout(){
    	AC.accountMgr().logout();
    }
    
    */
/**
     * @Description 是否登录
     *//*

    public static boolean isLogin(){
    	return AC.accountMgr().isLogin();
    }
    
    */
/**
     * @Description	检查账号是否存在
     * @param account  帐号名，email或phone任选其一
     * @param callback 返回结果的监听回调
     *//*

    public static void checkExist(String account, PayloadCallback<Boolean> callback){
    	AC.accountMgr().checkExist(account,callback);
    }
	
    
    */
/**
     * @Description	修改密码
     * @param oldPswd  旧密码
     * @param newPswd  新密码
     * @param callback 返回结果的监听回调
     *//*

    public static void changePassword(String oldPswd, String newPswd, VoidCallback callback){
    	AC.accountMgr().changePassword(oldPswd, newPswd, callback);
    }
    
    */
/**
     * @Description 	第三方账号登录
     * @param thirdPlatform 第三方类型（如QQ、微信、微博、FaceBook等）
     * @param openId        通过第三方登录获取的openId
     * @param accessToken   通过第三方登录获取的accessToken
     * @param callback      返回结果的监听回调
     *//*

    public static void loginWithOpenId(ACThirdPlatform thirdPlatform, String openId, String accessToken, PayloadCallback<ACUserInfo> callback){
    	AC.accountMgr().loginWithOpenId(thirdPlatform, openId, accessToken, callback);
    }
    
    */
/**
     * 
     * @Description		重置密码
     * @param account  帐号名，注册时候email或phone任选其一
     * @param pswd     新密码
     * @param callback 返回结果的监听回调
     *//*

    public static void resetPassword(String account, String pswd, String verifyCode, PayloadCallback<ACUserInfo> callback){
    	AC.accountMgr().resetPassword(account, pswd, verifyCode, callback);
    }
    
    */
/**
     * @Description 绑定第三方账号
     * @param thirdPlatform 第三方类型（如QQ、微信、微博、FaceBook等）
     * @param openId        通过第三方登录获取的openId
     * @param accessToken   通过第三方登录获取的accessToken
     * @param callback      返回结果的监听回调
     *//*

    public static void bindWithOpenId(ACThirdPlatform thirdPlatform, String openId, String accessToken, VoidCallback callback){
    	AC.accountMgr().bindWithOpenId(thirdPlatform, openId, accessToken, callback);
    }
    
    
    */
/**
     * @Description 第三方账号登录状态下绑定用户信息
     * @param email      用户邮箱，与phone任选其一，或都提供
     * @param phone      用户电话，或email任选其一，或都提供
     * @param password   用户密码
     * @param nickName   名字
     * @param verifyCode 验证码
     * @param callback   返回结果的监听回调
     *//*

    public static void bindWithAccount(String email, String phone, String password, String nickName, String verifyCode, VoidCallback callback){
    	AC.accountMgr().bindWithAccount(email, phone, password, nickName, verifyCode, callback);
    }
    
    */
/**
     * @Description 列举所有的第三方登录信息
     * @param callback 返回结果的监听回调
     *//*

    public static void listAllOpenIds(PayloadCallback<List<ACOpenIdInfo>> callback){
    	AC.accountMgr().listAllOpenIds(callback);
    }


	*/
/**
	 * @Description 提交用户反馈信息
	 * @param description 描述
	 * @param callback 返回结果的监听回调
	 *//*

	public static void submitFeedback(String description,VoidCallback callback){
		ACFeedback feedback = new ACFeedback();
		feedback.addFeedback("description",description);
		feedback.setSubDomain(Config.DEFAULTSUBDOMAIN);
		String strType="B:" + Build.BRAND  + " D:"   + Build.DEVICE + " DISPLAY:" + Build.DISPLAY + " M:" + Build.MANUFACTURER
				+ " M:" + Build.MODEL + " P:" + Build.PRODUCT;
		feedback.addFeedback("phonemodel",strType);
		AC.feedbackMgr().submitFeedback(feedback,callback);
	}

}
*/
