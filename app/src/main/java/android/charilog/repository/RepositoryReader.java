package android.charilog.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.charilog.repository.entity.CyclingRecord;
import android.charilog.repository.entity.GPSData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.charilog.repository.SQLConstants.*;
import static android.charilog.repository.SQLConstants.RECORD_COLUMNS.*;
import static android.charilog.repository.SQLConstants.GPS_DATA_COLUMNS.*;

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
						cursor.getDouble(RECORD_MAX_SPEED.ordinal()),
						cursor.getInt(RECORD_UPLOADED.ordinal())
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

	public boolean deleteCyclingRecord(Context context, int id) {
		final String[] columns = {COLUMN_RECORD_ID, COLUMN_RECORD_DATE_RAW};
		String[] selectionArgs = {String.valueOf(id)};

		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		boolean ret = false;

		try {
			Cursor cursorRecord = db.query(TABLE_RECORD, columns, COLUMN_RECORD_ID + " = ?", selectionArgs, null, null, null);

			if (cursorRecord.getCount() != 0) {
				while (cursorRecord.moveToNext()) {
					String[] whereArgs = {String.valueOf(cursorRecord.getLong(1))};

					db.delete(TABLE_RECORD, COLUMN_RECORD_ID + " = ?", selectionArgs);
					int num = db.delete(TABLE_GPS, COLUMN_GPS_DATE_RAW + " = ?", whereArgs);
					Log.v("GPS_DEL", String.valueOf(num));

					ret = true;
				}
			}
		} catch(Exception e) {
			Log.e("SQL", e.getMessage());
		} finally {
			db.close();
		}

		return ret;
	}

	public void setUploaded(Context context, int id) {
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			String[] whereArgs = {String.valueOf(id)};
			values.put(COLUMN_RECORD_UPLOADED, 1);
			int num = db.update(TABLE_RECORD, values, (COLUMN_RECORD_ID + " = ?"), whereArgs);
			Log.v("SET_UPLOADED", String.valueOf(num));
		} catch(Exception e) {
			Log.e("SQL", e.getMessage());
		} finally {
			db.close();
		}
	}

	public List<CyclingRecord> getNotUploadedRecord(Context context) {
		List<CyclingRecord> list = new ArrayList<>();

		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		try {
			String[] selectionArgs = {"0"};
			Cursor cursor = db.query(TABLE_RECORD, RECORD_COLUMNS_STR,
					(COLUMN_RECORD_UPLOADED + " = ?"), selectionArgs, null, null, COLUMN_RECORD_ID);
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
						cursor.getDouble(RECORD_MAX_SPEED.ordinal()),
						cursor.getInt(RECORD_UPLOADED.ordinal())
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
}
