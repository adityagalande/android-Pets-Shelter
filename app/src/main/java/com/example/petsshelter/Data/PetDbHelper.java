package com.example.petsshelter.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petsshelter.Data.PetContract.PetEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;


    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //OnCreate Method call when DB is created first time
    public void onCreate(SQLiteDatabase db) {
        //Write CREATE TABLE statement Here by using PetContract.PetEntry CONSTANT'S
        String SQL_CREATE_PETS_TABLE = " CREATE TABLE " + PetEntry.TABLE_NAME + "(" + PetEntry._id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " + PetEntry.COLUMN_PET_BREED + " TEXT, " + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 ); ";
        //It execute upper SQl command
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
