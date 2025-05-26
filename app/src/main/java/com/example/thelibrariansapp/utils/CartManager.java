package com.example.thelibrariansapp.utils;

import com.example.thelibrariansapp.models.Book;
import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<Book> bookList; // Lista globale dei libri nel carrello

    // Costruttore privato per il singleton
    private CartManager() {
        bookList = new ArrayList<>();
    }

    // Metodo per ottenere l'istanza singleton
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Metodo per ottenere i libri nel carrello
    public ArrayList<Book> getBookList() {
        return bookList;
    }

    // Metodo per aggiungere un libro al carrello
    public void addBook(Book book) {
        bookList.add(book);
    }

    // Metodo per rimuovere un libro dal carrello
    public void removeBook(Book book) {
        bookList.remove(book);
    }

    // Metodo per svuotare il carrello
    public void clearCart() {
        bookList.clear();
    }
}
