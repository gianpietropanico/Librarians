package com.example.thelibrariansapp.activity;



import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.thelibrariansapp.utils.CartManager;
import com.example.thelibrariansapp.utils.SocketClient;
import com.example.thelibrariansapp.models.Book;
import com.example.thelibrariansapp.R;

public class BookActivity extends ImmersiveActivity {

    private ImageButton backBtn;
    private Button aggiungiBtn;
    private ImageView bookCover;
    private TextView bookIsbn,bookTitolo, bookGenre, bookAuthor, bookQuantita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Inizializza le view
        backBtn = findViewById(R.id.backBtn);
        bookCover = findViewById(R.id.bookCover);
        bookTitolo = findViewById(R.id.bookTitle);
        bookGenre = findViewById(R.id.bookGenre);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookQuantita = findViewById(R.id.bookQuantity);
        bookIsbn= findViewById(R.id.isbnBook);
        aggiungiBtn = findViewById(R.id.aggiungiBtn);



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
                .into(bookCover);
        // Imposta i dettagli del libro nelle TextView
        bookTitolo.setText(book.getTitle());
        bookGenre.setText(book.getGenre());
        bookAuthor.setText(book.getAuthor());
        bookQuantita.setText(String.valueOf(book.getAvailable()));
        bookIsbn.setText(book.getIsbn());

        if (book.getAvailable() < 1){

            aggiungiBtn.setText("Non disponibile");
        } else {

            aggiungiBtn.setText("Aggiungi al carrello");
        }

        // Inizializzazione SharedPreferences per recuperare lo username
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "default_value");

        aggiungiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(book.getAvailable() > 0) {
                    // Ottieni il libro corrente
                    CartManager.getInstance().addBook(book); // Aggiungi il libro al carrello

                    // Thread per aggiungere libro al  DB
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketClient client = new SocketClient();
                            String response = client.bookTODB("aggiungialcarrello", username, book.getIsbn());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BookActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).start();
                }else {
                        Toast.makeText(BookActivity.this, "Libro terminato", Toast.LENGTH_SHORT).show();
                }



                    Intent intent = new Intent(BookActivity.this, HomeActivity.class);
                    startActivity(intent);

            }
        });



    }
}