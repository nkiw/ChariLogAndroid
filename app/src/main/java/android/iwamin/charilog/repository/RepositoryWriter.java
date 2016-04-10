package android.iwamin.charilog.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.iwamin.charilog.monitor.CyclingMonitor;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;

import static android.iwamin.charilog.repository.SQLConstants.*;

public class RepositoryWriter {
	private SQLiteOpenHelper databaseHelper;
	private SQLiteDatabase database;

	private long startTime;

	public void readyLogging(Context context) {
		// 走行モニターを初期化
		CyclingMonitor.getInstance().reset();

		// 開始時刻を記録
		startTime = System.currentTimeMillis();

		// DB作成
		databaseHelper = new DatabaseHelper(context);

		// データベースオブジェクト取得
		database = databaseHelper.getWritableDatabase();

		// GPSデータテーブル作成
		String sql = "create table " + (TABLE_GPS_PREFIX + startTime) + " ("
				+ COLUMN_GPS_TIME + " integer not null, "
				+ COLUMN_GPS_LATITUDE + " real not null, "
				+ COLUMN_GPS_LONGITUDE + " real not null, "
				+ COLUMN_GPS_ALTITUDE + " real not null)";
		Log.v("SQL", sql);
		try {
			database.execSQL(sql);
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}

	public void reportLocationChange(Location location) {
		CyclingMonitor.getInstance().reportLocationChange(location);

		// 位置情報をデータベースに記録
		try {
			database.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(COLUMN_GPS_TIME, location.getTime());
			values.put(COLUMN_GPS_LATITUDE, location.getLatitude());
			values.put(COLUMN_GPS_LONGITUDE, location.getLongitude());
			values.put(COLUMN_GPS_ALTITUDE, location.getAltitude());

			database.insert((TABLE_GPS_PREFIX + startTime), null, values);
			database.setTransactionSuccessful();

			database.endTransaction();
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}

	public void stopLogging() {
		// 走行記録を保存
		CyclingMonitor.CyclingInfo info = CyclingMonitor.getInstance().getCyclingInfo();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
		Log.v("DATE", date);

		try {
			database.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(COLUMN_RECORD_DATE_RAW, startTime);
			values.put(COLUMN_RECORD_DATE, date);
			values.put(COLUMN_RECORD_DISTANCE, info.getTotalDistance());
			values.put(COLUMN_RECORD_AVE_SPEED, info.getAverageSpeed());
			values.put(COLUMN_RECORD_MAX_SPEED, info.getMaximumSpeed());

			database.insert(TABLE_RECORD, null, values);
			database.setTransactionSuccessful();

			database.endTransaction();

		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}

		// データベースオブジェクトクローズ
		database.close();
	}
}
