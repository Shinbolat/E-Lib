package com.shinbolat.e_lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class DBHelper extends SQLiteOpenHelper {

    Context context;

    SQLiteDatabase db;

    final String LOG_TAG = "db_log";

    final private String DB_PATH = "/data/data/com.shinbolat.e_lib/databases/";
    final private String DB_NAME = "lib_db";

    public DBHelper(Context context) throws SQLException, IOException {

        super(context, "lib_db", null, 1);

        this.context = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG_TAG, "--- onCreate database ---");

        // создаем таблицу с полями
//        db.execSQL("create table books_table("
//                + "id integer primary key autoincrement,"
//                + "books_name text,"
//                + "books_authors text,"
//                + "books_rooms text"
//                + ");");

//        boolean dbExist = CheckDatabase();

    }

    public void create_db(){

        InputStream Input = null;

        OutputStream Output = null;

        try {

            File file = new File(DB_PATH + DB_NAME);

            if (!file.exists()) {

                this.getReadableDatabase();
                //получаем локальную бд как поток
                Input = context.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DB_NAME;

                // Открываем пустую бд
                Output = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = Input.read(buffer)) > 0) {
                    Output.write(buffer, 0, length);
                }

                Output.flush();
                Output.close();
                Input.close();
            }
        }
        catch(IOException ex){

        }
    }
    public void open() throws SQLException {
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);

    }


    public synchronized void close() {
        if(db != null) {
            db.close();
        }
        super.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}