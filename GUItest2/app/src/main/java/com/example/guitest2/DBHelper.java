package com.example.guitest2;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Name of database
    private static final String DATABASE_NAME = "ItemDB.db";

    // Column containing unique ID number
    private static final String CONTACTS_COLUMN_ID = "id";

    // Columns containing details of contacts
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DESCRIPTION = "description";
    public static final String CONTACTS_COLUMN_PRICE = "price";
    public static final String CONTACTS_COLUMN_LOWRESOLUTIONFILE = "lowResolutionFile";
    public static final String CONTACTS_COLUMN_HIGHRESOLUTIONFILE = "highResolutionFile";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contacts " +
                "(id integer primary key, name text, description text, price text,lowResolutionFile text,highResolutionFile text)");
    }

    //if exists no create table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    //insert data
    public boolean insertContact(String name, String description, String price, String lowResolutionFile, String highResolutionFile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",getCount()+1);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("lowResolutionFile", lowResolutionFile);
        contentValues.put("highResolutionFile", highResolutionFile);
        db.insert("contacts", null, contentValues);
        return true;
    }

    //query
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from contacts where id="
                + id, null);
        return c;
    }

    //count number of data
    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select COUNT(*) from contacts" , null);
        c.moveToFirst();
        return c.getInt(0);
    }

    //updata data useless now
    public boolean updateContact(Integer id, String name, String description, String price, String lowResolutionFile, String highResolutionFile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("lowResolutionFile", lowResolutionFile);
        contentValues.put("highResolutionFile", highResolutionFile);
        db.update("contacts", contentValues, "id = ? ",
                new String[] { Integer.toString(id) } );
        return true;
    }
}