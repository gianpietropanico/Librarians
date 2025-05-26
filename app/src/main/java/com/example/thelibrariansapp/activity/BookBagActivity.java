package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.thelibrariansapp.utils.CartManager;
import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;
import com.example.thelibrariansapp.models.Book;

public class BookBagActivity extends ImmersiveActivity {

    private ImageButton backBtn;
    private Button rimuoviBtn;
    private ImageView bookCover;
    private TextView bookIsbn,bookTitolo, bookGenre, bookAuthor, bookQuantita, nondisponibileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_bag);

        // Inizializza le view
        backBtn = findViewById(R.id.backBtn);
        bookCover = findViewById(R.id.bookCover);
        bookTitolo = findViewById(R.id.bookTitle);
        bookGenre = findViewById(R.id.bookGenre);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookQuantita = findViewById(R.id.bookQuantity);
        bookIsbn = findViewById(R.id.isbnBook);
        nondisponibileText = findViewById(R.id.nondisponibileText);






        // Imposta il listener per il pulsante di ritorno
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Ottiene il libro dalla view precedente
        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("Book");

        String url = book.getImageUrl();
        System.out.println("Url immagine....");
        System.out.println(url);
        Glide.with(this)
                .load(url)
                .centerInside()
                .into(bookCover);        // Imposta i dettagli del libro nelle TextView
        bookTitolo.setText(book.getTitle());
        bookGenre.setText(book.getGenre());
        bookAuthor.setText(book.getAuthor());
        bookQuantita.setText(String.valueOf(book.getAvailable()));
        bookIsbn.setText(book.getIsbn());

        if(book.getAvailable() < 1){
            nondisponibileText.setVisibility(View.VISIBLE);
        }else{
            nondisponibileText.setVisibility(View.INVISIBLE);
        }


        // In un'Activity o Fragment dove visualizzi un singolo libro
        Button rimuoviBtn = findViewById(R.id.rimuoviBtn);

        // Inizializzazione SharedPreferences per recuperare lo username
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "default_value");

        rimuoviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni il libro corrente
                CartManager.getInstance().removeBook(book); // Rimuovi il libro dal carrello

                // Thread per rimuovere libro dal DB
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SocketClient client = new SocketClient();
                        System.out.println("Sto rimuovendo il libro " + book.getIsbn());
                        String response = client.bookTODB("rimuovidalcarrello", username, book.getIsbn());

                        // Esegui il Toast nel thread principale
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookBagActivity.this, response, Toast.LENGTH_SHORT).show();

                                // Torna alla CarrelloActivity e aggiorna i libri
                                Intent intent = new Intent(BookBagActivity.this, CarrelloActivity.class);
                                intent.putExtra("updated", true); // Passa un extra per indicare che il carrello è aggiornato
                                startActivity(intent);
                                finish(); // Termina BookBagActivity
                            }
                        });
                    }
                }).start();
            }
        });




    }
}