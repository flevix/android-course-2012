package ru.flevix;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 19.11.12
 * Time: 3:28
 */
public class ChooseActivity extends Activity {

    Cursor cursor;
    final Uri cityUri = MyContentProvider.CityContentUri;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListView listView;
    ArrayList<String> val;
    ArrayAdapter<String> adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        listView = (ListView) findViewById(R.id.list_current);


        View header = getLayoutInflater().inflate(R.layout.header, null);
        listView.addHeaderView(header);
        val = new ArrayList<String>();
        try {
            cursor = getContentResolver().query(cityUri, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                val.add(cursor.getString(cursor.getColumnIndex(DataBaseHandler.cityNAME)));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ignored) {

        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, val);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Log.d("list", "itemClick: position = " + position + ", id = "+ id);
                if (position == 0) {
                    Toast.makeText(ChooseActivity.this, R.string.chooseHint, Toast.LENGTH_LONG).show();
                    return;
                }
                cursor = getContentResolver().query(cityUri, null, null, null, null);
                cursor.moveToFirst();
                for (int i = 1; i < position; i++) {
                    cursor.moveToNext();
                }
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChooseActivity.this);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.putInt("id", cursor.getInt(0));
                editor.putString("city", cursor.getString(1));
                editor.commit();
                Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("list", "LongItemClick: i = " + i + ", l = "+ l);
                if (val.size() == 1) return true;
                if (i == 0) return false;

                cursor = getContentResolver().query(cityUri, null, null, null, null);
                cursor.moveToFirst();
                for (int j = 0; j < i - 1; j++) {
                    cursor.moveToNext();
                }
                val.remove(cursor.getString(1));
                uri = Uri.withAppendedPath(MyContentProvider.CityContentUri, Integer.toString(cursor.getInt(0)));
                getContentResolver().delete(uri, null, null);
                uri = Uri.withAppendedPath(MyContentProvider.WeatherContentUri, Integer.toString(cursor.getInt(0)));
                getContentResolver().delete(uri, null, null);
                adapter = new ArrayAdapter<String>(ChooseActivity.this,
                        android.R.layout.simple_list_item_1, val);
                listView.setAdapter(adapter);
                return true;
            }
        });

    }
    Uri uri;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Intent intent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menuAddCity:
                intent = new Intent(ChooseActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}