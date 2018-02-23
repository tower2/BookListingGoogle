package com.example.android.booklistinggoogle;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * {@Event} represents an book.
 */
public class Book extends MainActivity {

    /**
     * Title of the book
     */
    private String bookTitle;

    /**
     * Author of book
     */
    private String author;

    private String book_request_url;

    private String query;

    /**
     * Constructs a new {@link Book}.
     *
     * @param bookTitle is the bookTitle of book
     * @param author    is the author of book
     */

    public Book(String bookTitle, String author, String book_request_url) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.book_request_url = book_request_url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button readyToRead = (Button) findViewById(R.id.search_button);
        readyToRead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 * URL for book data from the Google API
                 */
                book_request_url = "https://www.googleapis.com/books/v1/volumes?q=";

                if (getIntent() != null && getIntent().getExtras() != null) {
                    query = getIntent().getExtras().getString("query");
                }

                // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
                View emptyView = findViewById(R.id.empty_view);

                // Connectivity Manager sets connect manager
                ConnectivityManager connected = (ConnectivityManager)
                        getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                // Sets connection status and either exits early if connect isn't establish or performs search
                NetworkInfo activeConnection = connected.getActiveNetworkInfo();
                boolean isConnected = activeConnection != null & activeConnection.isConnectedOrConnecting();

                if (isConnected) {

                    // Hide TextViews, EditText, and Button after being clicked
                    EditText searchText = (EditText) findViewById(R.id.book_edit_text);

                    // Grabs information from EditText, converts to String, and removes all white spaces and "+"
                    String search = searchText.toString();

                    //  String search = searchText.getText().toString().replace(" ", "");

                    // Concatenate book_request_url with search
                    book_request_url = book_request_url + search;
                    book_request_url = book_request_url.replaceAll(" ", "");

                    // Creates an AsyncTask to perform HTTP request to the URL in background thread
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(book_request_url);
                } else {
                    Toast.makeText(Book.this, "Connect Device to WiFi or LAN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateUi(bookTitle, author);
    }

    /**
     * Update the UI with the given book information.
     */
    private void updateUi(String bookTitle, String author) {


        TextView authorTextView = (TextView) findViewById(R.id.author_textView);
        TextView titleTextView = (TextView) findViewById(R.id.title_textView);

        // Will set only Title
        if (bookTitle != null || !bookTitle.equals("") &&
                author == null || author.equals("")) {

            titleTextView.getText();
            titleTextView.setText(bookTitle);

            authorTextView.getText();
            authorTextView.setText("");
        }
        // Will set Author to value and set title to empty string
        else if (author != null || !author.equals("") &&
                bookTitle == null || bookTitle.equals("")) {

            authorTextView.getText();
            authorTextView.setText(author);

            titleTextView.getText();
            titleTextView.setText("");
        }
        // Will set both Title & Author
        else {
            authorTextView.getText();
            authorTextView.setText(author);

            titleTextView.getText();
            titleTextView.setText(bookTitle);
        }
    }

    public String getbookTitle() {
        return bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    private class BookAsyncTask extends AsyncTask<String, Void, Book> {

        // Performed in a background thread
        @Override
        protected Book doInBackground(String... urls) {

            // Exits early and doesn't perform URL request if there isn't an URL present
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            Uri groundWorkUri = Uri.parse(urls[0]);
            Uri.Builder uriBuilder = groundWorkUri.buildUpon();
            uriBuilder.appendQueryParameter("q", query);

            // Performs the HTTP request for book data and process the response.
            return (Book) Utils.fetchBookData(uriBuilder.toString());
        }

        // Performed in the main UI thread
        protected void onPostExecute(List<Items> data) {

            // If there is no result, do nothing
            if (data == null) {
                return;
            }

            // Update the information displayed to the user.
            data.clear();
        }
    }
}