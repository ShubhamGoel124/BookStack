package com.example.shubham.bookstack;

/**
 * Created by Shubham on 9/9/2017.
 */

public class ListItems {
    //Data Variables
    private String imageUrl;
    private String bookName;
    private String bookCost;
    private String bookDescription;
    private String bookCategory;
    private String postedBy;

    //Getters and Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCost() {
        return bookCost;
    }

    public void setBookCost(String bookCost) {
        this.bookCost = bookCost;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
