package com.example.android.booklistinggoogle;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jermainegoins on 8/17/17.
 */

public class ItemsAdapter extends ArrayAdapter<Items> {

    // Constructor for list adapter
    public ItemsAdapter(Activity context, ArrayList<Items> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        // Inflates to list_item layout
        listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        if (position < getCount()) {
            Items currentItems = getItem(position);

            //Displays views to user
            TextView titleTextView = (TextView) listView.findViewById(R.id.title_textView);
            titleTextView.setText(currentItems.getBookTitle());

            TextView authorTextView = (TextView) listView.findViewById(R.id.author_textView);
            authorTextView.setText(currentItems.getAuthors());

        }
        return listView;
    }
}
