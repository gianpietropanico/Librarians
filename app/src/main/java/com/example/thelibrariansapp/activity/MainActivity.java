package com.example.thelibrariansapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrariansapp.R;
import com.example.thelibrariansapp.utils.SocketClient;

public class MainActivity extends ImmersiveActivity {

    private EditText usernameEditText, passwordEditText;
    private TextView registratiLbl;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registratiLbl = findViewById(R.id.registratiLbl);

        // Imposta il listener per il clic su "Registrati"
        registratiLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Listener per il login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Controllo campi vuoti
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Inserisci username e password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Controllo credenziali admin
                if (username.equals("admin@admin.com") && password.equals("admin")) {
                    // Naviga alla HomePageActivity per l'admin
                    Intent intent1 = new Intent(MainActivity.this, HomePageActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    // Memorizza l'username usando SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();

                    // Invia le credenziali al server in un nuovo thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketClient client = new SocketClient();
                            String response = client.sendCredentials("login", username, password);

                            runOnUiThread(() -> {
                                if ("Login avvenuto con successo!".equals(response)) {
                                    // Naviga alla HomeActivity per l'utente normale
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Gestisce l'errore
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}
