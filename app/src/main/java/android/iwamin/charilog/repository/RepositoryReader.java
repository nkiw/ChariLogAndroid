package android.iwamin.charilog.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.entity.GPSData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.iwamin.charilog.repository.SQLConstants.*;
import static android.iwamin.charilog.repository.SQLConstants.RECORD_COLUMNS.*;
import static android.iwamin.charilog.repository.SQLConstants.GPS_DATA_COLUMNS.*;

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
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		try {
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
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public List<GPSData> getGPSDataList (Context context, Long condition) {
		List<GPSData> list = new ArrayList<>();

		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		try {
			Cursor cursor;
			if (condition == null) {
				cursor = db.query(TABLE_GPS, GPS_DATA_COLUMNS_STR, null, null, null, null, COLUMN_GPS_DATE_RAW);
			} else {
				String selection = COLUMN_GPS_DATE_RAW + " = ?";
				String[] args = new String[]{condition.toString()};
				cursor = db.query(TABLE_GPS, GPS_DATA_COLUMNS_STR, selection, args, null, null, COLUMN_GPS_DATE_RAW);
			}
			while (cursor.moveToNext()) {
				GPSData data = new GPSData(
						cursor.getLong(GPS_DATA_DATE_RAW.ordinal()),
						cursor.getLong(GPS_DATA_TIME.ordinal()),
						cursor.getDouble(GPS_DATA_LATITUDE.ordinal()),
						cursor.getDouble(GPS_DATA_LONGITUDE.ordinal()),
						cursor.getDouble(GPS_DATA_ALTITUDE.ordinal())
				);
				list.add(data);
			}
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}
}
