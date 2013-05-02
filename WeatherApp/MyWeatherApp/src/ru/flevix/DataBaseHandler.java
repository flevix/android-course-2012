package ru.flevix;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 02.12.12
 * Time: 19:30
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    static final String dbName = "weatherDB";

    //Weather Table
    static final String WeatherTable = "WEATHERTABLE";
    static final String ID = "ID";
    static final String lastUpdate = "LASTUPDATE";
    static final String name = "NAME";
    static final String currPict = "PICT";
    static final String currT = "CURRT";
    static final String currP = "CURRP";
    static final String currW = "CURRW";
    static final String currH = "CURRH";
    static final String pict1 = "PICT1";
    static final String t1 = "T1";
    static final String pict2 = "PICT2";
    static final String t2 = "T2";
    static final String pict3 = "PICT3";
    static final String t3 = "T3";
    static final String pict4 = "PICT4";
    static final String t4 = "T4";
    static final String pict5 = "PICT5";
    static final String t5 = "T5";
    static final String pict6 = "PICT6";
    static final String t6 = "T6";
    static final String pict7 = "PICT7";
    static final String t7 = "T7";
    static final String pict8 = "PICT8";
    static final String t8 = "T8";
    static final String pict9 = "PICT9";
    static final String t9 = "T9";
    static final String pict10 = "PICT10";
    static final String t10 = "T10";
    static final String pict11 = "PICT11";
    static final String t11 = "T11";
    static final String pict12 = "PICT12";
    static final String t12 = "T12";

    //City Table
    static final String CITYTABLE = "CITYTABLE";
    static final String cityID = "cityID";
    static final String cityNAME = "CITYNAME";

    ContentValues contentValues = new ContentValues();




    public DataBaseHandler(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + WeatherTable + " (" + ID + " INTEGER PRIMARY KEY, " +
                lastUpdate + " INTEGER, " + name + " TEXT, " +
                currPict + " TEXT, " + currT + " TEXT, " + currP + " TEXT, " + currH + " TEXT, " + currW + " TEXT, " +
                pict1 + " TEXT, " + t1 + " TEXT, " + pict2 + " TEXT, " + t2 + " TEXT, " + pict3 + " TEXT, " + t3 + " TEXT, " +
                pict4 + " TEXT, " + t4 + " TEXT, " + pict5 + " TEXT, " + t5 + " TEXT, " + pict6 + " TEXT, " + t6 + " TEXT, " +
                pict7 + " TEXT, " + t7 + " TEXT, " + pict8 + " TEXT, " + t8 + " TEXT, " + pict9 + " TEXT, " + t9 + " TEXT, " +
                pict10 + " TEXT, " + t10 + " TEXT, " +pict11 + " TEXT, " + t11 + " TEXT, " +pict12 + " TEXT, " + t12 + " TEXT);"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + CITYTABLE + " (" + cityID + " INTEGER PRIMARY KEY, " + cityNAME + " TEXT);"
        );
        cityAdd(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherTable + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CITYTABLE + ";");
        onCreate(sqLiteDatabase);
    }

    void cityAdd(SQLiteDatabase sqLiteDatabase) {
        {
            contentValues = fillCVbyNull("Санкт-Петербург. Россия");
            contentValues.put(ID, 773);
            sqLiteDatabase.insert(WeatherTable, null, contentValues);
            contentValues.clear();
            contentValues = fillCVbyNull("Москва. Россия");
            contentValues.put(ID, 27);
            sqLiteDatabase.insert(WeatherTable, null, contentValues);
            contentValues.clear();
            contentValues = fillCVbyNull("Новокузнецк. Россия");
            contentValues.put(ID, 700);
            sqLiteDatabase.insert(WeatherTable, null, contentValues);
            contentValues.clear();
            contentValues = fillCVbyNull("Новосибирск. Россия");
            contentValues.put(ID, 902);
            sqLiteDatabase.insert(WeatherTable, null, contentValues);
        }
        {
            contentValues.clear();
            contentValues.put(cityID, 773);
            contentValues.put(cityNAME, "Санкт-Петербург. Россия");
            sqLiteDatabase.insert(CITYTABLE, null, contentValues);
            contentValues.put(cityID, 27);
            contentValues.put(cityNAME, "Москва. Россия");
            sqLiteDatabase.insert(CITYTABLE, null, contentValues);
            contentValues.put(cityID, 700);
            contentValues.put(cityNAME, "Новокузнецк. Россия");
            sqLiteDatabase.insert(CITYTABLE, null, contentValues);
            contentValues.put(cityID, 902);
            contentValues.put(cityNAME, "Новосибирск. Россия");
            sqLiteDatabase.insert(CITYTABLE, null, contentValues);
            contentValues.clear();
        }
    }

    static public ContentValues fillCVbyNull(String city) {
        ContentValues cv = new ContentValues();
        cv.clear();
        //cv.put(ID, id);
        cv.put(lastUpdate, 0);
        cv.put(name, city);
        cv.put(currPict, "_255_na");
        cv.put(currT, "N/A");
        cv.put(currP, "N/A");
        cv.put(currW, "N/A");
        cv.put(currH, "N/A");
        cv.put(pict1, "_255_na");
        cv.put(t1, "N/A");
        cv.put(pict2, "_255_na");
        cv.put(t2, "N/A");
        cv.put(pict3, "_255_na");
        cv.put(t3, "N/A");
        cv.put(pict4, "_255_na");
        cv.put(t4, "N/A");
        cv.put(pict5, "_255_na");
        cv.put(t5, "N/A");
        cv.put(pict6, "_255_na");
        cv.put(t6, "N/A");
        cv.put(pict7, "_255_na");
        cv.put(t7, "N/A");
        cv.put(pict8, "_255_na");
        cv.put(t8, "N/A");
        cv.put(pict9, "_255_na");
        cv.put(t9, "N/A");
        cv.put(pict10, "_255_na");
        cv.put(t10, "N/A");
        cv.put(pict11, "_255_na");
        cv.put(t11, "N/A");
        cv.put(pict12, "_255_na");
        cv.put(t12, "N/A");
        return cv;
    }
}
