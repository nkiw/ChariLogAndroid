package android.iwamin.charilog.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.iwamin.charilog.entity.CyclingRecord;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.iwamin.charilog.repository.SQLConstants.COLUMN_GPS_ALTITUDE;
import static android.iwamin.charilog.repository.SQLConstants.COLUMN_GPS_LATITUDE;
import static android.iwamin.charilog.repository.SQLConstants.COLUMN_GPS_LONGITUDE;
import static android.iwamin.charilog.repository.SQLConstants.COLUMN_GPS_TIME;
import static android.iwamin.charilog.repository.SQLConstants.COLUMN_RECORD_ID;
import static android.iwamin.charilog.repository.SQLConstants.RECORD_COLUMNS_STR;
import static android.iwamin.charilog.repository.SQLConstants.TABLE_RECORD;

import static android.iwamin.charilog.repository.SQLConstants.RECORD_COLUMNS.*;

public class RepositoryReader {

	public void dumpRepository(Context context) {
		// for debug.

		// DB作成

		DatabaseHelper databaseHelper = new DatabaseHelper(context);

		SQLiteDatabase db;

		String[] columns_gps = {
				COLUMN_GPS_TIME,
				COLUMN_GPS_LATITUDE,
				COLUMN_GPS_LONGITUDE,
				COLUMN_GPS_ALTITUDE
		};

		try {
			db = databaseHelper.getReadableDatabase();

			long time = 0;

			Cursor cursor = db.query(TABLE_RECORD, RECORD_COLUMNS_STR, null, null, null, null, COLUMN_RECORD_ID);
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

	public List<CyclingRecord> getCyclingRecordList(Context context) {
		List<CyclingRecord> list = new ArrayList<>();

		DatabaseHelper databaseHelper = new DatabaseHelper(context);

		try {
			SQLiteDatabase db = databaseHelper.getReadableDatabase();

			Cursor cursor = db.query(TABLE_RECORD, RECORD_COLUMNS_STR, null, null, null, null, COLUMN_RECORD_ID);
			while (cursor.moveToNext()) {
				CyclingRecord record = new CyclingRecord(
						cursor.getInt(RECORD_ID.ordinal()),
						cursor.getLong(RECORD_DATE_RAW.ordinal()),
						cursor.getString(RECORD_DATE.ordinal()),
						cursor.getString(RECORD_START_TIME.ordinal()),
						cursor.getString(RECORD_END_TIME.ordinal()),
						cursor.getInt(RECORD_TOTAL_TIME.ordinal()),
						cursor.getInt(RECORD_DISTANCE.ordinal()),
						cursor.getDouble(RECORD_AVE_SPEED.ordinal()),
						cursor.getDouble(RECORD_MAX_SPEED.ordinal())
				);
				list.add(record);
			}
			db.close();
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}

		return list;
	}
}
