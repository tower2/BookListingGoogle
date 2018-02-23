package com.example.android.booklistinggoogle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Displays the perceived strength of a single book event based on responses from people who
 * felt the book.
 */
public class MainActivity extends AppCompatActivity {

    private String search;
    int hasInternet;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Connectivity Manager sets connect manager
        ConnectivityManager connected = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Sets connection status and either exits early if connect isn't establish or performs search
        NetworkInfo activeConnection = connected.getActiveNetworkInfo();

        // Converts activeConnection into workable boolean value to test connectivity
        if (activeConnection != null) {
            isConnected = activeConnection.isConnectedOrConnecting();
        } else {
            isConnected = false;
        }

        //   Boolean isNotConnected = activeConnection == null && !activeConnection.isConnectedOrConnecting();

        if (isConnected) {

            // Locates search button
            final Button readyToRead = (Button) findViewById(R.id.search_button);
            readyToRead.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // Hide TextViews, EditText, and Button after being clicked
                    EditText searchText = (EditText) findViewById(R.id.book_edit_text);
                    search = searchText.getText().toString();

                    if (search.isEmpty()) {

                        Toast.makeText(MainActivity.this, "You Forgot Something.", Toast.LENGTH_SHORT).show();

                        /**
                         *  Has the user go to Empty Activity and try their search again when they
                         *  click on the search again button.
                         */

                        Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                        startActivity(intent);

                    } else {

                        // Retrieves the value within editText and translate data to Catalog Activity
                        Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                        intent.putExtra("query", search);
                        startActivity(intent);
                    }

                }
            });
        } else {
            Toast.makeText(MainActivity.this, "OOPS", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(MainActivity.this).setMessage(R.string.warning_connect_wifi).show();
            return;
        }

    }
}
