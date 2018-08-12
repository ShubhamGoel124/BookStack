package com.example.shubham.bookstack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 8/28/2017.
 */

public class HomeNavFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<ListItems> listBooks;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        return inflater.inflate(R.layout.home_nav_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initializing Views
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        listBooks = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                getData();
            }
        });

        //initializing our adapter
        adapter = new CardAdapter(listBooks, getActivity());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        clear();
        getData();
    }

    public void clear() {
        int size = this.listBooks.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listBooks.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    private JsonArrayRequest getDataFromServer() {
        mSwipeRefreshLayout.setRefreshing(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        parseData(response);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mSwipeRefreshLayout.setRefreshing(false);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer());
    }

    //This method will parse json data
    private void parseData(JSONArray array) {

        if (array.length() == 0)
            Toast.makeText(getActivity(), "No items to load!", Toast.LENGTH_LONG).show();

        for (int i = 0; i < array.length(); i++) {

            ListItems listBook = new ListItems();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                listBook.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                listBook.setBookName(json.getString(Config.TAG_BOOK_NAME));
                listBook.setBookCost("Rs. " + json.getString(Config.TAG_COST));
                listBook.setBookDescription(json.getString(Config.TAG_DESCRIPTION));
                listBook.setBookCategory(json.getString(Config.TAG_CATEGORY));
                listBook.setPostedBy(json.getString(Config.TAG_POSTED_BY));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listBooks.add(listBook);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();

    }
}
