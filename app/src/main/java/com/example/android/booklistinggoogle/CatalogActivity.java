package com.example.android.booklistinggoogle;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    /*
        Set variables to retrieve Books data
     */
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private ListView listView;
    private ItemsAdapter adapter;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //Find the id for the following views
        listView = (ListView) findViewById(R.id.list_container);

        //this retrieves data from research field in the MainActvity
        if (getIntent() != null && getIntent().getExtras() != null) {
            search = getIntent().getExtras().getString("query").replaceAll(" ", "");
        }

        // Thread request in background
        booksAsyncTask task = new booksAsyncTask();
        task.execute(REQUEST_URL);

        // Shows up when empty
        //listView.setEmptyView(emptyTextView);
        adapter = new ItemsAdapter(this, new ArrayList<Items>());
        listView.setAdapter(adapter);

        // Goes to specific location when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Uri site;

                Items itemsList = (Items) adapterView.getAdapter().getItem(position);
                String author = itemsList.getAuthors();
                site = Uri.parse(String.valueOf(itemsList));

                Intent intent = new Intent(Intent.ACTION_VIEW, site);

                if (getIntent().resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

    }

    //determine if connection is active
    private Boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread.
     */
    private class booksAsyncTask extends AsyncTask<String, Void, List<Items>> {
        /**
         * This method is invoked (or called) on a background thread, so we can perform
         * long-running operations like making a network request. It is NOT okay to update the UI from a background thread.
         */
        @Override
        protected List<Items> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            //get the URL of the google server
            Uri baseUri = Uri.parse(urls[0]);

            //this is used to build the URL with the query parameters
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // "q" has the url perform a query of books
            uriBuilder.appendQueryParameter("q", search);
            uriBuilder.appendQueryParameter("maxResults", "10"); //this adds the max books listed
            uriBuilder.appendQueryParameter("orderBy", "relevance"); //this defines the order of the list
            return Utils.fetchBookData(uriBuilder.toString());
        }

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         */
        @Override
        protected void onPostExecute(List<Items> data) {
            if (isConnected()) {
                // Clear the adapter of previous data
                adapter.clear();

                // If there is a valid list of books, it add them to the adapter's data set.
                if (data != null && !data.isEmpty()) {
                    adapter.addAll(data);
                }
            }
        }
    }
}


