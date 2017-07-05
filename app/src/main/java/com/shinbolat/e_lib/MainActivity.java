package com.shinbolat.e_lib;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    final String LOG_TAG = "SEARCH_LOG";

    EditText insertedSymbols;
    CheckBox booksCheckBox;
    CheckBox authorsCheckBox;
    Button searchButton;

    TextView result;

    SQLiteDatabase db;

    DBHelper dbHelper;

    ListView listView;

    ArrayList<String> arrayList;
    ArrayList<String> idList;
    ArrayAdapter<String> arrayAdapter;

    ProgressBar progressBar;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView)findViewById(R.id.result);

        arrayList = new ArrayList<String>();

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.INVISIBLE);

        idList = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item,R.id.list_view_text,arrayList);

        listView = (ListView)findViewById(R.id.list_view);

        try {
            dbHelper = new DBHelper(this);
            dbHelper.create_db();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = dbHelper.getWritableDatabase();

        insertedSymbols = (EditText)findViewById(R.id.text_to_find);

        booksCheckBox = (CheckBox)findViewById(R.id.books_checkbox);
        authorsCheckBox = (CheckBox)findViewById(R.id.authors_checkbox);

        searchButton = (Button)findViewById(R.id.find_button);

        booksCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                authorsCheckBox.setChecked(false);

            }
        });

        authorsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                booksCheckBox.setChecked(false);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                arrayList.clear();
                idList.clear();

                Log.i("path",dbHelper.getDatabaseName());

                cursor = db.query("books_table",null,null,null,null,null,null);

                if(authorsCheckBox.isChecked()){

                    String selection = "books_authors LIKE ?";
                    String[] selectionArgs = {'%'+ insertedSymbols.getText().toString().trim() + '%'};

                    cursor = db.query("books_table", null, selection, selectionArgs, null, null, null);

                    Log.i(LOG_TAG,"authors_checkbox is activated");
                }
                if(booksCheckBox.isChecked()){

                    String selection = "books_name LIKE ?";
                    String[] selectionArgs = {'%'+ insertedSymbols.getText().toString().trim() + '%'};

                    cursor = db.query("books_table", null, selection, selectionArgs, null, null, null);

                    Log.i(LOG_TAG,"books_checkbox is activated");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(cursor.moveToFirst()){

                            do{

                                Log.i(LOG_TAG,"id = " + cursor.getInt(cursor.getColumnIndex("id")) + " book = " +
                                        cursor.getString(cursor.getColumnIndex("books_name")) + " authors = " + cursor.getString(cursor.getColumnIndex("books_authors")) +
                                        " rooms = " + cursor.getString(cursor.getColumnIndex("books_rooms")));

                                idList.add(cursor.getString(cursor.getColumnIndex("id")));

                                arrayList.add("" +getResources().getString(R.string.books_name)+": " +
                                        cursor.getString(cursor.getColumnIndex("books_name")) + "\n"+getResources().getString(R.string.authors_name)+": " + cursor.getString(cursor.getColumnIndex("books_authors"))/* +
                                        "\n"+getResources().getString(R.string.books_rooms)+": " + cursor.getString(cursor.getColumnIndex("books_rooms"))*/);

                            }while (cursor.moveToNext());

                        }
                        else{
                            Log.i(LOG_TAG,"0 rows");
                            //Toast.makeText(this,"NULL", Toast.LENGTH_SHORT).show();
                        }

                        listView.post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.INVISIBLE);
                                listView.setAdapter(arrayAdapter);
                                result.setText(getResources().getString(R.string.result)+ " " +idList.size());

                            }
                        });
                    }
                }).start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Proper.class);
                intent.putExtra("id",idList.get(position));
                startActivity(intent);
            }
        });

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),UpdateData.class);
                intent.putExtra("id",idList.get(position));
                startActivity(intent);
                return true;

            }
        });

    }

    public void onStop(){

        super.onStop();
        Log.i(LOG_TAG,"Main onStop");
        db.close();
    }
    public void onPause(){

        super.onPause();
        Log.i(LOG_TAG,"Main onPause");
    }
    public void onResume(){

        super.onResume();
        if(!db.isOpen()){

            db = dbHelper.getWritableDatabase();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_data:

                startActivity(new Intent(this,AddDataToDB.class));
                Toast.makeText(this, getResources().getString(R.string.add_data), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
