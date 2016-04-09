package android.example.iwamin.gpssample.repository;

public class SQLConstants {
	// データベース名
	public static final String DB_NAME = "cycling_log";

	// 走行記録テーブル
	public static final String TABLE_RECORD = "cycling_record";

	public static final String COLUMN_RECORD_ID = "_id";
	public static final String COLUMN_RECORD_DATE_RAW = "date_raw";
	public static final String COLUMN_RECORD_DATE = "date";
	public static final String COLUMN_RECORD_DISTANCE = "distance";
	public static final String COLUMN_RECORD_AVE_SPEED = "ave_speed";
	public static final String COLUMN_RECORD_MAX_SPEED = "max_speed";

	// GPSデータ記録テーブル
	public static final String TABLE_GPS_PREFIX = "gps_data_";

	public static final String COLUMN_GPS_TIME = "time";
	public static final String COLUMN_GPS_LATITUDE = "latitude";
	public static final String COLUMN_GPS_LONGITUDE = "longitude";
	public static final String COLUMN_GPS_ALTITUDE = "altitude";
}
