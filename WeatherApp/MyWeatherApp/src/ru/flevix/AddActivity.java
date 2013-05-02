package ru.flevix;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 13.12.12
 * Time: 5:42
 */

public class AddActivity extends Activity implements View.OnClickListener {

    EditText editText;
    ImageButton imageButton;
    DownloadHelper downloadHelper = new DownloadHelper();
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    String newCityName, message;
    int id;
    Cursor cursor;
    ContentValues contentValues;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HashMap<String, Integer> citys = new HashMap<String, Integer>();
    Set set;
    Iterator iterator;
    Map.Entry hashMapEntry;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        editText = (EditText) findViewById(R.id.editText);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        arrayList = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newCityName = arrayList.get(i);
                id = citys.get(newCityName);
                try {
                    cursor = getContentResolver().query(Uri.withAppendedPath(MyContentProvider.CityContentUri, newCityName), null, null, null, null);
                    cursor.close();
                } catch (Exception e) {
                    contentValues = DataBaseHandler.fillCVbyNull(newCityName);
                    contentValues.put(DataBaseHandler.ID, id);
                    getContentResolver().insert(MyContentProvider.WeatherContentUri, contentValues);
                    contentValues.clear();
                    contentValues.put(DataBaseHandler.cityID, id);
                    contentValues.put(DataBaseHandler.cityNAME, newCityName);
                    getContentResolver().insert(MyContentProvider.CityContentUri, contentValues);

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddActivity.this);
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putInt("id", id);
                    editor.putString("city", newCityName);
                    editor.commit();
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (editText.getText().toString().length() != 0) {
            imageButton.setBackgroundResource(R.color.red);
            imageButton.setEnabled(false);
            new search().execute();
        }
    }

    class search extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            message = "";
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                citys = downloadHelper.getCitys(editText.getText().toString());
            } catch (IOException e) {
                message = getString(R.string.IOexc);
            } catch (Exception e) {
                message = getString(R.string.cityUnAvailable);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            imageButton.setBackgroundResource(R.color.white);
            imageButton.setEnabled(true);
            if (!message.equals("")) {
                Toast.makeText(AddActivity.this, message, Toast.LENGTH_LONG).show();
            }
            if (!citys.isEmpty()) {
                set = citys.entrySet();
                iterator = set.iterator();
                while (iterator.hasNext()) {
                    hashMapEntry = (HashMap.Entry) iterator.next();
                    arrayList.add(hashMapEntry.getKey().toString());
                }
                adapter = new ArrayAdapter<String>(AddActivity.this,
                        android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Intent intent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
        case R.id.menuChangeCity:
            intent = new Intent(AddActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
            break;
        default:
            break;
    }
        return super.onOptionsItemSelected(item);
    }
}