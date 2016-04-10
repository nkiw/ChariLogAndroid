package android.iwamin.charilog.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.iwamin.charilog.repository.SQLConstants.DB_NAME;
import static android.iwamin.charilog.repository.SQLConstants.DB_VERSION;
import static android.iwamin.charilog.repository.SQLConstants.SQL_CREATE_RECORD_TABLE;
import static android.iwamin.charilog.repository.SQLConstants.SQL_CREATE_GPS_TABLE;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 走行記録テーブル作成
		Log.v("SQL", SQL_CREATE_RECORD_TABLE);
		Log.v("SQL", SQL_CREATE_GPS_TABLE);
		try {
			db.execSQL(SQL_CREATE_RECORD_TABLE);
			db.execSQL(SQL_CREATE_GPS_TABLE);
		} catch (Exception e) {
			Log.e("SQL", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Ver.1 -> Ver.2 (サンプルとして残す)
//		if ((oldVersion == 1) && (newVersion == 2)) {
//			final String backupTableName = TABLE_RECORD + "_bak";
//			final String sql_rename = "alter table " + TABLE_RECORD + " rename to " + backupTableName;
//			final String sql_drop = "drop table " + backupTableName;
//			final String[] columns = {
//					COLUMN_RECORD_DATE_RAW,
//					COLUMN_RECORD_DATE,
//					COLUMN_RECORD_DISTANCE,
//					COLUMN_RECORD_AVE_SPEED,
//					COLUMN_RECORD_MAX_SPEED
//			};
//
//			try {
//				db.execSQL(sql_rename);
//				Log.v("SQL_ALTER", sql_rename);
//				db.execSQL(sql_create_record_table);
//				Log.v("SQL_ALTER", sql_create_record_table);
//
//				Cursor cursor = db.query(backupTableName, columns, null, null, null, null, COLUMN_RECORD_DATE_RAW);
//
//				while (cursor.moveToNext()) {
//					db.beginTransaction();
//
//					ContentValues values = new ContentValues();
//					values.put(COLUMN_RECORD_DATE_RAW, cursor.getLong(0));
//					values.put(COLUMN_RECORD_DATE, cursor.getString(1));
//					values.put(COLUMN_RECORD_DISTANCE, cursor.getInt(2));
//					values.put(COLUMN_RECORD_AVE_SPEED, cursor.getInt(3));
//					values.put(COLUMN_RECORD_MAX_SPEED, cursor.getInt(4));
//
//					db.insert(TABLE_RECORD, null, values);
//
//					db.setTransactionSuccessful();
//					db.endTransaction();
//
//				}
//
//				db.execSQL(sql_drop);
//				Log.v("SQL_ALTER", sql_drop);
//			} catch (Exception e) {
//				Log.e("SQL", e.getMessage());
//			}
//		}
	}
}
