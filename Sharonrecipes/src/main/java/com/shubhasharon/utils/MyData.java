package com.shubhasharon.utils;

import com.shubhasharon.R;
import com.shubhasharon.modals.Category;

import java.util.ArrayList;

/**
 * Created by mac on 2/16/18.
 */

public class MyData {
    public static boolean isSet=false;
    public static ArrayList<Category> categories = new ArrayList<>();
    public static void addCategories(){
        if(!isSet){
            categories.add(new Category("ALL", "", R.color.classColor1));
            categories.add(new Category("Breakefast", "178",R.color.classColor3));
            categories.add(new Category("Lunch","109",R.color.classColor2));
            categories.add(new Category("Non veg","140",R.color.classColor4));
            categories.add(new Category("Rice","107",R.color.classColor4));
            categories.add(new Category("Curry","335",R.color.classColor3));
            categories.add(new Category("Roti","336",R.color.classColor1));
            categories.add(new Category("Sweets","23",R.color.classColor2));
            categories.add(new Category("Snacks","132",R.color.classColor3));



        }
    }
}
