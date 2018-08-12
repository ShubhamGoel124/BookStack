package com.example.shubham.bookstack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    TextView t2;
    Button loginButton;
    ProgressDialog progress;
    String email, password;

    SharedPreferences userSharedPreference;
    Boolean isLoggedIn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Shared preference code
        userSharedPreference = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if (userSharedPreference.contains("isLoggedIn")) {
            isLoggedIn = userSharedPreference.getBoolean("isLoggedIn", false);
        } else {
            addSharedPreference("isLoggedIn", false);
            isLoggedIn = false;
        }

        if (isLoggedIn == true) {
            // redirect to home page
            Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
            startActivity(intent);
            finish();
        }


        getSupportActionBar().hide();

        etEmail = (EditText) findViewById(R.id.editText);
        etPassword = (EditText) findViewById(R.id.editText2);
        t2 = (TextView) findViewById(R.id.textView2);
        loginButton = (Button) findViewById(R.id.button2);

        progress = new ProgressDialog(this);
        progress.setMessage("Logging you in!");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i2);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(LoginActivity.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("No internet connection, please check your network connection!")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else if (!validate()) {

                } else {
                    progress.show();
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {

                                    // add shared preference for login
                                    addSharedPreference("isLoggedIn", true);
                                    isLoggedIn = true;

                                    String userid = jsonResponse.getString("userid");
                                    String name = jsonResponse.getString("name");
                                    String number = jsonResponse.getString("number");
                                    String email = jsonResponse.getString("email");

                                    addSharedPreference("userid", userid);
                                    addSharedPreference("name", name);
                                    addSharedPreference("number", number);
                                    addSharedPreference("email", email);


                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progress.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed, Please enter correct details!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                    etPassword.setText("");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        if (email.trim().length() == 0) {
            etEmail.setError("Field is empty");
            etEmail.requestFocus();
            valid = false;
        }

        if (password.trim().length() == 0) {
            etPassword.setError("Field is empty");
            etPassword.requestFocus();
            valid = false;
        }

        return valid;
    }

    public void addSharedPreference(String key, Boolean value) {
        SharedPreferences.Editor editor = userSharedPreference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void addSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = userSharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
