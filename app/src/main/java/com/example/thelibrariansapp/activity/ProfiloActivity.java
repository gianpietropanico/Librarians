package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;

public class ProfiloActivity extends ImmersiveActivity {

    private ImageButton homeButton;
    private ImageButton carrelloButton;
    private ImageButton profiloButton;
    private SharedPreferences sharedPreferences;
    private String username;
    private TextView maxLoansTextView;
    private TextView LoansAttualiTextView;
    SocketClient socketClient = new SocketClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profilo);

        // Recupera lo username dalle SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "default_value");
        TextView usernameTextView = findViewById(R.id.usernameText);
        usernameTextView.setText(username);

        // Button per la gestione dei libri
        Button butt = findViewById(R.id.button);
        butt.setOnClickListener(v -> {
            Intent intent = new Intent(ProfiloActivity.this, ManageBooksActivity.class);
            startActivity(intent);
        });

        // Tasto di uscita
        Button ButtonEsci = findViewById(R.id.esciButton);
        ButtonEsci.setOnClickListener(v -> {
            Intent intent = new Intent(ProfiloActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Bottom menu
        homeButton = findViewById(R.id.imgBtnHome);
        carrelloButton = findViewById(R.id.imgBtnCarrello);
        profiloButton = findViewById(R.id.imgBtnProfile);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfiloActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        carrelloButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfiloActivity.this, CarrelloActivity.class);
            startActivity(intent);
        });

        profiloButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfiloActivity.this, ProfiloActivity.class);
            startActivity(intent);
        });

        // Inizializzazione dei TextView
        maxLoansTextView = findViewById(R.id.showMaxPrestitiTV);
        LoansAttualiTextView = findViewById(R.id.showNumPrestitiTV);

        // Chiamata per ottenere i dati dei prestiti
        getNumberUserLoans();
        getMaxPrestiti();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNumberUserLoans();
        getMaxPrestiti();
    }

    // Metodo per ottenere il numero massimo di prestiti
    private void getMaxPrestiti() {
        new Thread(() -> {
            String response = String.valueOf(socketClient.nMaxPrestiti("getmaxprestiti"));
            runOnUiThread(() -> maxLoansTextView.setText(response));
        }).start();
    }

    // Metodo per ottenere il numero di prestiti dell'utente
    private void getNumberUserLoans() {
        new Thread(() -> {
            String response = String.valueOf(socketClient.getNLease("numprestiti", username));
            runOnUiThread(() -> LoansAttualiTextView.setText(response));
        }).start();
    }
}
