package com.ewansr.www.koobenapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ewansr.www.koobenapp.SQLiteDBConfig;

/**
 * Created by Saulo on 18/07/2016.
 */
public class SQLiteDbHelper  extends SQLiteOpenHelper {
    public SQLiteDbHelper (Context context/*, String SessionID, String UserName, String Mail, String User, String Img, String UserType*/){
         super(context, SQLiteDBConfig.DATABASE_NAME, null, SQLiteDBConfig.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la base de datos
        db.execSQL(SQLiteDBDataSource.CREATE_SCHEMA_SCRIPT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la base de datos
        db.execSQL("DROP TABLE IF EXISTS user_profile");

        //Se crea la nueva versión de la tabla
        db.execSQL(SQLiteDBDataSource.CREATE_SCHEMA_SCRIPT);
    }


}