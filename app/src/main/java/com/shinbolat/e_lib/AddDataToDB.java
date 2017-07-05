package com.shinbolat.e_lib;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;


public class AddDataToDB extends Activity {

    final String LOG_TAG = "Add Data";

    EditText BooksName,BooksAuthors, BooksRooms;
    Button Back, Add;

    TextView QueriesCondition;

    SQLiteDatabase db;

    DBHelper dbHelper;

    public void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.add_data_to_db);

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

                long rowID = db.insert("books_table", null, contentValues);

                Log.i(LOG_TAG,"row inserted" + rowID + " >> " + name + " " + authors + " " + rooms);

                QueriesCondition.setText("Success: id " + rowID);

                BooksName.setText("");
                BooksAuthors.setText("");
                BooksRooms.setText("");

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
