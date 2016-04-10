package android.iwamin.charilog.lib;

public class CommonLib {
	public static int[] msecToHourMinSec(long msec) {
		int[] time = new int[3];

		time[0] = (int)(msec / 1000 / 3600);								// 時
		time[1] = (int)(msec / 1000 / 60 - (time[0] * 60));					// 分
		time[2] = (int)(msec / 1000 - (time[0] * 3600) - (time[1] * 60));	// 秒

		return time;
	}
}
