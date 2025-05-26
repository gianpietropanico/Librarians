package com.example.thelibrariansapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Loans {

    private User user;
    private Book book;
    private Date startDate;
    private Date dueDate;
    private String status;



    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date duedate) {
        this.dueDate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    // Getter e Setter per User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public LendLease(List<Book> books, Date startDate, Date duedate) {
//        this.books = books;
//        this.startDate = startDate;
//        this.dueDate = dueDate;
//    }
    public String getFormattedStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(startDate);
}

    public String getFormattedDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(dueDate);
    }
    public Loans(User user, Book books, Date startDate, Date dueDate, String status) {
        this.user = user;
        this.book = books;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Loans() {

    }
}
