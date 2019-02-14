package udacity.example.com.abfitness.async.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.ArrayList;

import udacity.example.com.abfitness.Utils.JsonUtils;
import udacity.example.com.abfitness.Utils.NetworkUtils;
import udacity.example.com.abfitness.interfaces.OnFetchJsonTaskCompleted;

public class FetchJsonAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private static String TAG = FetchJsonAsyncTask.class.getSimpleName();
    private OnFetchJsonTaskCompleted mTaskCompleted;
    private SharedPreferences mPreferences;


    public FetchJsonAsyncTask(OnFetchJsonTaskCompleted activityContext, SharedPreferences preferences) {
        this.mTaskCompleted = activityContext;
        this.mPreferences = preferences;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> list;
        NetworkUtils.getJsonString(mPreferences);
        list = JsonUtils.getBaseListFromJson();
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<String> list) {
        super.onPostExecute(list);
        mTaskCompleted.onTaskCompleted(list);
    }
}
