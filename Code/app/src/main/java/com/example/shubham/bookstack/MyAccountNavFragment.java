package com.example.shubham.bookstack;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Shubham on 8/23/2017.
 */


public class MyAccountNavFragment extends Fragment {
    TextView t1, t2, t3;
    String name, number, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.my_account_nav_fragment, container, false);
        getActivity().setTitle("My Account");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        t1 = (TextView) getActivity().findViewById(R.id.tv_name);
        t2 = (TextView) getActivity().findViewById(R.id.tv_number);
        t3 = (TextView) getActivity().findViewById(R.id.tv_email);

        SharedPreferences userSharedPreference = getActivity().getSharedPreferences("UserDetails", 0);
        name = userSharedPreference.getString("name", "User");
        number = userSharedPreference.getString("number", "0123456789");
        email = userSharedPreference.getString("email", "hello@bookstack.com");

        t1.setText(name);
        t2.setText(number);
        t3.setText(email);

    }
}
