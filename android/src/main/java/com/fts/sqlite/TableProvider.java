package com.fts.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by jacksonmalta on 13/04/19.
 */

public class TableProvider {

    Client client = null;
    SQLiteDatabase dbWrite = null;
    SQLiteDatabase dbRead = null;

    public TableProvider(Client client) {
        this.client = client;
        this.dbWrite = client.getWritableDatabase();
        this.dbRead = client.getReadableDatabase();
    }

    public void beginTransaction() {
        this.dbWrite.beginTransaction();
    }

    public long insert(String tableName, JSONObject obj) throws Exception {
        ContentValues contentValues = new ContentValues();

        Iterator<String> keysItr = obj.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            try {
                Object value = obj.get(key);
                if (value instanceof String) {
                    contentValues.put(key, (String) value);
                } else if (value instanceof Integer) {
                    contentValues.put(key, (Integer) value);
                } else if (value instanceof Double) {
                    contentValues.put(key, (Double) value);
                } else if (value instanceof Float) {
                    contentValues.put(key, (Float) value);
                } else if (value instanceof Boolean) {
                    contentValues.put(key, (Boolean) value);
                } else if (value instanceof Long) {
                    contentValues.put(key, (Long) value);
                }
            } catch (Exception ex) {
                throw ex;
            }
        }

        long newRowId = this.dbWrite.insert(tableName, null, contentValues);
        return newRowId;
    }

    public void criteria() {
        //this.dbRead.query();
    }

    public JSONObject query(String sql, String[] selectionArgs) throws Exception {
        Cursor cursor = this.dbRead.rawQuery(sql, selectionArgs);

        JSONObject resultJSON = new JSONObject();

        try {
            resultJSON.put("totalHits", cursor.getCount());
        } catch (Exception ex) {
            throw ex;
        }

        JSONArray jsonArray = new JSONArray();

        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            do {
                JSONObject hitJSON = new JSONObject();
                for (String column: columnNames) {
                    hitJSON.put(column, cursor.getString(cursor.getColumnIndex(column)));
                }
                jsonArray.put(hitJSON);
            } while (cursor.moveToNext());
        }

        resultJSON.put("hits", jsonArray);

        return resultJSON;
    }

    public Integer delete(String tableName, String whereQuery, String[] whereArgs) {
        return this.dbWrite.delete(tableName, whereQuery, whereArgs);
    }

    public Integer update(String tableName, JSONObject obj, String whereQuery, String[] whereArgs) throws Exception {
        ContentValues contentValues = new ContentValues();

        Iterator<String> keysItr = obj.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            try {
                Object value = obj.get(key);
                if (value instanceof String) {
                    contentValues.put(key, (String) value);
                } else if (value instanceof Integer) {
                    contentValues.put(key, (Integer) value);
                } else if (value instanceof Double) {
                    contentValues.put(key, (Double) value);
                } else if (value instanceof Float) {
                    contentValues.put(key, (Float) value);
                } else if (value instanceof Boolean) {
                    contentValues.put(key, (Boolean) value);
                } else if (value instanceof Long) {
                    contentValues.put(key, (Long) value);
                }
            } catch (Exception ex) {
                throw ex;
            }
        }

        return this.dbWrite.update(tableName, contentValues, whereQuery, whereArgs);
    }

    public void commit() {
        this.dbWrite.setTransactionSuccessful();
        this.dbWrite.endTransaction();
    }

    public void rollback() {
        this.dbWrite.endTransaction();
    }
}
