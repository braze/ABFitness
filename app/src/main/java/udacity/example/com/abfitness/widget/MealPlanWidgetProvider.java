package udacity.example.com.abfitness.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import udacity.example.com.abfitness.MainActivity;
import udacity.example.com.abfitness.R;

public class MealPlanWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        
        RemoteViews remoteViews = null;
        if (width < 200) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_default_image, pendingIntent);
        } else {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
            Intent intent = new Intent(context, ListWidgetService.class);

            remoteViews.setTextViewText(R.id.meal_plan_widget_header, context.getString(R.string.widget_header));
            remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);

            // Set the PlantDetailActivity intent to launch when clicked
            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

            // Handle empty gardens
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        }

        // Instruct the udacity.example.com.abfitness.widget manager to update the udacity.example.com.abfitness.widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    
    public static void updateMealPlanWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MealPlanService.startActionUpdateMealPlanHelpListWidget(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        MealPlanService.startActionUpdateMealPlanHelpListWidget(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first udacity.example.com.abfitness.widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last udacity.example.com.abfitness.widget is disabled
    }

}
