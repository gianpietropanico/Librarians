package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;

public class MaxLoansActivity extends AppCompatActivity {

    private int currentNumber = 0;  // Numero iniziale
    SocketClient socketClient = new SocketClient();

    // I riferimenti ai componenti devono essere inizializzati dopo setContentView()
    private Button incrementButton;
    private Button decrementButton;
    private TextView numberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_max_loans);

        // Trova i riferimenti ai componenti del layout
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);
        numberTextView = findViewById(R.id.numberTextView);

        ImageView kButton = findViewById(R.id.backBtn);
        kButton.setOnClickListener(v -> {
            Intent intent = new Intent(MaxLoansActivity.this, HomePageActivity.class);
            startActivity(intent);
        });

        // Imposta il valore iniziale
        numberTextView.setText(String.valueOf(currentNumber));

        // Listener per il pulsante di incremento
        incrementButton.setOnClickListener(v -> {
            currentNumber++;  // Incrementa il numero
            numberTextView.setText(String.valueOf(currentNumber));  // Aggiorna la TextView
        });

        // Listener per il pulsante di decremento
        decrementButton.setOnClickListener(v -> {
            if (currentNumber > 0) {
                currentNumber--;  // Decrementa il numero (se maggiore di 0)
                numberTextView.setText(String.valueOf(currentNumber));  // Aggiorna la TextView
            }
        });

        getMaxPrestiti();

        // Logica del tasto conferma
        Button confermaBtn = findViewById(R.id.confermaButton);
        confermaBtn.setOnClickListener(v -> {
            new Thread(() -> {
                String response = socketClient.editMaxPrestiti("editmaxprestiti", numberTextView.getText());
                runOnUiThread(() -> Toast.makeText(MaxLoansActivity.this, response, Toast.LENGTH_SHORT).show());
            }).start();
        });
    }

    private int getMaxPrestiti() {
        new Thread(() -> {
            String response = String.valueOf(socketClient.nMaxPrestiti("getmaxprestiti"));
            runOnUiThread(() -> numberTextView.setText(response));
        }).start();

        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMaxPrestiti();
    }
}
