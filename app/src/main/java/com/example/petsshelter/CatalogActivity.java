package com.example.petsshelter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsshelter.Data.PetContract;
import com.example.petsshelter.Data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CatalogActivity extends AppCompatActivity {

    TextView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();

        PetDbHelper mDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

    }


    @SuppressLint("SetTextI18n")
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
//        PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT,
                PetContract.PetEntry.COLUMN_PET_BREED};

        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, projection, null, null, null);

//        Cursor cursor = db.query(PetContract.PetEntry.TABLE_NAME, projection, null, null, null, null, null);

        displayView = (TextView) findViewById(R.id.text_view_pet);
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).

            displayView.setText("The pets table contains " + cursor.getCount() + " Pets.\n \n");

            displayView.append(PetContract.PetEntry._id + " | " + PetContract.PetEntry.COLUMN_PET_NAME + " | " + PetContract.PetEntry.COLUMN_PET_BREED + " | " + PetContract.PetEntry.COLUMN_PET_GENDER + " | " + PetContract.PetEntry.COLUMN_PET_WEIGHT + " \n ");

            int ID_column, Name_column, Breed_column, Weight_column, Gender_column;

            ID_column = cursor.getColumnIndex(PetContract.PetEntry._id);
            Name_column = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            Breed_column = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            Weight_column = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            Gender_column = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);


            //.moveToNext() is BOOLEAN thing
            while (cursor.moveToNext()) {
                int id = cursor.getInt(ID_column);
                String name = cursor.getString(Name_column);
                String breed = cursor.getString(Breed_column);
                int weight = cursor.getInt(Weight_column);
                int gender = cursor.getInt(Gender_column);

                displayView.append("\n" + id + " | " + name + " | " + breed + " | " + weight + " | " + gender);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        PetDbHelper petDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = petDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);

        Uri uri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
    }
}