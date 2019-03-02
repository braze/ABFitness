package udacity.example.com.abfitness.async.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import udacity.example.com.abfitness.utils.NetworkUtils;

public class FetchJsonAsyncTask extends AsyncTask<Void, Void, Void> {

    private static String TAG = FetchJsonAsyncTask.class.getSimpleName();
    private SharedPreferences mPreferences;


    public FetchJsonAsyncTask(SharedPreferences preferences) {
        this.mPreferences = preferences;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        NetworkUtils.getJsonString(mPreferences);
        return null;
    }
}
