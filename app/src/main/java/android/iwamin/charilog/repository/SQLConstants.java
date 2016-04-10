package android.iwamin.charilog.repository;

public class SQLConstants {
	// データベース名
	public static final String DB_NAME = "cycling_log";

	// データベースバージョン
	public static final int DB_VERSION = 2;

	// 走行記録テーブル
	public static final String TABLE_RECORD = "cycling_record";

	public static final String COLUMN_RECORD_ID = "_id";
	public static final String COLUMN_RECORD_DATE_RAW = "date_raw";
	public static final String COLUMN_RECORD_DATE = "date";
	public static final String COLUMN_RECORD_START_TIME = "start_time";
	public static final String COLUMN_RECORD_END_TIME = "end_time";
	public static final String COLUMN_RECORD_TOTAL_TIME = "total_time";
	public static final String COLUMN_RECORD_DISTANCE = "distance";
	public static final String COLUMN_RECORD_AVE_SPEED = "ave_speed";
	public static final String COLUMN_RECORD_MAX_SPEED = "max_speed";

	// 走行記録テーブル新規作成用 (Ver.2)
	public static final String SQL_CREATE_RECORD_TABLE = "create table " + TABLE_RECORD + " ("
			+ COLUMN_RECORD_ID + " integer primary key autoincrement, "
			+ COLUMN_RECORD_DATE_RAW + " integer not null, "
			+ COLUMN_RECORD_DATE + " text, "
			+ COLUMN_RECORD_START_TIME + " text, "
			+ COLUMN_RECORD_END_TIME + " text, "
			+ COLUMN_RECORD_TOTAL_TIME + " integer, "
			+ COLUMN_RECORD_DISTANCE + " integer, "
			+ COLUMN_RECORD_AVE_SPEED + " real, "
			+ COLUMN_RECORD_MAX_SPEED + " real)";

	public enum RECORD_COLUMNS {
		RECORD_ID,
		RECORD_DATE_RAW,
		RECORD_DATE,
		RECORD_START_TIME,
		RECORD_END_TIME,
		RECORD_TOTAL_TIME,
		RECORD_DISTANCE,
		RECORD_AVE_SPEED,
		RECORD_MAX_SPEED,

		RECORD_COLUMN_NUM
	}

	public static final String[] RECORD_COLUMNS_STR = {
			COLUMN_RECORD_ID,
			COLUMN_RECORD_DATE_RAW,
			COLUMN_RECORD_DATE,
			COLUMN_RECORD_START_TIME,
			COLUMN_RECORD_END_TIME,
			COLUMN_RECORD_TOTAL_TIME,
			COLUMN_RECORD_DISTANCE,
			COLUMN_RECORD_AVE_SPEED,
			COLUMN_RECORD_MAX_SPEED
	};

	// GPSデータ記録テーブル
	public static final String TABLE_GPS = "gps_data";

	public static final String COLUMN_GPS_DATE_RAW = "date_raw";
	public static final String COLUMN_GPS_TIME = "time";
	public static final String COLUMN_GPS_LATITUDE = "latitude";
	public static final String COLUMN_GPS_LONGITUDE = "longitude";
	public static final String COLUMN_GPS_ALTITUDE = "altitude";

	// GPSデータテーブル作成
	public static final String SQL_CREATE_GPS_TABLE = "create table " + TABLE_GPS + " ("
			+ COLUMN_GPS_DATE_RAW + " integer not null, "
			+ COLUMN_GPS_TIME + " integer not null, "
			+ COLUMN_GPS_LATITUDE + " real not null, "
			+ COLUMN_GPS_LONGITUDE + " real not null, "
			+ COLUMN_GPS_ALTITUDE + " real not null)";

	public enum GPS_DATA_COLUMNS {
		GPS_DATA_DATE_RAW,
		GPS_DATA_TIME,
		GPS_DATA_LATITUDE,
		GPS_DATA_LONGITUDE,
		GPS_DATA_ALTITUDE,

		GPS_DATA_COLUMN_NUM
	}

	public static final String[] GPS_DATA_COLUMNS_STR = {
			COLUMN_GPS_DATE_RAW,
			COLUMN_GPS_TIME,
			COLUMN_GPS_LATITUDE,
			COLUMN_GPS_LONGITUDE,
			COLUMN_GPS_ALTITUDE
	};

}
