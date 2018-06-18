package com.shubhasharon.modals;

/**
 * Created by mac on 2/16/18.
 */

public class Category {
    private String category_title;
    private String category_id;
    private int color;
    private String category_posts_count;

    public Category(){}
    public Category(String category_title, String category_id,int color) {
        this.category_title = category_title;
        this.category_id = category_id;
        this.color=color;
    }

    public String getCategory_posts_count() {
        return category_posts_count;
    }

    public void setCategory_posts_count(String category_posts_count) {
        this.category_posts_count = category_posts_count;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
