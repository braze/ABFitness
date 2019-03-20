package udacity.example.com.abfitness.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import udacity.example.com.abfitness.BaseActivity;
import udacity.example.com.abfitness.R;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private ArrayList<String> mMealHelpList;
    private Context mContext;


    public ListRemoteViewsFactory(Context applicationContext) {
        this.mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        //fetch data
        if (BaseActivity.sMealHelpList != null){
            mMealHelpList = BaseActivity.sMealHelpList;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mMealHelpList == null) {
            return 0;
        }
        return mMealHelpList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mMealHelpList == null || mMealHelpList.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_lsit_view_item);

        String item = mMealHelpList.get(position);
        views.setTextViewText(R.id.widget_item_tv, String.valueOf(item));

        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_item_tv, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
