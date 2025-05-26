package com.example.thelibrariansapp.activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.adapters.RecommendedBookAdapter;
import com.example.thelibrariansapp.utils.SocketClient;
import com.example.thelibrariansapp.models.Book;

import java.util.ArrayList;

public class HomePageActivity extends ImmersiveActivity {

    private RecyclerView.Adapter adapterRecommended;
    private RecyclerView recyclerViewBooks;
    private ImageButton exit;
    private EditText searchbar;
    private ArrayList<Book> ItemsBooks = new ArrayList<>();

    private SocketClient socketClient = new SocketClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhomepage);

        Button kButton = findViewById(R.id.kButton);
        kButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, MaxLoansActivity.class);
                startActivity(intent);
            }
        });


        // Thread per ottenere i libri dal server
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Usa la variabile membro ItemsBooks
                ItemsBooks = socketClient.getAllBooks("allbooks");

                // Aggiorna l'interfaccia utente
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ItemsBooks != null && !ItemsBooks.isEmpty()) {
                            // Aggiungi elementi alla RecyclerView
                            initRecyclerview(ItemsBooks);
                        } else {
                            Toast.makeText(HomePageActivity.this, "Nessun libro trovato", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

        // Exit button
        exit = findViewById(R.id.Exit_button);
        exit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
            builder.setTitle("Attenzione");
            builder.setMessage("Sei sicuro di voler uscire?");
            builder.setPositiveButton("Si", (dialog, which) -> {
                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        // Search bar
        searchbar = findViewById(R.id.Searchbar);
        // Detect touch on drawableEnd (the clear icon)
        searchbar.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                // Check if the touch was on the drawableEnd (right side of the EditText)
                if (event.getRawX() >= (searchbar.getRight() - searchbar.getCompoundDrawables()[2].getBounds().width())) {
                    // Clear the text
                    searchbar.setText("");
                    return true;
                }

                // Check if the touch was on the drawableStart
                if (event.getRawX() <= (searchbar.getLeft() + searchbar.getCompoundDrawables()[0].getBounds().width())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Book> BookIsbn = socketClient.getBooksByIsbn(searchbar.getText().toString());

                            // Aggiorna l'interfaccia utente
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (BookIsbn != null && !BookIsbn.isEmpty()) {
                                        Intent intent = new Intent(HomePageActivity.this, BookLoans.class);
                                        intent.putExtra("object", BookIsbn.get(0));
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                                        builder.setTitle("Attenzione");
                                        builder.setMessage("Nessun libro trovato con questo ISBN");
                                        builder.setPositiveButton("Ok", (dialog, which) -> searchbar.setText(""));
                                        builder.show();
                                    }
                                }
                            });
                        }
                    }).start();

                    return true;
                }
            }
            return false;
        });
    }

    private void initRecyclerview(ArrayList<Book> itemsBooks) {
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterRecommended = new RecommendedBookAdapter(itemsBooks);
        recyclerViewBooks.setAdapter(adapterRecommended);
    }


}
