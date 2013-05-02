package ru.flevix.TODO;/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 16.01.13
 * Time: 11:32
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {
    static final String dbName = "ColorDB";

    //Weather Table
    static final String TodoTable = "TODOTABLE";
    static final String ID = "ID";
    static final String name = "NAME";
    static final String prior = "PRIOR";
    static final String info = "INFO";


    public DataBaseHandler(Context context) {
        super(context, dbName, null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TodoTable + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        name + " TEXT, " + prior + " INTEGER," + info + " STRING);"
        );

        TODOAdd(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodoTable + ";");
        onCreate(sqLiteDatabase);
    }

    ContentValues contentValues = new ContentValues();
    void TODOAdd(SQLiteDatabase sqLiteDatabase) {
        {
            contentValues.put(name, "first");
            contentValues.put(prior, 1);
            contentValues.put(info, "first todo");
            sqLiteDatabase.insert(TodoTable, null, contentValues);
            contentValues.clear();
            contentValues.put(name, "second");
            contentValues.put(prior, 2);
            contentValues.put(info, "second todo");
            sqLiteDatabase.insert(TodoTable, null, contentValues);
            contentValues.clear();
        }
    }
}
