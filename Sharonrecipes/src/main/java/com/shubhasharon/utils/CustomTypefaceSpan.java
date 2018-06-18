package com.shubhasharon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.shubhasharon.R;

/**
 * Created by wail babou on 2016-12-24.
 */
@SuppressLint("ParcelCreator")
public class CustomTypefaceSpan extends TypefaceSpan {
    Context context;

    private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface type, Context context) {
        super(family);
        newType = type;
        this.context=context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType,context);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType,context);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf,Context context) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }
}