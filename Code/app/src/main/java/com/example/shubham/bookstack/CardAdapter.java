package com.example.shubham.bookstack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Shubham on 9/9/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;
    private ListItems item = new ListItems();
    //List to store all Book items
    List<ListItems> listItems;

    //Constructor of this class
    public CardAdapter(List<ListItems> listItems, Context context) {
        super();

        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        ListItems listItem = listItems.get(position);
        // Global item
        Log.i("postedBy", listItem.getPostedBy() + "," + listItem.getBookName());
        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(listItem.getImageUrl(), ImageLoader.getImageListener(holder.imageViewBook, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageViewBook.setImageUrl(listItem.getImageUrl(), imageLoader);
        holder.textViewBookName.setText(listItem.getBookName());
        holder.textViewCost.setText(listItem.getBookCost());
        holder.textViewCategory.setText(listItem.getBookCategory());
        holder.position = position;

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView imageViewBook;
        public TextView textViewBookName, textViewCost, textViewDescription, textViewCategory, textViewYourName, textViewEmail, textViewNumber;
        public int position;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewBook = (NetworkImageView) itemView.findViewById(R.id.imageViewBook);
            textViewBookName = (TextView) itemView.findViewById(R.id.textViewBookName);
            textViewCost = (TextView) itemView.findViewById(R.id.textViewCost);
            textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intentBookInfoActivity = new Intent(context, BookInfoActivity.class);
                    intentBookInfoActivity.putExtra("image", listItems.get(position).getImageUrl());
                    intentBookInfoActivity.putExtra("book_name", textViewBookName.getText().toString());
                    intentBookInfoActivity.putExtra("book_cost", textViewCost.getText().toString());
                    intentBookInfoActivity.putExtra("book_category", textViewCategory.getText().toString());
                    intentBookInfoActivity.putExtra("description", listItems.get(position).getBookDescription());
                    intentBookInfoActivity.putExtra("posted_by", listItems.get(position).getPostedBy());

                    context.startActivity(intentBookInfoActivity);
                }
            });
        }
    }


}
