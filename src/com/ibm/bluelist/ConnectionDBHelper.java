package com.ibm.bluelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ondrejplevka on 29/11/15.
 */
public class ConnectionDBHelper {

    private static final String TAG = ConnectionDBHelper.class.getSimpleName();

    // database configuration
    // if you want the onUpgrade to run then change the database_version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "connections.db";

    // table configuration
    private static final String TABLE_NAME = "connnections";         // Table name
    private static final String PERSON_TABLE_COLUMN_ID = "_id";     // a column named "_id" is required for cursor
    private static final String CONN_ROUTE_COLUMN = "route";
    private static final String CONN_KEY_COLUMN = "key";
    private static final String CONN_PASSWORD_COLUMN = "password";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    // this is a wrapper class. that means, from outside world, anyone will communicate with PersonDatabaseHelper,
    // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations
    public ConnectionDBHelper(Context aContext) {

        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertData (String aConnRoute, String aConnKey, String aConnPassword) {

        // we are using ContentValues to avoid sql format errors

        ContentValues contentValues = new ContentValues();

        contentValues.put(CONN_ROUTE_COLUMN, aConnRoute);
        contentValues.put(CONN_KEY_COLUMN, aConnKey);
        contentValues.put(CONN_PASSWORD_COLUMN, aConnPassword);

        database.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData () {

        String buildSQL = "SELECT * FROM " + TABLE_NAME;

        Log.d(TAG, "getAllData SQL: " + buildSQL);

        return database.rawQuery(buildSQL, null);
    }

    public void deleteItemById(int id){
        openHelper.deleteItemById(id);
    }



    // this DatabaseOpenHelper class will actually be used to perform database related operation

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // Create your tables here

            String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + PERSON_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    CONN_ROUTE_COLUMN + " TEXT, " + CONN_KEY_COLUMN + " TEXT , " + CONN_PASSWORD_COLUMN + " TEXT )";

            Log.d(TAG, "onCreate SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);
        }

        public void deleteItemById(int id){

            SQLiteDatabase db = this.getWritableDatabase();

            try {

                db.delete(TABLE_NAME, "_id = ?",
                        new String[] { String.valueOf(id)});

            } catch (Exception e)
            {
                Log.e("insert",e.getMessage());
            }



            db.close();

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // Database schema upgrade code goes here

            String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

            Log.d(TAG, "onUpgrade SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);       // drop previous table

            onCreate(sqLiteDatabase);               // create the table from the beginning
        }
    }


}
