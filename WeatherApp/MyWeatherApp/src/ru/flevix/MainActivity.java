package ru.flevix;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView textView, lastUpd;
    ImageButton buttonRefresh;
    ImageView imageView;
    Intent intent;
    Cursor cursor;
    boolean fail = false, unvalid = false, EXIST = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int firstId = 773;
    long interval, curr, last, validInt = 1800;
    Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        buttonRefresh = (ImageButton) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.currentCity);
        textView.setOnClickListener(this);
        lastUpd = (TextView) findViewById(R.id.lastUpdate);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uri = Uri.withAppendedPath(MyContentProvider.WeatherContentUri, Integer.toString(sharedPreferences.getInt("id", firstId)));
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            last = cursor.getInt(cursor.getColumnIndex(DataBaseHandler.lastUpdate));
            if (last == 0) {
                unvalid = true;
            } else {
                curr = System.currentTimeMillis() / 1000;
                if (Math.abs(curr - last) > validInt) {
                    lastUpd.setText(getString(R.string.unvalid));
                    unvalid = true;
                } else {
                    lastUpd.setText(getString(R.string.valid));
                    unvalid = false;
                }
            }
            EXIST = true;
        } catch (Exception e) {
            EXIST = false;
        }
        intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        update();
    }
    void update() {
        if (EXIST) {
            new ParserCondition().execute();
        } else {
            Toast.makeText(this, getString(R.string.cityUnExist), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentCity:
                Toast.makeText(this, getString(R.string.button_hint), Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonRefresh:
                unvalid = true;
                update();
                break;
        }
    }

    class ParserCondition extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonRefresh.setImageResource(R.drawable.refresh_on);
            buttonRefresh.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... v) {
            if (unvalid) {
                try {
                    fail = false;
                    new DownloadHelper().getForecast(getContentResolver(), sharedPreferences.getInt("id", firstId),
                            sharedPreferences.getString("city", getString(R.string.firstCity)));
                } catch (Exception e) {
                    fail = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (fail) {
                Toast.makeText(MainActivity.this, getString(R.string.IOexc), Toast.LENGTH_LONG).show();
            } else {
                lastUpd.setText(getString(R.string.valid));
            }
            buttonRefresh.setImageResource(R.drawable.refresh);
            buttonRefresh.setEnabled(true);

            uri = Uri.withAppendedPath(MyContentProvider.WeatherContentUri,
                    Integer.toString(sharedPreferences.getInt("id", firstId)));
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                textView = (TextView) findViewById(R.id.currentCity);
                textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.name)));
                textView = (TextView) findViewById(R.id.textView_tWing);
                textView.setText(getString(R.string.wind_speed) + cursor.getString(cursor.getColumnIndex(DataBaseHandler.currW)) + getString(R.string.wind_speedV));
                textView = (TextView) findViewById(R.id.textView_tDegrees);
                textView.setText(getString(R.string.temperature) + cursor.getString(cursor.getColumnIndex(DataBaseHandler.currT)) + getString(R.string.temperatureV));
                textView = (TextView) findViewById(R.id.textView_tPressure);
                textView.setText(getString(R.string.pressure) + cursor.getString(cursor.getColumnIndex(DataBaseHandler.currP)) + getString(R.string.pressureV));
                textView = (TextView) findViewById(R.id.textView_tRelHum);
                textView.setText(getString(R.string.humidity) + cursor.getString(cursor.getColumnIndex(DataBaseHandler.currH)) + getString(R.string.humidityV));
                imageView = (ImageView) findViewById(R.id.imageCurrent);
                imageView.setImageResource(getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.currPict))));
                {
                    textView = (TextView) findViewById(R.id.id1);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t1)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict1))), 0, 0);

                    textView = (TextView) findViewById(R.id.id2);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t2)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict2))), 0, 0);

                    textView = (TextView) findViewById(R.id.id3);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t3)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict3))), 0, 0);

                    textView = (TextView) findViewById(R.id.id4);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t4)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict4))), 0, 0);

                    textView = (TextView) findViewById(R.id.id5);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t5)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict5))), 0, 0);

                    textView = (TextView) findViewById(R.id.id6);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t6)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict6))), 0, 0);

                    textView = (TextView) findViewById(R.id.id7);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t7)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict7))), 0, 0);

                    textView = (TextView) findViewById(R.id.id8);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t8)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict8))), 0, 0);

                    textView = (TextView) findViewById(R.id.id9);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t9)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict9))), 0, 0);

                    textView = (TextView) findViewById(R.id.id10);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t10)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict10))), 0, 0);

                    textView = (TextView) findViewById(R.id.id11);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t11)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict11))), 0, 0);

                    textView = (TextView) findViewById(R.id.id12);
                    textView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHandler.t12)) + getString(R.string.temperatureV));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, getPicId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.pict12))), 0, 0);
                }
                cursor.close();
            } catch (Exception ignored) {

            }
        }
        int getPicId(String s) {
            switch (s.charAt(1)) {
                case '0':
                    if (s.charAt(3) == 'm')
                        return R.drawable._0_moon;
                    else
                        return R.drawable._0_sun;
                case '1':
                    if (s.charAt(3) == 'm')
                        return R.drawable._0_moon;
                    else if (s.charAt(3) == 's')
                        return R.drawable._0_sun;
                    else
                        return R.drawable._10_heavy_snow;
                case '2':
                    if (s.charAt(3) == 'c')
                        return R.drawable._2_cloudy;
                    else
                        return R.drawable._255_na;
                case '3':
                    return R.drawable._3_pasmurno;
                case '4':
                    return R.drawable._4_short_rain;
                case '5':
                    return R.drawable._5_rain;
                case '6':
                    return R.drawable._6_lightning;
                case '7':
                    return R.drawable._7_hail;
                case '8':
                    return R.drawable._8_rain_swon;
                case '9':
                    return R.drawable._9_snow;
                default:
                    return R.drawable._255_na;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        interval = 1000 * 60 * 60; // 1 hour

        switch (item.getItemId()) {
            case R.id._10sec:
                interval /= (60 * 6);
                setInterval();
                break;
            case R.id._1_2hour:
                interval /= 2;
                setInterval();
                break;
            case R.id._1hour:
                setInterval();
                break;
            case R.id._2hours:
                setInterval();
                interval *= 2;
                break;
            case R.id._3hours:
                setInterval();
                interval *= 3;
                break;
            case R.id._6hours:
                setInterval();
                interval *= 6;
                break;
            case R.id._9hours:
                setInterval();
                interval *= 9;
                break;
            case R.id._12hours:
                setInterval();
                interval *= 12;
                break;
            case R.id.menuChangeCity:
                intent = new Intent(MainActivity.this, ChooseActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menuAddCity:
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setInterval() {
        editor = sharedPreferences.edit();
        editor.putLong("interval", interval);
        editor.commit();
        intent = new Intent(this, MyService.class);
        stopService(intent);
        startService(intent);
    }

}
