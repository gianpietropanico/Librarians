package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thelibrariansapp.adapters.LoansAdapter;
import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.models.Book;
import com.example.thelibrariansapp.utils.SocketClient;
import com.example.thelibrariansapp.models.Loans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageBooksActivity extends ImmersiveActivity {

    private RecyclerView loansRecyclerView;
    private LoansAdapter loansAdapter;
    private List<Loans> loansList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String username;
    private SocketClient socketClient = new SocketClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);


        // Inizializza la RecyclerView
        loansRecyclerView = findViewById(R.id.recyclerViewLoans);
        loansRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Recupera lo username dalle SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "default_value");

        // Carica i prestiti dell'utente
        loadLoans();

        //bottom menu
        ImageButton homeButton = findViewById(R.id.imgBtnHome);
        ImageButton carrelloButton = findViewById(R.id.imgBtnCarrello);
        ImageButton profiloButton = findViewById(R.id.imgBtnProfile);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        carrelloButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CarrelloActivity.class);
            startActivity(intent);
        });

        profiloButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfiloActivity.class);
            startActivity(intent);
        });

        ImageView ButtonBack = findViewById(R.id.backButton);
        ButtonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageBooksActivity.this, ProfiloActivity.class);
                startActivity(intent);
            }
        });

    }





        private void loadLoans (){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SocketClient client = new SocketClient();
                    loansList = client.getUserLoans("userloans", username);  // Passa il tipo di prestito e l'username

                    // Aggiorna l'interfaccia utente nel thread principale
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loansAdapter = new LoansAdapter(loansList, ManageBooksActivity.this);
                            loansAdapter.sortLoansList(); // Ordina i prestiti
                            loansRecyclerView.setAdapter(loansAdapter); // Imposta l'adapter alla RecyclerView
                        }
                    });
                }
            }).start();

        }


    }
