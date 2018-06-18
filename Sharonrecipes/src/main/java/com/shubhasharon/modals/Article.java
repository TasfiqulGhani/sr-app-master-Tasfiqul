package com.shubhasharon.modals;

import java.util.ArrayList;

/**
 * Created by wail babou on 2016-12-24.
 */

public class Article {
    private String img_url,auther,title,date,content;
    private int id, BDID,category_id;
    private String article_url,saved;
    private ArrayList<ItemComment> comments;
    public boolean isCategoryOpened = false;

    public Article(){

    }
    public Article(String img_url, String title, String auther, String date) {
        this.img_url = img_url;
        this.title = title;
        this.auther = auther;
        this.date = date;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public ArrayList<ItemComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<ItemComment> comments) {
        this.comments = comments;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getBDID() {
        return BDID;
    }

    public void setBDID(int BDID) {
        this.BDID = BDID;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
