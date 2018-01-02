package com.example.shubham.bookstack;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shubham on 8/22/2017.
 */

public class SellBookNavFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText etBookName, etCost, etDescription;
    String image, person_name, book_name, email, number, cost, description, category;
    ImageView imageView;
    Spinner spinner;
    Button b;
    private Bitmap bitmap;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final int REQUEST_CODE_GALLERY = 1;
    private String UPLOAD_URL = "http://restricting-writer.000webhostapp.com/Upload.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Sell Book");
        return inflater.inflate(R.layout.sell_book_nav_fragment_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etBookName = (EditText) getActivity().findViewById(R.id.et_BookName);
        etCost = (EditText) getActivity().findViewById(R.id.et_Cost);
        etDescription = (EditText) getActivity().findViewById(R.id.et_Description);
        imageView = (ImageView) getActivity().findViewById(R.id.image);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        b = (Button) getActivity().findViewById(R.id.bt_Upload);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("No internet connection, please check your network connection!")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else if (!validate()) {

                } else {
                    uploadImage();
                }
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Select Category:");
        categories.add("Fiction");
        categories.add("Non-Fiction");
        categories.add("Textbooks");
        categories.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = null;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setVisibility(View.GONE);
                    tv.setHeight(0);
                    v = tv;
                    v.setVisibility(View.GONE);
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;

        category = myText.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        loading.dismiss();

                        Fragment fragment = new SellBookNavFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);

                        Toast.makeText(getActivity(), "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        fragmentTransaction.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        loading.dismiss();

                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                image = getStringImage(bitmap);

                book_name = etBookName.getText().toString();
                cost = etCost.getText().toString();
                description = etDescription.getText().toString();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                // Getting userid from sharedpreference
                SharedPreferences userSharedPreference = getActivity().getSharedPreferences("UserDetails", 0);
                String userid = userSharedPreference.getString("userid", "user");

                //Adding parameters
                params.put("userid", userid);
                params.put("image", image);
                params.put("name", book_name);
                params.put("cost", cost);
                params.put("description", description);
                params.put("category", category);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validate() {
        boolean valid = true;


        if (etBookName.getText().toString().trim().length() == 0) {
            etBookName.setError("Field is empty!");
            etBookName.requestFocus();
            valid = false;
        }


        if (etCost.getText().toString().trim().length() == 0) {
            etCost.setError("Field is empty!");
            etCost.requestFocus();
            valid = false;
        }

        if (etDescription.getText().toString().trim().length() < 10) {
            etDescription.setError("Enter some description of the product with minimum 10 characters!");
            etDescription.requestFocus();
            valid = false;
        }

        if (spinner.getSelectedItem().toString().trim().equals("Select Category:")) {
            Toast.makeText(getActivity(), "Please select category!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.gallery_icon).getConstantState()) {
            Toast.makeText(getActivity(), "Please select an image from Gallery!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
