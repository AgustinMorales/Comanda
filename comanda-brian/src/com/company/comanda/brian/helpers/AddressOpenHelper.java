package com.company.comanda.brian.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressOpenHelper extends SQLiteOpenHelper{

    public AddressOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "addresses";
    
    public static final String ADDRESS_TABLE_NAME = "addresses";
    
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NICE_STRING = "NICE_STRING";
    public static final String COLUMN_ADDITIONAL_DATA = "ADDITIONAL_DATA";
    public static final String COLUMN_CITY = "CITY";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    private static final String ADDRESS_TABLE_CREATE = 
            "CREATE TABLE " + ADDRESS_TABLE_NAME + " (" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NICE_STRING + " TEXT, " +
                    COLUMN_ADDITIONAL_DATA + " TEXT, " +
                    COLUMN_CITY + " TEXT, " + 
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL);";
                    
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ADDRESS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException();
        
    }
}
