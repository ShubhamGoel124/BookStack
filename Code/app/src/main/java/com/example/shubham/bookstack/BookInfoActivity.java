package com.example.shubham.bookstack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class BookInfoActivity extends AppCompatActivity {

    TextView bookName, bookCost, bookCategory, bookDescription, userName, userNumber, userEmail;
    ProgressBar imageProgressBar;
    ImageView bookImage;
    String postedByValue;
    Button btnCallSeller, btnEmailSeller;

    Bitmap bitmap;

    String Fetch_URL = "http://restricting-writer.000webhostapp.com/Fetch_User_Details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookName = (TextView) findViewById(R.id.textView8);
        bookCost = (TextView) findViewById(R.id.textView9);
        bookCategory = (TextView) findViewById(R.id.textView10);
        bookDescription = (TextView) findViewById(R.id.textView11);
        userName = (TextView) findViewById(R.id.textView12);
        userNumber = (TextView) findViewById(R.id.textView13);
        userEmail = (TextView) findViewById(R.id.textView14);

        btnCallSeller = (Button) findViewById(R.id.btnCallSeller);
        btnEmailSeller = (Button) findViewById(R.id.btnEmailSeller);

        bookImage = (ImageView) findViewById(R.id.bookImage);

        imageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);

        String book_name = null;
        String book_cost = null;
        String book_category = null;
        String description = null;
        String image_URL = null;

        Intent cardAdapterData = getIntent();
        image_URL = cardAdapterData.getStringExtra("image");
        book_name = cardAdapterData.getStringExtra("book_name");
        book_cost = cardAdapterData.getStringExtra("book_cost");
        book_category = cardAdapterData.getStringExtra("book_category");
        description = cardAdapterData.getStringExtra("description");
        postedByValue = cardAdapterData.getStringExtra("posted_by");

        Glide.with(this)
                .load(image_URL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .signature(new StringSignature(image_URL))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(bookImage);

        bookName.setText(book_name);
        bookCost.setText(book_cost);
        bookCategory.setText(book_category);
        bookDescription.setText(description);

        fetchDetails();

        btnCallSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + userNumber.getText()));
                startActivity(callIntent);
            }
        });

        btnEmailSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + userEmail.getText()));
                Intent i2 = Intent.createChooser(i1, "Choose Email App");
                startActivity(i2);
            }
        });

        bookImage.buildDrawingCache();
        bitmap = bookImage.getDrawingCache();

        final String finalImage_URL = image_URL;
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookInfoActivity.this, LargeImageActivity.class);
                intent.putExtra("imageURL", finalImage_URL);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchDetails() {
        Log.d("BookInfoActivity", "inside fetchDetails() ");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Fetch_URL,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("BookInfoActivity", "inside onResponse() ");


                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String name = jsonResponse.getString("name");
                            userName.setText(name);
                            String number = jsonResponse.getString("number");
                            userNumber.setText(number);
                            String email = jsonResponse.getString("email");
                            userEmail.setText(email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("BookInfoActivity", "inside getParams() postedByValue " + postedByValue);

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                //Toast.makeText(getApplicationContext(), " userid " + postedByValue, Toast.LENGTH_LONG).show();
                params.put("userid", postedByValue);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void logoutAndSaveSharedPreference() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences("UserDetails", 0);
        SharedPreferences.Editor editor = userSharedPreference.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(BookInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}