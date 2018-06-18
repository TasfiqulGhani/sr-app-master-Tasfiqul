package com.shubhasharon;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import java.net.InetAddress;



public class NoInternetChecker {
    Context c;



   public NoInternetChecker(Context c){

       this.c=c;


       if(!isNetworkConnected()){
           AlertDialog.Builder builder;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               builder = new AlertDialog.Builder(c, android.R.style.Theme_Material_Dialog_Alert);
           } else {
               builder = new AlertDialog.Builder(c);
           }
           builder.setTitle("No Internet !")
                   .setMessage("Internet is not available please connect to a network !")
                   .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           // continue with delete
                       }
                   })

                   .setIcon(android.R.drawable.ic_dialog_alert)
                   .show();
       }
    }



    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
