package chipsea.wifiplug.lib.util;

public class BytesUtil {
	public static String byteToBit(byte b) {
		return ""
				+ (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	public static int byte2int(byte b){
		return b & 0xFF;
	}

	public static byte[] shortToByteArray( short s) {
		byte[] bRet=new byte[2];
		bRet[0] = (byte) (s >> 8);
		bRet[1] = (byte) (s >> 0);

		return bRet;
	}

	/**
	 * 字节转换为字符串的转换工具函数
	 * */
	public static String bytesToPrintString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hv="0" + hv;
			}
			stringBuilder.append("0x" + hv);
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}

	/**
	 * 解析字节转换为整形
	 * */
	public static int bytesToInt(byte[] src){
		String tmp =BytesUtil.formatHexString(src);
		int sum = 0;
		try{
			sum = Integer.parseInt(tmp, 16);
		}catch(NumberFormatException e){

		}
		return sum;
	}

	/**
	 * 字节截取函数
	 * */
	public static byte[] subBytes(byte[] src, int begin, int count) {

		if( (count > 0) && (src.length >= (begin + count)) ){
			byte[] bs = new byte[count];
			for (int i = begin; i < begin + count; i++){
				bs[i - begin] = src[i];
			}
			return bs;
		}else{
			return null;
		}
	}

	/**
	 * 字节转换为字符串的转换工具函数
	 * */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}

	private static String formatHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
