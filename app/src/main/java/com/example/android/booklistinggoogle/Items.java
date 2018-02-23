package com.example.android.booklistinggoogle;

/**
 * Created by jermainegoins on 8/17/17.
 */

public class Items {

    // Variables the will store title, author, and imageUrl
    private String bookTitle; //this is the title of the book
    private String authors; //this gets the a list of authors

    // Constructor for retrieved listing
    public Items(String bookTitle, String authors) {
        this.bookTitle = bookTitle;
        this.authors = authors;
    }

    public Items() {

    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthors() {
        return authors;
    }
}
