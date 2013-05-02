package ru.flevix.TODO;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 16.01.13
 * Time: 11:23
 */
public class MyActivity extends ListActivity {

    ArrayList<VeryGoogType> values;
    Cursor cursor;
    String name, info;
    int prior;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            values = new ArrayList<VeryGoogType>();
            try {
                cursor = getContentResolver().query(MyContentProvider.TodoContentUri, null, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    name = cursor.getString(cursor.getColumnIndex(DataBaseHandler.name));
                    prior = cursor.getInt(cursor.getColumnIndex(DataBaseHandler.prior));
                    info = cursor.getString(cursor.getColumnIndex(DataBaseHandler.info));
                    values.add(new VeryGoogType(name, prior, info));
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (Exception ignored) {

            }
        }
        MyArrayAdapter adapter = new MyArrayAdapter(this, values);
        setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        intent = new Intent(MyActivity.this, AddTodo.class);
        intent.putExtra("mode", 1);
        intent.putExtra("name", values.get(position).name);
        intent.putExtra("prior", Integer.toString(values.get(position).prior));
        intent.putExtra("id", position + 1 );
        intent.putExtra("info", values.get(position).info);
        startActivity(intent);
        finish();
//        Toast.makeText(MyActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Intent intent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menuAddNewTodo:
                intent = new Intent(MyActivity.this, AddTodo.class);
                intent.putExtra("mode", 0);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}