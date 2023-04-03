package com.example.f89497_quyet_nguyen_save_password.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String website, String username,String password) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.WEBSITE, website);
        contentValue.put(DatabaseHelper.USERNAME, username);
        contentValue.put(DatabaseHelper.PASSWORD, password);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.WEBSITE, DatabaseHelper.USERNAME ,DatabaseHelper.PASSWORD};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String website, String username,String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.WEBSITE, website);
        contentValues.put(DatabaseHelper.USERNAME, username);
        contentValues.put(DatabaseHelper.PASSWORD, password);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
