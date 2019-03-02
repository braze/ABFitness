package udacity.example.com.abfitness.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import udacity.example.com.abfitness.R;

public class ColorUtils {

    public static int getViewHolderBackgroundColorFromInstance(Context context, int instanceNum) {
        switch (instanceNum) {
            case 0:
                return ContextCompat.getColor(context, R.color.red);
            case 1:
                return ContextCompat.getColor(context, R.color.orange);
            case 2:
                return ContextCompat.getColor(context, R.color.yellow);
            case 3:
                return ContextCompat.getColor(context, R.color.green);
            case 4:
                return ContextCompat.getColor(context, R.color.lightBlue);
            case 5:
                return ContextCompat.getColor(context, R.color.blue);
            case 6:
                return ContextCompat.getColor(context, R.color.purple);
            default:
                return Color.WHITE;
        }
    }
}