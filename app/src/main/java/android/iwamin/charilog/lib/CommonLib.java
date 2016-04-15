package android.iwamin.charilog.lib;

import android.util.Log;

import java.security.MessageDigest;

public class CommonLib {
	public static int[] msecToHourMinSec(long msec) {
		int[] time = new int[3];

		time[0] = (int)(msec / 1000 / 3600);								// 時
		time[1] = (int)(msec / 1000 / 60 - (time[0] * 60));					// 分
		time[2] = (int)(msec / 1000 - (time[0] * 3600) - (time[1] * 60));	// 秒

		return time;
	}

	public static String encryptSHA256(String plainText) {
		String encrypted = null;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(plainText.getBytes());
			byte[] cipher = messageDigest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : cipher) {
				sb.append(String.format("%02x", (b & 0xff)));
			}
			encrypted = sb.toString();
		} catch (Exception e) {
			Log.e("SHA256", e.getMessage());
		}
		return encrypted;
	}
}
