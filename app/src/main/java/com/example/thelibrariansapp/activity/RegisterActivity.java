package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends ImmersiveActivity {

    private EditText usernameEditText, passwordEditText, passwordEditText2;
    private Button registraBtn;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        passwordEditText2 = findViewById(R.id.password2);
        registraBtn = findViewById(R.id.registraBtn);
        backBtn = findViewById(R.id.backBtn);

        // Imposta il listener per il pulsante di ritorno
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();
                String enteredPassword2 = passwordEditText2.getText().toString();

                // Verifica che la password sia una combinazione di almeno 4 caratteri e almeno 1 numero
                Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$");
                Matcher matcher = pattern.matcher(enteredPassword);

                if (!matcher.matches()) {
                    Toast.makeText(RegisterActivity.this, "La password deve essere una combinazione di caratteri e numeri", Toast.LENGTH_SHORT).show();
                } else if (enteredUsername.length() < 4 || enteredPassword.length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Username e password devono essere almeno di 4 caratteri", Toast.LENGTH_SHORT).show();
                } else if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else if (!enteredPassword.equals(enteredPassword2)) {
                    Toast.makeText(RegisterActivity.this, "Le password non coincidono", Toast.LENGTH_SHORT).show();
                } else {
                    // Invia le credenziali al server
                    sendCredentialsToServer("register", enteredUsername, enteredPassword);
                }
            }
        });
    }

    private void sendCredentialsToServer(final String type, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketClient client = new SocketClient();
                    String response = client.sendCredentials(type, username, password);

                    // Gestisci la risposta nel thread principale
                    runOnUiThread(() -> {
                        if ("Registrazione completata con successo!".equals(response)) {
                            // Naviga a HomeActivity
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Gestisci l'errore
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Errore nella comunicazione con il server", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }


}
