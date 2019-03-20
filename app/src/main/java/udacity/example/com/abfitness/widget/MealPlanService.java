package udacity.example.com.abfitness.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import udacity.example.com.abfitness.R;


public class MealPlanService extends IntentService {

    public static final String ACTION_UPDATE_BAKE_WIDGETS = "udacity.example.com.abfitness.action.update_meal_plan_widgets";

    public MealPlanService() {
        super("MealPlanService");
    }

    public static void startActionSetWidgetMealPlanHelp(Context context) {
        Intent intent = new Intent(context, MealPlanService.class);
        intent.setAction(ACTION_UPDATE_BAKE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_BAKE_WIDGETS.equals(action)) {
                handleActionUpdateMealPlanWidget();
            }
        }
    }

    private void handleActionUpdateMealPlanWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MealPlanWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        MealPlanWidgetProvider.updateMealPlanWidgets(this, appWidgetManager, appWidgetIds);

    }

    public static void startActionUpdateMealPlanHelpListWidget(Context context) {
        Intent intent = new Intent(context, MealPlanService.class);
        intent.setAction(ACTION_UPDATE_BAKE_WIDGETS);
        context.startService(intent);
    }
}
