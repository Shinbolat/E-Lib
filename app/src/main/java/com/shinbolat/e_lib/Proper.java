package com.shinbolat.e_lib;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;


public class Proper extends Activity {

    TextView Content;

    SQLiteDatabase db;

    String id;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proper_layout);

        Log.i("ID",">>"+id);

        try {
            dbHelper = new DBHelper(this);
            dbHelper.create_db();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = dbHelper.getWritableDatabase();

        Content = (TextView)findViewById(R.id.content_text);

        Cursor cursor;

        Intent intent = getIntent();

        id = new String(intent.getStringExtra("id"));

        String selection = " id = ?";
        String[] selectionArgs = {id};

        cursor = db.query("books_table", null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {

            Content.setText(
                    "Book: " + cursor.getString(cursor.getColumnIndex("books_name"))
                    +"\nAuthors: " + cursor.getString(cursor.getColumnIndex("books_authors"))
                    +"\nRooms: " + cursor.getString(cursor.getColumnIndex("books_rooms")));
        }

    }
}
