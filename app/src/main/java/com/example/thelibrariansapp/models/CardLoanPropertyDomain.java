package com.example.thelibrariansapp.models;

import java.io.Serializable;
import java.time.LocalDate;

public class CardLoanPropertyDomain implements Serializable {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    //Ogni prestito è caratterizzato da email del utente,data di inizio e fine prestito
    private String email;
    private LocalDate startDate;
    private LocalDate dueDate;

    public CardLoanPropertyDomain(String email, LocalDate startDate, LocalDate dueDate) {
        this.email = email;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }
}
