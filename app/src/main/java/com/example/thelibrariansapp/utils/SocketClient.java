package com.example.thelibrariansapp.utils;

import com.example.thelibrariansapp.models.Book;
import com.example.thelibrariansapp.models.Loans;
import com.example.thelibrariansapp.models.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SocketClient {
    private static final String SERVER_IP = "35.192.39.236"; // Sostituisci con l'indirizzo IP del server
    private static final int SERVER_PORT = 8080; // Porta del server

    public String sendCredentials(String type, String username, String password) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);
            System.out.println("Password: %s\n" + password);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String credentials = type + ":" + username + ":" + password + "\n";  // Indica se è registrazione o login
            OutputStream os = socket.getOutputStream();
            os.write(credentials.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serverResponse; // Restituisci la risposta
    }

    public String check(String type, String username) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String credentials = type + ":" + username + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(credentials.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serverResponse; // Restituisci la risposta
    }

    public ArrayList<Book> getAllBooks(String type) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Book> books = new ArrayList<>();

        try {
            // Test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: " + type);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Imposta un timeout per la lettura dal server (5000 ms = 5 secondi)
            socket.setSoTimeout(5000);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String getbooks = type + ":\n";  // Indica il tipo di richiesta
            OutputStream os = socket.getOutputStream();
            os.write(getbooks.getBytes());
            outputStream.flush();

            // Ricevi i dati dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            // Continua a leggere fino a quando non arriva "END"
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Dati ricevuti: " + line); // Log dei dati ricevuti
                String[] bookData = line.split(",");  // Spezza la stringa separata da virgole
                if (bookData.length == 7) {  // Assicurati che ci siano tutti i campi
                    try {
                        Book book = new Book(
                                bookData[0],    // isbn
                                bookData[1],    // titolo
                                bookData[2],    // genere
                                bookData[3],    // imageUrl
                                bookData[4],    // autore
                                Integer.parseInt(bookData[5]),  // quantita
                                Integer.parseInt(bookData[6])   // copiePrestate
                        );
                        books.add(book);  // Aggiungi il libro alla lista
                    } catch (NumberFormatException e) {
                        System.err.println("Errore durante il parsing dei dati del libro: " + e.getMessage());
                    }
                } else {
                    System.err.println("Dati libro non corretti: " + line);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ora stampo la lista");
        System.out.println(books.toString());
        return books;  // Restituisci la lista di libri
    }


    public ArrayList<Book> getFilteredBooks(String type) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Book> filteredBooks = new ArrayList<>();

        try {
            // Test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Imposta un timeout per la lettura dal server (5000 ms = 5 secondi)
            socket.setSoTimeout(5000);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());

            String getbooks = type + ":\n";  // Indica il tipo di richiesta
            OutputStream os = socket.getOutputStream();
            os.write(getbooks.getBytes());
            outputStream.flush();

            // Ricevi i dati dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            // Continua a leggere fino a quando non arriva "END"
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Dati ricevuti: " + line); // Log dei dati ricevuti
                String[] bookData = line.split(",");  // Spezza la stringa separata da virgole
                if (bookData.length == 7) {  // Assicurati che ci siano tutti i campi
                    try {
                        Book book = new Book(
                                bookData[0],    // isbn
                                bookData[1],    // titolo
                                bookData[2],    // genere
                                bookData[3],    // imageUrl
                                bookData[4],    // autore
                                Integer.parseInt(bookData[5]),  // quantita
                                Integer.parseInt(bookData[6])   // copiePrestate
                        );
                        filteredBooks.add(book);  // Aggiungi il libro alla lista
                    } catch (NumberFormatException e) {
                        System.err.println("Errore durante il parsing dei dati del libro: " + e.getMessage());
                    }
                } else {
                    System.err.println("Dati libro non corretti: " + line);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ora stampo la lista");
        System.out.println(filteredBooks.toString());
        return filteredBooks;  // Restituisci la lista di libri
    }

    public ArrayList<Book> getBooksByIsbn(String isbn) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Book> filteredBooks = new ArrayList<>();

        try {
            // Test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Imposta un timeout per la lettura dal server (5000 ms = 5 secondi)
            socket.setSoTimeout(5000);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());

            String getbooks = "isbn:" + isbn + "\n";  // Indica il tipo di richiesta
            OutputStream os = socket.getOutputStream();
            os.write(getbooks.getBytes());
            outputStream.flush();

            // Ricevi i dati dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            // Continua a leggere fino a quando non arriva "END"
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Dati ricevuti: " + line); // Log dei dati ricevuti
                String[] bookData = line.split(",");  // Spezza la stringa separata da virgole
                if (bookData.length == 7) {  // Assicurati che ci siano tutti i campi
                    try {
                        Book book = new Book(
                                bookData[0],    // isbn
                                bookData[1],    // titolo
                                bookData[2],    // genere
                                bookData[3],    // imageUrl
                                bookData[4],    // autore
                                Integer.parseInt(bookData[5]),  // quantita
                                Integer.parseInt(bookData[6])   // copiePrestate
                        );
                        filteredBooks.add(book);  // Aggiungi il libro alla lista
                    } catch (NumberFormatException e) {
                        System.err.println("Errore durante il parsing dei dati del libro: " + e.getMessage());
                    }
                } else {
                    System.err.println("Dati libro non corretti: " + line);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ora stampo la lista");
        System.out.println(filteredBooks.toString());
        return filteredBooks;  // Restituisci la lista di libri
    }

    public int getNLease(String type, String username) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);



            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String numLease = type + ":" + username + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(numLease.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(serverResponse.equals("Errore durante l'esecuzione della query")){
            System.err.println("Errore esecuzione richiesta");
        }else {

            return Integer.valueOf(serverResponse); // Restituisci la risposta

        }
        return 0;
    }

    public ArrayList<Book> getBagBooks(String type, String username) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Book> books = new ArrayList<>();

        try {
            // Test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: " + type);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Imposta un timeout per la lettura dal server (5000 ms = 5 secondi)
            socket.setSoTimeout(5000);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String getbooks = type + ":" + username + ":\n";  // Indica il tipo di richiesta
            OutputStream os = socket.getOutputStream();
            os.write(getbooks.getBytes());
            outputStream.flush();

            // Ricevi i dati dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            // Continua a leggere fino a quando non arriva "END"
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Dati ricevuti: " + line); // Log dei dati ricevuti
                String[] bookData = line.split(",");  // Spezza la stringa separata da virgole
                if (bookData.length == 7) {  // Assicurati che ci siano tutti i campi
                    try {
                        Book book = new Book(
                                bookData[0],    // isbn
                                bookData[1],    // titolo
                                bookData[2],    // genere
                                bookData[3],    // imageUrl
                                bookData[4],    // autore
                                Integer.parseInt(bookData[5]),  // quantita
                                Integer.parseInt(bookData[6])   // copiePrestate
                        );
                        books.add(book);  // Aggiungi il libro alla lista
                    } catch (NumberFormatException e) {
                        System.err.println("Errore durante il parsing dei dati del libro: " + e.getMessage());
                    }
                } else {
                    System.err.println("Dati libro non corretti: " + line);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ora stampo la lista");
        System.out.println(books.toString());
        return books;  // Restituisci la lista di libri
    }

    public String bookTODB(String type, String username, String isbn) {


        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String credentials = type + ":" + username + ":" + isbn + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(credentials.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serverResponse; // Restituisci la risposta


    }

    public ArrayList<Loans> getUserLoans(String type, String username) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Loans> loansList = new ArrayList<>();

        try {
            // Test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: " + type);
            System.out.println("Username: " + username);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia la richiesta al server con il tipo e lo username
            outputStream = new DataOutputStream(socket.getOutputStream());
            String request = type + ":" + username + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes());
            outputStream.flush();

            // Ricevi la risposta dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                String[] loanData = line.split(",");
                if (loanData.length == 7) {  // Assumiamo 7 campi (isbn, data inizio, data fine, stato, titolo, genere, immagine)
                    Loans loan = new Loans();
                    loan.setBook(new Book()); // Inizializza un nuovo oggetto Book qui
                    loan.setUser(new User()); // Assicurati di inizializzare anche l'oggetto User

                    // Parsing dei campi
                    loan.getBook().setIsbn(loanData[0]);

                    // Parsing delle date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loan.setStartDate(sdf.parse(loanData[1]));
                    loan.setDueDate(sdf.parse(loanData[2]));

                    loan.setStatus(loanData[3]);
                    loan.getBook().setTitle(loanData[4]);
                    loan.getBook().setGenre(loanData[5]);
                    loan.getBook().setImageUrl(loanData[6]);

                    loan.getUser().setUsername(username); // Imposta lo username

                    // Aggiungi il prestito alla lista
                    loansList.add(loan);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // Chiudi tutte le connessioni
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loansList;

    }

    public ArrayList<Loans> getUserLoansByState(String type, String username, String stato) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Loans> loansList = new ArrayList<>();

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);


            // Invia la richiesta al server con il tipo e lo username
            outputStream = new DataOutputStream(socket.getOutputStream());
            String request = type + ":" + username + ":" + stato + ":\n";  // Richiesta al server
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes());
            outputStream.flush();

            // Ricevi la risposta dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                String[] loanData = line.split(",");
                if (loanData.length == 7) {  // Assumiamo 7 campi (isbn, data inizio, data fine, stato, titolo, genere, immagine)
                    Loans loan = new Loans();

                    // Parsing dei campi
                    loan.getBook().setIsbn(loanData[0]);

                    // Parsing delle date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loan.setStartDate(sdf.parse(loanData[1]));
                    loan.setDueDate(sdf.parse(loanData[2]));

                    loan.setStatus(loanData[3]);
                    loan.getBook().setTitle(loanData[4]);
                    loan.getBook().setGenre(loanData[5]);
                    loan.getBook().setImageUrl(loanData[6]);

                    loan.getUser().setUsername(username); // Imposta lo username

                    // Aggiungi il prestito alla lista
                    loansList.add(loan);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // Chiudi tutte le connessioni
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loansList;
    }

    public ArrayList<Loans> getBookLoans(String type, String isbn) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Loans> loansList = new ArrayList<>();

        try {

            // Log per verificare la connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Tipo richiesta: " + type);
            System.out.println("ISBN: " + isbn);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connessione al server riuscita!");

            // Invia la richiesta al server con il tipo e lo username
            outputStream = new DataOutputStream(socket.getOutputStream());
            String request = type + ":" + isbn + ":\n";  // Richiesta al server
            System.out.println("Inviando richiesta: " + request);
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes());
            outputStream.flush();
            System.out.println("Richiesta inviata con successo!");

            // Ricevi la risposta dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Risposta ricevuta: " + line);
                String[] loanData = line.split(",");
                if (loanData.length == 4) {  // Assumiamo 5 campi (username, libro, data inizio, data fine, stato)
                    Loans loan = new Loans();

                    loan.setUser(new User(loanData[0]));

                    // Parsing delle date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loan.setStartDate(sdf.parse(loanData[1]));
                    loan.setDueDate(sdf.parse(loanData[2]));

                    loan.setStatus(loanData[3]);
                    loan.setStatus(loanData[3]);

                    loansList.add(loan);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // Chiudi tutte le connessioni
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loansList;
    }





    public ArrayList<Loans> getBookLoansDelaied(String type) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        ArrayList<Loans> loansList = new ArrayList<>();

        try {

            // Log per verificare la connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Tipo richiesta: " + type);

            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connessione al server riuscita!");

            // Invia la richiesta al server con il tipo e lo username
            outputStream = new DataOutputStream(socket.getOutputStream());
            String request = "overdueloans:\n";  // Richiesta al server
            System.out.println("Inviando richiesta: " + request);
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes());
            outputStream.flush();
            System.out.println("Richiesta inviata con successo!");

            // Ricevi la risposta dal server
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = inputStream.readLine()) != null && !line.equals("END")) {
                System.out.println("Risposta ricevuta: " + line);
                String[] loanData = line.split(",");
                if (loanData.length == 4) {  // Assumiamo 5 campi (username, libro, data inizio, data fine, stato)
                    Loans loan = new Loans();

                    loan.setUser(new User(loanData[0]));

                    // Parsing delle date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loan.setStartDate(sdf.parse(loanData[1]));
                    loan.setDueDate(sdf.parse(loanData[2]));

                    loan.setStatus(loanData[3]);
                    loan.setStatus(loanData[3]);

                    loansList.add(loan);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // Chiudi tutte le connessioni
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loansList;
    }

    //socket per consegnare libro
    public String returnBook(String type, String isbn, String username) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Username: %s\n" + username);
            System.out.println("Isbn: %s\n" + isbn);


            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String credentials = type + ":" + isbn + ":" + username + "\n";
            OutputStream os = socket.getOutputStream();
            os.write(credentials.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serverResponse; // Restituisci la risposta
    }

    public int nMaxPrestiti(String type) {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);



            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String numLease = type + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(numLease.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(serverResponse.equals("Errore durante l'esecuzione della query")){
            System.err.println("Errore esecuzione richiesta");
        }else {

            return Integer.parseInt(serverResponse); // Restituisci la risposta

        }
        return 0;
    }

    public String editMaxPrestiti(String type, CharSequence value) {

        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String serverResponse = "";

        try {

            //test connessione
            System.out.println("Tentativo di connessione a " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Request type: %s\n" + type);
            System.out.println("Nuovo valore:\n" + value);



            // Connessione al server
            socket = new Socket(SERVER_IP, SERVER_PORT);

            // Invia i dati al server
            outputStream = new DataOutputStream(socket.getOutputStream());
            String numLease = type + ":" + value + ":\n";
            OutputStream os = socket.getOutputStream();
            os.write(numLease.getBytes());
            outputStream.flush();

            // Ricevi risposta dal server
            inputStream = new DataInputStream(socket.getInputStream());
            serverResponse = inputStream.readLine();
            System.out.println("Risposta dal server: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = "Errore nella comunicazione con il server"; // Messaggio di errore
        } finally {
            // Chiudi le risorse
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serverResponse; // Restituisci la risposta

    }
}
