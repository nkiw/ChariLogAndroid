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

	// GPSデータ記録テーブル
	public static final String TABLE_GPS_PREFIX = "gps_data_";

	public static final String COLUMN_GPS_TIME = "time";
	public static final String COLUMN_GPS_LATITUDE = "latitude";
	public static final String COLUMN_GPS_LONGITUDE = "longitude";
	public static final String COLUMN_GPS_ALTITUDE = "altitude";

	// 走行記録テーブル新規作成用 (Ver.2)
	public static final String sql_create_record_table = "create table " + TABLE_RECORD + " ("
			+ COLUMN_RECORD_ID + " integer primary key autoincrement, "
			+ COLUMN_RECORD_DATE_RAW + " integer not null, "
			+ COLUMN_RECORD_DATE + " text, "
			+ COLUMN_RECORD_START_TIME + "text, "
			+ COLUMN_RECORD_END_TIME + "text, "
			+ COLUMN_RECORD_TOTAL_TIME + "integer, "
			+ COLUMN_RECORD_DISTANCE + " integer, "
			+ COLUMN_RECORD_AVE_SPEED + " real, "
			+ COLUMN_RECORD_MAX_SPEED + " real)";
}
