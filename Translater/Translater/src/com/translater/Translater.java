package com.translater;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class Translater extends Activity implements OnClickListener {
	
	TextView tvWord;
	EditText edWord;
	Button button;
	String woord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translater);
        tvWord = (TextView) findViewById(R.id.textView1);
        button = (Button) findViewById(R.id.button1);
        tvWord.setText("¬ведите слово, которое вы хотели бы перевести.");
        edWord = (EditText) findViewById(R.id.editText1);
        button.setText("Translate!");
        button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_translater, menu);
        return true;
    }
    
    public void onClick(View v) {
		Intent intent = new Intent(Translater.this, TranslateActivity.class);
		if (edWord != null && edWord.getText().length() != 0) {
			woord = edWord.getText().toString();
		} else {
			woord = "Error";
		}
			
		intent.putExtra("word", woord);
		startActivity(intent);
    }
}
