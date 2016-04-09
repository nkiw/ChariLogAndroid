package android.example.iwamin.gpssample.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.iwamin.gpssample.monitor.CyclingMonitor;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;

public class LocationRepository {
	private static final String DB_NAME = "cycling_log";
	private static final String TABLE_RECORD = "cycling_record";
	private static final String TABLE_GPS_PREFIX = "gps_data_";

	private static final String COLUMN_RECORD_ID = "_id";
	private static final String COLUMN_RECORD_DATE_RAW = "date_raw";
	private static final String COLUMN_RECORD_DATE = "date";
	private static final String COLUMN_RECORD_DISTANCE = "distance";
	private static final String COLUMN_RECORD_AVE_SPEED = "ave_speed";
	private static final String COLUMN_RECORD_MAX_SPEED = "max_speed";


	private static final String COLUMN_GPS_TIME = "time";
	private static final String COLUMN_GPS_LATITUDE = "latitude";
	private static final String COLUMN_GPS_LONGITUDE = "longitude";
	private static final String COLUMN_GPS_ALTITUDE = "altitude";


	private SQLiteOpenHelper databaseHelper;
	private SQLiteDatabase database;

	private long startTime;

	public void readyLogging(Context context) {
		// 走行モニターを初期化
		CyclingMonitor.getInstance().reset();

		// 開始時刻を記録
		startTime = System.currentTimeMillis();

		// DB作成
		databaseHelper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
			@Override
			public void onCreate(SQLiteDatabase db) {
				// None
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// None

			}
		};

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
		// 走行記録テーブル作成
		String sql = "create table " + TABLE_RECORD + " ("
				+ COLUMN_RECORD_ID + " integer primary key autoincrement, "
				+ COLUMN_RECORD_DATE_RAW + " integer not null, "
				+ COLUMN_RECORD_DATE + " text, "
				+ COLUMN_RECORD_DISTANCE + " integer, "
				+ COLUMN_RECORD_AVE_SPEED + " real, "
				+ COLUMN_RECORD_MAX_SPEED + " real)";
		Log.v("SQL", sql);
		try {
			database.execSQL(sql);
		} catch (Exception e) {
			Log.v("SQL", e.getMessage());
		}

		// 走行記録を保存
		CyclingMonitor.CyclingInfo info = CyclingMonitor.getInstance().getCyclingInfo();
		String date = new SimpleDateFormat("yyyy/MM/dd").format(startTime);
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

		dumpRepository();
	}

	public void dumpRepository() {
		// for debug.

		SQLiteDatabase db;
		String[] columns = {
				COLUMN_RECORD_ID,
				COLUMN_RECORD_DATE_RAW,
				COLUMN_RECORD_DATE,
				COLUMN_RECORD_DISTANCE,
				COLUMN_RECORD_AVE_SPEED,
				COLUMN_RECORD_MAX_SPEED
		};

		String[] columns_gps = {
				COLUMN_GPS_TIME,
				COLUMN_GPS_LATITUDE,
				COLUMN_GPS_LONGITUDE,
				COLUMN_GPS_ALTITUDE
		};

		try {
			db = databaseHelper.getReadableDatabase();

			long time = 0;

			Cursor cursor = db.query(TABLE_RECORD, columns, null, null, null, null, COLUMN_RECORD_ID);
			while (cursor.moveToNext()) {
				Log.d("RECORD", "{" + cursor.getInt(0) + ", " + cursor.getLong(1) + ". "
						+ cursor.getString(2) + ", " + cursor.getInt(3) + ", "
						+ cursor.getDouble(4) + ", " + cursor.getDouble(5) + "}");
				time = cursor.getLong(1);
			}

			String tableName = TABLE_GPS_PREFIX + time;
			Log.d("GPS_TABLE", tableName);

			Cursor cursor_gps = db.query(tableName, columns_gps, null, null, null, null, COLUMN_GPS_TIME);
			while (cursor_gps.moveToNext()) {
				Log.d("GPS", "{" + cursor_gps.getLong(0) + ", " + cursor_gps.getDouble(1) + ". "
						+ cursor_gps.getDouble(2) + ", " + cursor_gps.getDouble(3) + "}");

			}
			db.close();

		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}
}
