package com.example.android.booklistinggoogle;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();
    private static String title;
    private static String author;
    Book book;

    public Utils(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * Query the Book list and return an {@link Book} object to represent a single earthquake.
     */
    public static List<Items> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Book} object
        List<Items> items = extractData(jsonResponse);

        // Return the {@link Book}
        return items;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(500 /* milliseconds */);
            urlConnection.setConnectTimeout(750 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), read the input stream and parse
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the first book from the input bookJSON string.
     */
    private static List<Items> extractData(String bookJSON) {
        // If the JSON string is empty or null, then return early. Create ArrayList to hold data.
        ArrayList<Items> bookListing = new ArrayList<>();

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // If there are results in the features array
            if (baseJsonResponse.has("items")) {
                // Extract out the first feature (which is an earthquake)
                JSONArray featureArray = baseJsonResponse.getJSONArray("items");

                for (int count = 0; count < featureArray.length(); count++) {


                    // Extract position of JSON Object
                    JSONObject positionItem = featureArray.getJSONObject(count);

                    // Follow JSON Object to next wanted volumeInfo Key
                    JSONObject volumeInfo = positionItem.getJSONObject("volumeInfo");

                    // Pull out title of book from within volumeInfo key
                    String title = volumeInfo.getString("title");

                    // Verify if current book has an author if so continue to extract data
                    if (volumeInfo.has("authors")) {
                        JSONArray authorList = volumeInfo.getJSONArray("authors");

                        if (authorList.length() > 1) {
                            author = authorList.join(", ").replaceAll("\"", " ");
                        } else if (authorList.length() == 1) {
                            author = authorList.getString(0);
                        } else {
                            author = "";
                        }

                    }

                    // Store data that was found into book object and load into bookListing arrayList
                    Items book = new Items(title, author);
                    bookListing.add(book);

                }

            } else {

                // If no results were retrieved from search
                String title = "No Title to Display";
                String author = "No Author to Display";

                // Store data that was found into book object and load into bookListing arrayList
                Items book = new Items(title, author);
                bookListing.add(book);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return booklisting data
        return bookListing;
    }
}
