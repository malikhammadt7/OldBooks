package com.example.oldbooks;
public class Post {

    private String postId;
    private String bookName;
    private String location;
    private int price;
    private String date;
    private String[] imageURLs;
    private String author;
    private String publisher;
    private Enums.BookCondition bookCondition;

    public Post() {
    }

    public Post(String postId, String bookName, String location, int price, String date, String[] imageURLs, String author, String publisher, Enums.BookCondition bookCondition) {
        this.postId = postId;
        this.bookName = bookName;
        this.location = location;
        this.price = price;
        this.date = date;
        this.imageURLs = imageURLs;
        this.author = author;
        this.publisher = publisher;
        this.bookCondition = bookCondition;
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

    public String[] getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String[] imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Enums.BookCondition getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(Enums.BookCondition bookCondition) {
        this.bookCondition = bookCondition;
    }
}
