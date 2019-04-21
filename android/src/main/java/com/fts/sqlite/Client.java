package com.fts.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jacksonmalta on 13/04/19.
 */

public class Client extends SQLiteOpenHelper {
    SQLiteDatabase db = null;

    public Client(Context context, String databaseName, Integer databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        this.db = this.getWritableDatabase();
    }

    public void beginTransaction() {
        this.db.beginTransaction();
    }

    public void execSQL(String sql) {
        this.db.execSQL(sql);
    }

    public void commit() {
        this.db.setTransactionSuccessful();
        this.db.endTransaction();
    }

    public void rollback() {
        this.db.endTransaction();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
