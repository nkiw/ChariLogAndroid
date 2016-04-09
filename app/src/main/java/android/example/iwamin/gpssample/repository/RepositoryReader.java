package android.example.iwamin.gpssample.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.example.iwamin.gpssample.repository.SQLConstants.*;

public class RepositoryReader {
	DatabaseHelper databaseHelper;

	public void dumpRepository(Context context) {
		// for debug.

		// DB作成

		databaseHelper = new DatabaseHelper(context);

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

//			String tableName = TABLE_GPS_PREFIX + time;
//			Log.d("GPS_TABLE", tableName);
//
//			Cursor cursor_gps = db.query(tableName, columns_gps, null, null, null, null, COLUMN_GPS_TIME);
//			while (cursor_gps.moveToNext()) {
//				Log.d("GPS", "{" + cursor_gps.getLong(0) + ", " + cursor_gps.getDouble(1) + ". "
//						+ cursor_gps.getDouble(2) + ", " + cursor_gps.getDouble(3) + "}");
//
//			}
			db.close();

		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}
}
