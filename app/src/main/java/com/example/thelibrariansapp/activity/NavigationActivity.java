package com.example.thelibrariansapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrariansapp.utils.NavigationManager;
import com.example.thelibrariansapp.R;

public class NavigationActivity extends ImmersiveActivity {

    private static final String TAG = "NavigationActivity"; // Aggiunta costante per il logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation); // Assicurati di impostare il layout corretto

        setupMenuButtons();
    }

    protected void setupMenuButtons() {
        ImageButton homeButton = findViewById(R.id.imgBtnHome);
        ImageButton carrelloButton = findViewById(R.id.imgBtnCarrello);
        ImageButton profiloButton = findViewById(R.id.imgBtnProfile);

        // Controllo null per evitare crash
        if (homeButton != null) {
            homeButton.setOnClickListener(v -> {
                Log.d(TAG, "Home button clicked");
                NavigationManager.navigateToHome(NavigationActivity.this);
            });
        }
        if (carrelloButton != null) {
            carrelloButton.setOnClickListener(v -> {
                Log.d(TAG, "Carrello button clicked");
                NavigationManager.navigateToCarrello(NavigationActivity.this);
            });
        }
        if (profiloButton != null) {
            profiloButton.setOnClickListener(v -> {
                Log.d(TAG, "Profilo button clicked");
                NavigationManager.navigateToProfile(NavigationActivity.this);
            });
        }
    }
}
