package com.shubhasharon.SQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import com.shubhasharon.modals.Article;

import java.util.ArrayList;

/**
 * Created by wail babou on 2017-02-14.
 */

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "DBWordpress.db";
    private static final String TABLE_SAVED = "save";
    //......
    private static final String COLUMN_ARTICLE_ID = "article_id";
    private static final String COLUMN_ARTICLE_IMGURL = "article_img";
    private static final String COLUMN_ARTICLE_TITLE = "article_title";
    private static final String COLUMN_ARTICLE_DATE = "article_date";
    private static final String COLUMN_ARTICLE_INFOS = "article_info";
    private static final String COLUMN_ARTICLE_URL= "article_url";
    private static final String COLUMN_ARTICLE_WEBID = "article_id_web";



    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREAT_TABLE_SAVED="CREATE TABLE IF NOT EXISTS " +TABLE_SAVED
                +"("+COLUMN_ARTICLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ARTICLE_TITLE+" TEXT,"+COLUMN_ARTICLE_INFOS+" TEXT ,"+
                COLUMN_ARTICLE_DATE+" TEXT,"+COLUMN_ARTICLE_IMGURL+" TEXT,"+
                COLUMN_ARTICLE_WEBID+" INTEGER UNIQUE,"+
                COLUMN_ARTICLE_URL+" TEXT"+ ")";
        sqLiteDatabase.execSQL(CREAT_TABLE_SAVED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED);
        onCreate(sqLiteDatabase);
    }
    public void addArticle(Article me){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(COLUMN_ARTICLE_TITLE,me.getTitle());
        values.put(COLUMN_ARTICLE_INFOS,me.getContent());
        values.put(COLUMN_ARTICLE_DATE,me.getDate());
        values.put(COLUMN_ARTICLE_IMGURL,me.getImg_url());
        values.put(COLUMN_ARTICLE_URL,me.getArticle_url());
        values.put(COLUMN_ARTICLE_WEBID,me.getId());
        long rowInserted=db.insert(TABLE_SAVED,null,values);
        if(rowInserted != -1)
            Toast.makeText(context,"Item saved",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"This item is already saved !",Toast.LENGTH_SHORT).show();
    }
    public void deleteArticle(Article me){
        SQLiteDatabase  db =this.getReadableDatabase();
        ContentValues args = new ContentValues();
        db.delete(TABLE_SAVED,COLUMN_ARTICLE_ID+" =?",new String[]{me.getBDID()+""});
        Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show();
    }
    public ArrayList<Article> getSaved(int id){
        ArrayList<Article> articles=new ArrayList<>();
        SQLiteDatabase  db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SAVED+" where "+COLUMN_ARTICLE_ID+
                " < "+id+" order by "+COLUMN_ARTICLE_ID+" DESC LIMIT 3",null);
        while (cursor.moveToNext()){
            Article st = new Article();
            st.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_TITLE)));
            st.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_INFOS)));
            st.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_DATE)));
            st.setImg_url(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_IMGURL)));
            st.setArticle_url(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_URL)));
            st.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICLE_WEBID)));
            st.setBDID(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICLE_ID)));
            st.setSaved("saved");
            articles.add(st);
        }
        return articles;
    }
    public ArrayList<Article> getSaved(){
        ArrayList<Article> articles=new ArrayList<>();
        SQLiteDatabase  db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SAVED+" order by "+COLUMN_ARTICLE_ID+" DESC LIMIT 3",null);
        while (cursor.moveToNext()){
            Article st = new Article();
            st.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_TITLE)));
            st.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_INFOS)));
            st.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_DATE)));
            st.setImg_url(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_IMGURL)));
            st.setArticle_url(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_URL)));
            st.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICLE_WEBID)));
            st.setBDID(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICLE_ID)));
            st.setSaved("saved");
            articles.add(st);
        }
        return articles;
    }
}
