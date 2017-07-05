package com.shinbolat.e_lib;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;


public class UpdateData extends Activity {

    final String LOG_TAG = "Add Data";

    EditText BooksName,BooksAuthors, BooksRooms;
    Button Back, Add;

    TextView QueriesCondition;

    SQLiteDatabase db;

    String id;

    DBHelper dbHelper;

    public void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.update_data_layout);

        try {
            dbHelper = new DBHelper(this);
            dbHelper.create_db();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = dbHelper.getWritableDatabase();

        BooksName = (EditText)findViewById(R.id.insert_books_name);
        BooksAuthors = (EditText)findViewById(R.id.insert_books_authors);
        BooksRooms = (EditText)findViewById(R.id.insert_books_rooms);

        QueriesCondition = (TextView)findViewById(R.id.queries_condition);

        Back = (Button)findViewById(R.id.back);
        Add = (Button)findViewById(R.id.add);

        Cursor cursor;

        Intent intent = getIntent();

        id = new String(intent.getStringExtra("id"));

        String selection = " id = ?";
        String[] selectionArgs = {id};

        cursor = db.query("books_table", null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {

            BooksName.setText(cursor.getString(cursor.getColumnIndex("books_name")));
            BooksAuthors.setText(cursor.getString(cursor.getColumnIndex("books_authors")));
            BooksRooms.setText(cursor.getString(cursor.getColumnIndex("books_rooms")));

        }

        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ContentValues contentValues = new ContentValues();


                String name = BooksName.getText().toString().trim();
                String authors = BooksAuthors.getText().toString().trim();
                String rooms = BooksRooms.getText().toString().trim();

                contentValues.put("books_name",name);
                contentValues.put("books_authors",authors);
                contentValues.put("books_rooms",rooms);



                int updCount = db.update("books_table", contentValues, "id = ?",
                        new String[] {id});

                Log.i(LOG_TAG, "row updated id =" + id + " >> " + name + " " + authors + " " + rooms);

                QueriesCondition.setText("Success: updated id " + id);

                BooksName.setText("");
                BooksAuthors.setText("");
                BooksRooms.setText("");

                finish();

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    public void onStop(){

        super.onStop();
        db.close();
        Log.i(LOG_TAG,"db closed");
    }

    public void onPause(){

        super.onPause();

    }



}
