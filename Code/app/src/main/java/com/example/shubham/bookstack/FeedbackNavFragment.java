package com.example.shubham.bookstack;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Shubham on 8/23/2017.
 */

public class FeedbackNavFragment extends Fragment {
    Button b;
    EditText e1, e2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Feedback");
        return inflater.inflate(R.layout.feedback_nav_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = (Button) getActivity().findViewById(R.id.btEmail);
        e1 = (EditText) getActivity().findViewById(R.id.et_Subject);
        e2 = (EditText) getActivity().findViewById(R.id.et_Message);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "feedback@bookstack.com"));
                i1.putExtra(Intent.EXTRA_SUBJECT, e1.getText().toString());
                i1.putExtra(Intent.EXTRA_TEXT, e2.getText().toString());
                Intent i2 = Intent.createChooser(i1, "Choose Email App");
                startActivity(i2);
            }
        });
    }
}
