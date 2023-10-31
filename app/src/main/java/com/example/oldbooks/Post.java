package com.example.oldbooks;

import java.util.List;

public class Post {

    private String postId;
    private String bookName;
    private Enums.BookCategory bookCategory;
    private String location;
    private int price;
    private String date;
    private List<String> imageURLs;
    private String author;
    private String description;
    private Enums.BookCondition bookCondition;

    private boolean contactVisible;

    public Post() {
    }

    public Post(String postId, String bookName, Enums.BookCategory bookCategory, String location, int price, String date, List<String> imageURLs, String author, String description, Enums.BookCondition bookCondition, boolean contactVisible) {
        this.postId = postId;
        this.bookName = bookName;
        this.bookCategory = bookCategory;
        this.location = location;
        this.price = price;
        this.date = date;
        this.imageURLs = imageURLs;
        this.author = author;
        this.description = description;
        this.bookCondition = bookCondition;
        this.contactVisible = contactVisible;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Enums.BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(Enums.BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enums.BookCondition getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(Enums.BookCondition bookCondition) {
        this.bookCondition = bookCondition;
    }

    public boolean isContactVisible() {
        return contactVisible;
    }

    public void setContactVisible(boolean contactVisible) {
        this.contactVisible = contactVisible;
    }
}
