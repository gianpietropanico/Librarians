package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.NotProvisionedException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.thelibrariansapp.LateLoansDialogFragment;
import com.example.thelibrariansapp.NotAvaiableDialogFragment;
import com.example.thelibrariansapp.adapters.BookAdapter;
import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;
import com.example.thelibrariansapp.models.Book;
import android.view.GestureDetector;

import java.util.ArrayList;

import android.content.SharedPreferences;

public class HomeActivity extends ImmersiveActivity {

    SharedPreferences sharedPreferences;

    private MediaPlayer mediaPlayer;
    private ArrayList<Book> bookList; // Memorizza la lista di libri
    private BookAdapter bookAdapter;
    private GestureDetector gestureDetector;
    private String username; // Username dell'utente

    private ImageButton homeButton;
    private ImageButton carrelloButton;
    private ImageButton profiloButton;
    private CheckBox disponibili;
    private EditText cercaEditText;
    private Button cercaBtn, filterBtn;
    private Spinner genereSpinner;

    private RecyclerView books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        // Inizializza le view
        homeButton = findViewById(R.id.imgBtnHome);
        carrelloButton = findViewById(R.id.imgBtnCarrello);
        profiloButton = findViewById(R.id.imgBtnProfile);
        cercaEditText = findViewById(R.id.cercaEditText);
        cercaBtn = findViewById(R.id.cercaBtn);
        filterBtn = findViewById(R.id.filterBtn);
        genereSpinner = findViewById(R.id.genereSpinner);
        books = findViewById(R.id.recyclerViewLoans);
        disponibili = findViewById(R.id.checkBox);

        // Imposta il GridLayoutManager per RecyclerView con 2 colonne
        books.setLayoutManager(new GridLayoutManager(this, 2));


        // Crea un adattatore per il tuo spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.generi, // Sostituisci con il tuo array di stringhe
                R.layout.spinner_item // Usa il layout personalizzato
        );
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        genereSpinner.setAdapter(adapter);

        // Inizializzazione SharedPreferences per recuperare lo username
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "default_value");
        // Gestione della navigazione nei pulsanti
        homeButton.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        carrelloButton.setOnClickListener(v -> startActivity(new Intent(this, CarrelloActivity.class)));
        profiloButton.setOnClickListener(v -> startActivity(new Intent(this, ProfiloActivity.class)));

        mediaPlayer = MediaPlayer.create(this, R.raw.filtra_libri);

        // Ottieni la data corrente
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Ottieni la data dell'ultimo controllo da SharedPreferences
        String lastCheckedDate = sharedPreferences.getString("lastCheckedDate", "");

        // Confronta la data corrente con l'ultima data di controllo
        if (!currentDate.equals(lastCheckedDate)) {
            // Se la data è diversa, esegui checkDelay()
            checkDelay();

            // Aggiorna SharedPreferences con la nuova data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastCheckedDate", currentDate);
            editor.apply();
        }



        // Gestione visibilità filtri e cerca
        filterBtn.setOnClickListener(new View.OnClickListener() {
            boolean isVisible = false;

            @Override
            public void onClick(View v) {
                if (isVisible) {
                    cercaBtn.setVisibility(View.GONE);
                    cercaEditText.setVisibility(View.GONE);
                    genereSpinner.setVisibility(View.GONE);
                    disponibili.setVisibility(View.GONE);
                } else {
                    cercaBtn.setVisibility(View.VISIBLE);
                    cercaEditText.setVisibility(View.VISIBLE);
                    genereSpinner.setVisibility(View.VISIBLE);
                    disponibili.setVisibility(View.VISIBLE);
                }
                isVisible = !isVisible;
            }
        });

        // Carica i libri dal server
        loadBooksFromServer();

        // Imposta il listener per il pulsante di ricerca
        cercaBtn.setOnClickListener(v -> searchBooks());
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Carica i libri dal server solo se non sono già stati caricati
        if (bookList == null) {
            loadBooksFromServer();
        } else {
            // Aggiorna l'adapter se i libri sono già caricati
            bookAdapter.notifyDataSetChanged();
        }
    }



    private void checkDelay() {

        new Thread(() -> {
            SocketClient client = new SocketClient();
            String response = client.check("checkloans", username);

            runOnUiThread(() -> {
                if ("Hai dei prestiti in ritardo".equals(response)) {
                    // Eseguire azione
                    LateLoansDialogFragment dialog = new LateLoansDialogFragment();
                    dialog.show(getSupportFragmentManager(), "LateLoansDialog");

                } else {
                    Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            });

    }).start();
    }

    private void loadBooksFromServer() {
        new Thread(() -> {
            SocketClient client = new SocketClient();
            bookList = client.getAllBooks("allbooks"); // Ottieni i libri dal server

            runOnUiThread(() -> {
                if (bookList != null && !bookList.isEmpty()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                    bookAdapter = new BookAdapter(bookList, HomeActivity.this);
                    books.setAdapter(bookAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Nessun libro trovato", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void searchBooks() {
        String titolo = cercaEditText.getText().toString().trim();
        String genere = genereSpinner.getSelectedItem().toString();

        new Thread(() -> {
            SocketClient client = new SocketClient();
            String type;
            ArrayList<Book> filteredBooks = null;

            if(disponibili.isChecked()) {
                if (titolo.isEmpty() && genereSpinner.getSelectedItemPosition() == 0) {
                    // Caso 1: EditText vuoto e nessun genere selezionato
                    type = "allbooksavaiable";
                    filteredBooks = client.getFilteredBooks(type);
                } else if (!titolo.isEmpty() && genereSpinner.getSelectedItemPosition() != 0) {
                    // Caso 2: Titolo presente e genere selezionato
                    type = "totalfilteravaiable:" + titolo + ":" + genere;
                    filteredBooks = client.getFilteredBooks(type);
                } else if (!titolo.isEmpty() && genereSpinner.getSelectedItemPosition() == 0) {
                    // Caso 3: Titolo presente, ma nessun genere selezionato
                    type = "onlytitleavaiable:" + titolo;
                    filteredBooks = client.getFilteredBooks(type);
                } else if (titolo.isEmpty() && genereSpinner.getSelectedItemPosition() != 0) {
                    // Caso 4: Nessun titolo ma genere selezionato
                    type = "onlygenreavaiable:" + genere;
                    filteredBooks = client.getFilteredBooks(type);
                }
            } else {

                if (titolo.isEmpty() && genereSpinner.getSelectedItemPosition() == 0) {
                    // Caso 1: EditText vuoto e nessun genere selezionato
                    type = "allbooks";
                    filteredBooks = client.getFilteredBooks(type);
                } else if (!titolo.isEmpty() && genereSpinner.getSelectedItemPosition() != 0) {
                    // Caso 2: Titolo presente e genere selezionato
                    type = "totalfilter:" + titolo + ":" + genere;
                    filteredBooks = client.getFilteredBooks(type);
                } else if (!titolo.isEmpty() && genereSpinner.getSelectedItemPosition() == 0) {
                    // Caso 3: Titolo presente, ma nessun genere selezionato
                    type = "onlytitle:" + titolo;
                    filteredBooks = client.getFilteredBooks(type);
                } else if (titolo.isEmpty() && genereSpinner.getSelectedItemPosition() != 0) {
                    // Caso 4: Nessun titolo ma genere selezionato
                    type = "onlygenre:" + genere;
                    filteredBooks = client.getFilteredBooks(type);


                }
            }


            ArrayList<Book> finalFilteredBooks = filteredBooks;
            runOnUiThread(() -> {
                if (finalFilteredBooks != null && !finalFilteredBooks.isEmpty()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                    bookAdapter.updateBooks(finalFilteredBooks);
                } else {
                    Toast.makeText(HomeActivity.this, "Nessun libro trovato per i criteri di ricerca", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Rilascia il MediaPlayer quando l'attività viene distrutta
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
