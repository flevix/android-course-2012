package com.translater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.TextView;

public class TranslateActivity extends Activity 
{
	Gallery g1;
	TextView trWord;
	TextView trWordRus;
	GetTranslate gt = new GetTranslate();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        trWord = (TextView) findViewById(R.id.textView1);
        trWordRus = (TextView) findViewById(R.id.textView2);
        Intent i = getIntent();
        String woord = (String) i.getSerializableExtra("word");
        String rusWord = gt.setRus(woord);
        trWord.setText(woord);
        trWordRus.setText(rusWord);
        g1 = (Gallery) findViewById(R.id.gallery1);
        g1.setAdapter(new ImageAdapter(this));
    }
    
}