package com.example.ok.task.Fonts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by ok on 01/11/2016.
 */

public class TypefaceUtil extends AppCompatActivity {


    public static void overrideFonts(final Context context, final View v) {
        float width = 0, height = 0;
        double diagonalInches = 0;
        TelephonyManager manager;
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.heightPixels / metrics.ydpi;
            height = metrics.widthPixels / metrics.xdpi;
            diagonalInches = Math.sqrt(height * height + width * width);

            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/DroidKufi-Bold.ttf"));
                if (diagonalInches>=6.5) {
                    //code for big screen (like tablet)
                    ((TextView) v).setTextSize(30);
                }

            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/DroidKufi-Bold.ttf"));
                if (diagonalInches>=6.5) {
                    //code for big screen (like tablet)
                    ((EditText) v).setTextSize(30);
                }

            } else if (v instanceof Button) {
                ((Button) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/DroidKufi-Bold.ttf"));

                if (diagonalInches>=6.5) {
                    //code for big screen (like tablet)
                    ((Button) v).setTextSize(30);
                }
            }else if (v instanceof ImageView) {


                if (diagonalInches>=6.5) {
                    //code for big screen (like tablet)
                    ((ImageView) v).getLayoutParams().height = 36;
                    ((ImageView) v).getLayoutParams().width = 36;
                }
            }else if (v instanceof android.support.v7.widget.SwitchCompat) {


                if (diagonalInches>=6.5) {
                    //code for big screen (like tablet)
                    ((android.support.v7.widget.SwitchCompat) v).getLayoutParams().width = 50;
                }
            }

        } catch (Exception e) {
        }
    }




}
