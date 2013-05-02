package ru.flevix.TODO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 16.01.13
 * Time: 11:27
 */

public class MyArrayAdapter extends ArrayAdapter<VeryGoogType> {
    private final Context context;
    private final ArrayList<VeryGoogType> values;

    public MyArrayAdapter(Context context, ArrayList<VeryGoogType> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    //Cursor cursor;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position).name);
        int color = values.get(position).prior;
        switch (color) {
            case 1:
                imageView.setBackgroundColor(Color.RED);
                break;
            case 2:
                imageView.setBackgroundColor(Color.YELLOW);
                break;
            case 3:
                imageView.setBackgroundColor(Color.GREEN);
                break;
            default:
                imageView.setBackgroundColor(Color.WHITE);
        }
        return rowView;
    }
}
