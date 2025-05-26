package com.example.thelibrariansapp.models;

import android.os.Parcel;
import java.io.Serializable;

public class Book implements Serializable {

    private String isbn;
    private String title;
    private String genre;
    private String imageUrl;
    private String author;
    private int quantity;
    private int copyOnLease;

    public Book(String isbn, String title, String genre, String imageUrl, String author, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.author = author;
        this.quantity = quantity;
        this.copyOnLease = 0; // Imposta il valore predefinito per copie in prestito
    }

    public Book(String isbn, String title, String genre, String imageUrl,  String author, int quantity, int copyOnLease) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.author = author;
        this.quantity = quantity;
        this.copyOnLease = copyOnLease;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        title = in.readString();
        genre = in.readString();
        imageUrl = in.readString();
        author = in.readString();
        quantity = in.readInt();
        copyOnLease = in.readInt();
    }

    public Book(String isbn, String title, String genre, String imageUrl) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }

    public Book() {
    }

    // Calcola il numero disponibile di copie
    public int getAvailable() {
        return quantity - copyOnLease;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCopyOnLease() {
        return copyOnLease;
    }

    public void setCopyOnLease(int copyOnLease) {
        this.copyOnLease = copyOnLease;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", titolo='" + title + '\'' +
                ", genere='" + genre + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", autore='" + author + '\'' +
                ", quantita=" + quantity +
                ", copiePrestate=" + copyOnLease +
                ", disponibili=" + getAvailable() +
                '}';
    }
}
