package com.company.comanda.brian.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressOpenHelper extends SQLiteOpenHelper{

    public AddressOpenHelper(Context context, String name,
            CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "addresses";
    
    private static final String ADDRESS_TABLE_NAME = "addresses";
    
    private static final String COLUMN_NICE_STRING = "NICE_STRING";
    private static final String COLUMN_ADDITIONAL_DATA = "ADDITIONAL_DATA";
    private static final String COLUMN_LATITUDE = "LATITUDE";
    private static final String COLUMN_LONGITUDE = "LONGITUDE";
    private static final String ADDRESS_TABLE_CREATE = 
            "CREATE TABLE " + ADDRESS_TABLE_NAME + " (" +
                    COLUMN_NICE_STRING + " TEXT, " +
                    COLUMN_ADDITIONAL_DATA + " TEXT, " +
                    COLUMN_LATITUDE + " DOUBLE, " +
                    COLUMN_LONGITUDE + " DOUBLE);";
                    
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ADDRESS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException();
        
    }
}
