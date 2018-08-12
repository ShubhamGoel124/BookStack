package com.example.shubham.bookstack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

public class UserAreaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String name, number, email;
    public DrawerLayout mdrawerLayout;
    public ActionBarDrawerToggle mToggle;
    boolean isHomePage = false;
    Fragment fragment = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        if (savedInstanceState == null) {
            isHomePage = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeNavFragment()).commit();
        }

        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mdrawerLayout, R.string.open, R.string.close);

        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Do you really want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                logoutAndSaveSharedPreference();
                            }
                        })
                        .setNegativeButton("No", null).show();
                break;

            case R.id.about_us:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedScreen(int itemId) {
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.home:
                fragment = new HomeNavFragment();
                isHomePage = true;
                break;
            case R.id.account:
                fragment = new MyAccountNavFragment();
                isHomePage = false;
                break;
            case R.id.sellbook:
                fragment = new SellBookNavFragment();
                isHomePage = false;
                break;
            case R.id.postedbooks:
                fragment = new PostedBooksNavFragment();
                isHomePage = false;
                break;
            case R.id.feedback:
                fragment = new FeedbackNavFragment();
                isHomePage = false;
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            //ft.addToBackStack(null);
            ft.commit();
        }

        mdrawerLayout.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displaySelectedScreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        item.setChecked(true);
        //make this method blank
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!isHomePage) {
            isHomePage = true;
            fragment = new HomeNavFragment();
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeNavFragment()).commit();
        } else
            super.onBackPressed();
    }

    public void logoutAndSaveSharedPreference() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences("UserDetails", 0);
        SharedPreferences.Editor editor = userSharedPreference.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
