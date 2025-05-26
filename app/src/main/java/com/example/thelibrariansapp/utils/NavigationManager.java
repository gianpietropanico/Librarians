package com.example.thelibrariansapp.utils;

import android.content.Context;
import android.content.Intent;

import com.example.thelibrariansapp.activity.CarrelloActivity;
import com.example.thelibrariansapp.activity.HomeActivity;
import com.example.thelibrariansapp.activity.ProfiloActivity;

public class NavigationManager {


    public static void navigateToHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
    public static void navigateToCarrello(Context context) {
        Intent intent = new Intent(context, CarrelloActivity.class);
        context.startActivity(intent);
    }
    public static void navigateToProfile(Context context) {
        Intent intent = new Intent(context, ProfiloActivity.class);
        context.startActivity(intent);
    }
}
