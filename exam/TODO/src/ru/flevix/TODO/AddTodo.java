package ru.flevix.TODO;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 16.01.13
 * Time: 12:04
 */

public class AddTodo extends Activity implements View.OnClickListener {

    EditText editName, editPrior, editInfo;
    Button edAdButton;
    int mode = 0;
    int id = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);
        editName = (EditText) findViewById(R.id.editName);
        editPrior = (EditText) findViewById(R.id.editPrior);
        editInfo = (EditText) findViewById(R.id.editInfo);
        edAdButton = (Button) findViewById(R.id.buttonAdd);
        edAdButton.setOnClickListener(this);
        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        if (mode == 1) {
            edAdButton.setText(R.string.editTodo);
            editName.setText(intent.getStringExtra("name"));
            editPrior.setText(intent.getStringExtra("prior"));
            editInfo.setText(intent.getStringExtra("info"));
            id = intent.getIntExtra("id", 0);
        } else {
            edAdButton.setText(R.string.insertTodo);
        }
    }

    ContentValues contentValues = new ContentValues();

    @Override
    public void onClick(View view) {
        contentValues.clear();
        contentValues.put(DataBaseHandler.name, editName.getText().toString());
        contentValues.put(DataBaseHandler.prior, Integer.parseInt(editPrior.getText().toString()));
        contentValues.put(DataBaseHandler.info, editInfo.getText().toString());
        try {
            if (mode == 1) {
                Uri uri = ContentUris.withAppendedId(MyContentProvider.TodoContentUri, id);
                getContentResolver().update(uri, contentValues, null, null);
            } else {
                getContentResolver().insert(MyContentProvider.TodoContentUri, contentValues);
            }
        } catch (Exception e) {
        }
        intent = new Intent(AddTodo.this, MyActivity.class);
        startActivity(intent);
        finish();
    }

    Intent intent;

    @Override
    public void onBackPressed() {
        intent = new Intent(AddTodo.this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}