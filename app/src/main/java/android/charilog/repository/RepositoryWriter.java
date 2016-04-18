package android.charilog.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.charilog.monitor.CyclingMonitor;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;

import static android.charilog.repository.SQLConstants.*;

public class RepositoryWriter {
	private SQLiteOpenHelper databaseHelper;
	private SQLiteDatabase database;

	private long startTime;

	public void readyLogging(Context context) {
		// 開始時刻を記録
		startTime = System.currentTimeMillis();

		// DB作成
		databaseHelper = new DatabaseHelper(context);

		// データベースオブジェクト取得
		database = databaseHelper.getWritableDatabase();

	}

	public void reportLocationChange(Location location) {
		// 位置情報をデータベースに記録
		try {
			database.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(COLUMN_GPS_DATE_RAW, startTime);
			values.put(COLUMN_GPS_TIME, location.getTime());
			values.put(COLUMN_GPS_LATITUDE, location.getLatitude());
			values.put(COLUMN_GPS_LONGITUDE, location.getLongitude());
			values.put(COLUMN_GPS_ALTITUDE, location.getAltitude());

			database.insert(TABLE_GPS, null, values);
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}

	public void stopLogging() {
		// 走行記録を保存
		CyclingMonitor.CyclingInfo info = CyclingMonitor.getInstance().getInfo();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
		String time = new SimpleDateFormat("HH:mm:ss").format(startTime);
		String endTime = new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis());
		Log.v("DATE", date);

		try {
			database.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(COLUMN_RECORD_DATE_RAW, startTime);
			values.put(COLUMN_RECORD_DATE, date);
			values.put(COLUMN_RECORD_START_TIME, time);
			values.put(COLUMN_RECORD_END_TIME, endTime);
			values.put(COLUMN_RECORD_TOTAL_TIME, info.getTotalTime());
			values.put(COLUMN_RECORD_DISTANCE, info.getTotalDistance());
			values.put(COLUMN_RECORD_AVE_SPEED, info.getAverageSpeed());
			values.put(COLUMN_RECORD_MAX_SPEED, info.getMaximumSpeed());

			database.insert(TABLE_RECORD, null, values);
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		} finally {
			database.close();
		}
	}
}
