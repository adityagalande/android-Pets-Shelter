package com.example.petsshelter.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PetProvider extends ContentProvider {

    private static final String LOG_TAG = PetProvider.class.getSimpleName();
    //Create and Initialize PetDBHelper Database helper object to Gain access
    private PetDbHelper mPetDbHelper;

    private static final int PETS = 100;
    private static final int PETS_ID = 101;


    private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {


        sURI_MATCHER.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);

        sURI_MATCHER.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PETS_ID);
    }


    @Override
    public boolean onCreate() {
        mPetDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();

        Cursor cursor;


        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case PETS:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PETS_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown Uri " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sURI_MATCHER.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PETS_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sURI_MATCHER.match(uri);

        switch (match) {
            case PETS:
                insertPet(uri, values);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown Uri " + uri);
        }
        return null;
    }

    private Uri insertPet(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);

        if (name == null) {
            throw new IllegalArgumentException("Pet requires name!");
        }

        Integer Gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);

        if (Gender == null || !PetContract.PetEntry.isValidGender(Gender)) {
            throw new IllegalArgumentException("Pet requires valid weight!");
        }

        Integer Weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);

        if (Weight != null && Weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight!");
        }

        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        long id = db.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert data!" + uri);
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        final int match = sURI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            case PETS_ID:
                selection = PetContract.PetEntry._ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delection is not supported for this Uri "+ uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sURI_MATCHER.match(uri);

        switch (match) {
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PETS_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);

            if (name == null) {
                throw new IllegalArgumentException("Pet require name!");
            }
        }

        if (values.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);

            if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender!");
            }
        }

        if (values.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);

            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet require valid weight!");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        return db.update(PetContract.PetEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
