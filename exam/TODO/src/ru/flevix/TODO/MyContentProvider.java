package ru.flevix.TODO;/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 16.01.13
 * Time: 11:37
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    static final String AUTHORITY = "ru.flevix.TODO.todolist";
    static final String TodoPath = "todo";
    public static final Uri TodoContentUri = Uri.parse("content://" + AUTHORITY + "/" + TodoPath);
    //Data Type
    //some lines
    static final String TodoContentType = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TodoPath;
    //one line
    static final String TodoContentItemType = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TodoPath;

    //Uri Matcher
    static final int UriTodo = 1;
    static final int UriTodoId = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TodoPath, UriTodo);
        uriMatcher.addURI(AUTHORITY, TodoPath + "/#", UriTodoId);
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
            case UriTodo:
                break;
            case UriTodoId:
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
            case UriTodo:
                return TodoContentType;
            case UriTodoId:
                return TodoContentItemType;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri tempUri;
        switch (uriMatcher.match(uri)) {
            case UriTodo:
                tempUri = TodoContentUri;
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
            case UriTodo:
                break;
            case UriTodoId:
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
        int cnt = sqLiteDatabase.delete(getTableName(uri), s, sl);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        Log.i("contentProviderLog", "UPDATE_CONTENT_PROVIDER");
        switch (uriMatcher.match(uri)) {
            case UriTodo:
                break;
            case UriTodoId:
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
            case UriTodo:
            case UriTodoId:
                return DataBaseHandler.TodoTable;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }
}
