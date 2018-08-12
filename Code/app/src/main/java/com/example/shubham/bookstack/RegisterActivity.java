package com.example.shubham.bookstack;

import android.app.AlertDialog;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etNumber, etPassword, etRePassword;
    public String name, email, number, password, repassword;
    Button registerButton;
    ProgressDialog progress;
    TextView t1;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        etName = (EditText) findViewById(R.id.name);
        etEmail = (EditText) findViewById(R.id.email);
        etNumber = (EditText) findViewById(R.id.number);
        etPassword = (EditText) findViewById(R.id.password);
        etRePassword = (EditText) findViewById(R.id.repassword);
        t1 = (TextView) findViewById(R.id.textView);
        registerButton = (Button) findViewById(R.id.button);

        progress = new ProgressDialog(this);
        progress.setMessage("Registering you on BookStack!");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i1);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                number = etNumber.getText().toString();
                password = etPassword.getText().toString();
                repassword = etRePassword.getText().toString();

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(RegisterActivity.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    Toast.makeText(getApplicationContext(), "Registration Successful, Login!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progress.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Registeration Failed, Please try again!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


                    RegisterRequest registerRequest = new RegisterRequest(name, email, number, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;
        if (etName.getText().toString().trim().length() < 3) {
            etName.setError("Enter valid name with atleast 3 characters");
            etName.requestFocus();
            valid = false;
        }

        if (email.matches(emailPattern) && email.trim().length() > 0) {

        } else {
            etEmail.setError("Enter Valid Email");
            etEmail.requestFocus();
            valid = false;
        }

        if (etNumber.getText().toString().trim().length() == 0 || etNumber.getText().toString().trim().length() < 10) {
            etNumber.setError("Enter valid 10 digit number");
            etNumber.requestFocus();
            valid = false;
        }

        if (password.matches(PASSWORD_PATTERN) && password.trim().length() > 0) {

        } else {
            etPassword.setError("Enter minimum 8 characters with atleast 1 number");
            etPassword.requestFocus();
            valid = false;
        }

        if (etRePassword.getText().toString().trim().length() == 0) {
            etRePassword.setError("Field is empty");
            etRePassword.requestFocus();
            valid = false;
        }

        if (password.equals(repassword)) {

        } else if (etPassword.getText().toString().trim().length() == 0) {
            etPassword.setError("Field is empty");
            etPassword.requestFocus();
            valid = false;
        } else if (etRePassword.getText().toString().trim().length() == 0) {
            etRePassword.setError("Please enter your password again");
            etRePassword.requestFocus();
            valid = false;
        } else {
            etRePassword.setError("Passwords do not match");
            etRePassword.requestFocus();
            valid = false;
        }

        return valid;
    }
}


