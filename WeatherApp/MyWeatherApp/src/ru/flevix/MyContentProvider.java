package ru.flevix;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 04.12.12
 * Time: 19:56
 */

public class MyContentProvider extends ContentProvider {

    static final String AUTHORITY = "ru.flevix.providers.WeatherForecast";
    static final String WeatherPath = "weather";
    static final String CityPath = "city";
    public static final Uri WeatherContentUri = Uri.parse("content://" + AUTHORITY + "/" + WeatherPath);
    public static final Uri CityContentUri = Uri.parse("content://" + AUTHORITY + "/" + CityPath);

    //Data Type
    //some lines
    static final String WeatherContentType = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + WeatherPath;
    static final String CityContentType = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CityPath;
    //one line
    static final String WeatherContentItemType = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + WeatherPath;
    static final String CityContentItemType = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CityPath;

    //Uri Matcher
    static final int UriWeather = 1;
    static final int UriWeatherId = 2;
    static final int UriCity = 3;
    static final int UriCityId = 4;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, WeatherPath, UriWeather);
        uriMatcher.addURI(AUTHORITY, WeatherPath + "/#", UriWeatherId);

        uriMatcher.addURI(AUTHORITY, CityPath, UriCity);
        uriMatcher.addURI(AUTHORITY, CityPath + "/#", UriCityId);
    }

    public Uri resultUri;
    private String id = "";
    private DataBaseHandler dataBaseHandler;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        dataBaseHandler = new DataBaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        switch (uriMatcher.match(uri)) {
            case UriWeather:
                break;
            case UriCity:
                break;
            case UriCityId:
            case UriWeatherId:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = DataBaseHandler.ID + " = " + id;
                } else {
                    s = s + " AND " + DataBaseHandler.ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        sqLiteDatabase = dataBaseHandler.getWritableDatabase();
        return sqLiteDatabase.query(getTableName(uri), strings, s, strings1, null, null, null);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case UriWeather:
                return WeatherContentType;
            case UriWeatherId:
                return WeatherContentItemType;
            case UriCity:
                return CityContentType;
            case UriCityId:
                return CityContentItemType;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri tempUri;
        switch (uriMatcher.match(uri)) {
            case UriWeather:
                tempUri = WeatherContentUri;
                break;
            case UriCity:
                tempUri = CityContentUri;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        sqLiteDatabase = dataBaseHandler.getWritableDatabase();
        long rowID = sqLiteDatabase.insert(getTableName(uri), null, contentValues);
        resultUri = ContentUris.withAppendedId(tempUri, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String s, String[] sl) {
        switch (uriMatcher.match(uri)) {
            case UriCity:
            case UriWeather:
                break;
            case UriWeatherId:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = DataBaseHandler.ID + " = " + id;
                } else {
                    s = s + " AND " + DataBaseHandler.ID + " = " + id;
                }
                break;
            case UriCityId:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = DataBaseHandler.cityID + " = " + id;
                } else {
                    s = s + " AND " + DataBaseHandler.cityID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        sqLiteDatabase = dataBaseHandler.getWritableDatabase();
        int cnt = sqLiteDatabase.delete(getTableName(uri), s, sl);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        Log.i("contentProviderLog", "UPDATE_CONTENT_PROVIDER");
        switch (uriMatcher.match(uri)) {
            case UriWeather:
                break;
            case UriCity:
                break;
            case UriCityId:
            case UriWeatherId:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = DataBaseHandler.ID + " = " + id;
                } else {
                    s = s + " AND " + DataBaseHandler.ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        sqLiteDatabase = dataBaseHandler.getWritableDatabase();
        int cnt = sqLiteDatabase.update(getTableName(uri), contentValues, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private String getTableName(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case UriWeather:
            case UriWeatherId:
                return DataBaseHandler.WeatherTable;
            case UriCity:
            case UriCityId:
                return DataBaseHandler.CITYTABLE;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }
}
