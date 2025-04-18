package com.example.sqlite_rev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlDataBaseAdapter {
    SqlHelper helper;

    public sqlDataBaseAdapter(Context context) {
        // TODO Auto-generated constructor stub
        helper = new SqlHelper(context);
    }

    public long insertData(String name, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlHelper.NAME, name);
        contentValues.put(SqlHelper.PASSWORD, password);
        long id = db.insert(SqlHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getAllData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {SqlHelper.UID, SqlHelper.NAME, SqlHelper.PASSWORD };
        Cursor cursor = db.query(SqlHelper.TABLE_NAME , columns , null , null  ,null , null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int cursor_id = cursor.getInt(0);
            String name = cursor.getString(1);
            String passward = cursor.getString(2);
            buffer.append(cursor_id+" "+name+" "+passward+"\n");

        }
        return buffer.toString();
    }


//    public String getData(String name){
//        SQLiteDatabase db = helper.getWritableDatabase();
//        String[] columns = {SqlHelper.NAME, SqlHelper.PASSWORD };
//        Cursor cursor = db.query(SqlHelper.TABLE_NAME , columns , SqlHelper.NAME+"  = '"+name+"'" , null  ,null , null, null);
//        StringBuffer buffer = new StringBuffer();
//        while (cursor.moveToNext()){
//            int index1 = cursor.getColumnIndex(SqlHelper.NAME);
//            int index2 = cursor.getColumnIndex(SqlHelper.PASSWORD);
//
//            String personeName = cursor.getString(index1);
//            String passward = cursor.getString(index2);
//            buffer.append(personeName+" "+passward+"\n");
//
//        }
//        return buffer.toString();
//    }

    public String getData(String name, String password){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {SqlHelper.UID};
        String[] selectionArgs = {name , password };
        Cursor cursor = db.query(SqlHelper.TABLE_NAME , columns , SqlHelper.NAME + " =? AND " + SqlHelper.PASSWORD + " =?" , selectionArgs  ,null , null, null);
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(SqlHelper.UID);
            int personID = cursor.getInt(index0);
            buffer.append(personID + "\n");

        }
        return buffer.toString();
    }

    public int updateName(String oldName , String newName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlHelper.NAME, newName);
        String[] whreArgs  = {oldName};
        int count = db.update(SqlHelper.TABLE_NAME , contentValues , SqlHelper.NAME+" =? ", whreArgs);
        return count;

    }

    public int deleteName(String oldName ){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whreArgs  = {oldName};
        int count = db.delete(SqlHelper.TABLE_NAME , SqlHelper.NAME+ "=?" , whreArgs);
        return count;


    }


    static class SqlHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "vivzdatabase.db";
        private static final String TABLE_NAME = "VIVZTABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String PASSWORD = "Password";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                + " VARCHAR(255), " + PASSWORD + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
        private Context context;

        public SqlHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            Message.message(context, "constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // CREATE TABLE VIVZTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT,
            // Name
            // VARCHAR(255));
            try {
                db.execSQL(CREATE_TABLE);
                Message.message(context, "onCreate called");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                Message.message(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "onUpgrade called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                Message.message(context, "" + e);
            }
        }

    }
}



