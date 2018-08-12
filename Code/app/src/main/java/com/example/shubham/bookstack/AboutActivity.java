package com.example.shubham.bookstack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    Animation anim1, anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        moveViewToScreenCenter(findViewById(R.id.names));
        moveIcon(findViewById(R.id.imageViewAbout));
    }

    private void moveIcon(View view) {
        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        anim2 = new TranslateAnimation(0, 0, 0, originalPos[1] + 100);
        anim2.setDuration(2000);
        anim2.setFillAfter(true);
        view.startAnimation(anim2);
    }

    private void moveViewToScreenCenter(View view) {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.ctr);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        int yDest = dm.heightPixels / 2 - (view.getMeasuredHeight() / 2) - statusBarOffset;

        anim1 = new TranslateAnimation(0, 0, 0, yDest - originalPos[1] + 200);
        anim1.setDuration(1500);
        anim1.setFillAfter(true);
        view.startAnimation(anim1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
