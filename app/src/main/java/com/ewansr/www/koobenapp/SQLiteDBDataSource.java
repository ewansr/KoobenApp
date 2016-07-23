package com.ewansr.www.koobenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.renderscript.Script;

/**
 * Created by Saulo on 18/07/2016.
 */
public class SQLiteDBDataSource {

    //Metadata de la base de datos
    public static final String TABLE_NAME = "user_profile";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    //Campos de la tabla user_profile
    public static class ColumnQuotes{
        public static final String ID_QUOTES    = BaseColumns._ID;
        public static final String SESSIONID    = "SessionID";
        public static final String PROFILE_NAME = "Name";
        public static final String PROFILE_MAIL = "Mail";
        public static final String PROFILE_USER = "User";
        public static final String PROFILE_IMG  = "Img";
        public static final String PROFILE_TYPE = "Type";
    }

    //Script de Creación de la tabla user_profile
    public static final String CREATE_SCHEMA_SCRIPT =
            "create table "+ TABLE_NAME       + "(" +
                    ColumnQuotes.ID_QUOTES    + " " + INT_TYPE    +" primary key autoincrement," +
                    ColumnQuotes.PROFILE_MAIL + " " + STRING_TYPE +" not null," +
                    ColumnQuotes.PROFILE_USER + " " + STRING_TYPE +" not null," +
                    ColumnQuotes.PROFILE_IMG  + " " + STRING_TYPE +" not null," +
                    ColumnQuotes.PROFILE_TYPE + " " + STRING_TYPE +" not null," +
                    ColumnQuotes.SESSIONID    + " " + STRING_TYPE +" not null," +
                    ColumnQuotes.PROFILE_NAME + " " + STRING_TYPE +" not null)";


    private SQLiteDbHelper openHelper;
    private SQLiteDatabase database;

    public SQLiteDBDataSource(Context context) {
        //Creando una instancia hacia la base de datos
        openHelper = new SQLiteDbHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public static void insertDataUser(Context context, String SessionID, String UserName, String Mail, String User, String Img, String UserType){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_IMG, Img);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_MAIL,Mail);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_NAME, UserName);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_TYPE, UserType);
        values.put(SQLiteDBDataSource.ColumnQuotes.PROFILE_USER, User);
        values.put(SQLiteDBDataSource.ColumnQuotes.SESSIONID, SessionID);

        SQLiteDbHelper dbh = new SQLiteDbHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    public static void insertDataUser(Context context, ContentValues values){
        SQLiteDbHelper dbh = new SQLiteDbHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.insert(TABLE_NAME, null, values);
    }

    public static long getProfilesCount(Context context) {
        SQLiteDbHelper dbh = new SQLiteDbHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }
}