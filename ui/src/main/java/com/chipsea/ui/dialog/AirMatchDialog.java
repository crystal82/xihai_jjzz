//package com.chipsea.ui.dialog;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.chipsea.code.util.AirUtlis;
//import com.chipsea.code.util.DownloadIRFileAsyncTask;
//import com.chipsea.code.util.FileUtil;
//import com.chipsea.mode.entity.AirModel;
//import com.chipsea.mode.entity.DeviceInfo;
//import com.chipsea.ui.R;
//import com.chipsea.ui.utils.PointLayoutUtils;
//import com.chipsea.view.dialog.BaseDialog;
//import com.chipsea.view.text.CustomTextView;
//
//
//public class AirMatchDialog extends BaseDialog{
//
//	private CustomTextView tipText;
//	private ImageView img;
//	private Context _context;
//	private LinearLayout pointLayout ;
//	private PointLayoutUtils pointLayoutUtils ;
//	private SendCoderImp sendCoderImp ;
//	public AirMatchDialog(Context context) {
//		super(context);
//		_context=context;
//		View vv = LayoutInflater.from(context).inflate(
//				R.layout.dialog_air_match, null);
//		addView(vv);
//		dialog.setCancelable(false);
//		img = (ImageView) vv.findViewById(R.id.img);
//		tipText = (CustomTextView) vv.findViewById(R.id.tipText);
//		pointLayout = (LinearLayout) vv.findViewById(R.id.pointLayout);
//		pointLayoutUtils = new PointLayoutUtils(pointLayout) ;
//		pointLayoutUtils.startAnim();
//	}
//	public long dowloadText(int keyfile){
//		tipText.setText(_context.getString(R.string.air_matching_dialog_tip1)) ;
//		img.setImageResource(R.mipmap.air_match_dialog_top1);
//		return AirUtlis.dowloadText(_context,keyfile + ".txt") ;
//	}
//	public void sendCode(AirModel currModel, DeviceInfo currDeviceInfo,SendCoderImp sendCoderImp,String srvInfo){
//		tipText.setText(_context.getString(R.string.air_matching_dialog_tip2)) ;
//		this.sendCoderImp = sendCoderImp ;
//		new SendTextAsyncTask(_context, FileUtil.PATH_CODES + currModel.getM_keyfile() + ".txt",
//				currModel.getM_keyfile(),currDeviceInfo.physicalDeviceId,currDeviceInfo.subDominId,srvInfo).execute() ;
//	}
//	public class SendTextAsyncTask extends DownloadIRFileAsyncTask {
//
//		/**
//		 * @param context          上下文
//		 * @param fileName         红外码库名称
//		 * @param fid              红外码库唯一ID
//		 * @param physicalDeviceId 智能插座物理ID
//		 * @param subDomainId      智能插座子域
//		 * @Description 异步发送红外码库给WIFI插座
//		 */
//		public SendTextAsyncTask(Context context, String fileName, int fid, String physicalDeviceId, long subDomainId,String SrvInfo) {
//			super(context, fileName, fid, physicalDeviceId, subDomainId, SrvInfo);
//		}
//
//		@Override
//		protected void onPostExecute(Integer integer) {
//			super.onPostExecute(integer);
//			dismiss();
//			if(sendCoderImp != null){
//				sendCoderImp.getResult(integer);
//			}
//		}
//	}
//	@Override
//	public void dismiss() {
//		super.dismiss();
//		pointLayoutUtils.stopAnio();
//	}
//	public interface SendCoderImp{
//		void getResult(int result) ;
//	}
//}
