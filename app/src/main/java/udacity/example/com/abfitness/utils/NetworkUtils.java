package udacity.example.com.abfitness.utils;

import android.content.SharedPreferences;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();
    private static final String ABFITNESS_BASE_URL = "https://firebasestorage.googleapis.com/v0/b/abfiness-dd02f.appspot.com/o/abfit.json?alt=media&token=fcbad711-0de6-4e14-9a55-c56c81c30ee3";
    public static final String THE_JSON = "the_Json";
    private static SharedPreferences sPreferences;

    private NetworkUtils() {
    }

    public static void getJsonString(SharedPreferences preferences){
        sPreferences = preferences;

        URL fitnessUrl = buildUrl();
        String jsonString = null;
        try {
            jsonString = fetchJson(fitnessUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sPreferences.edit().putString(THE_JSON, jsonString).apply();
    }

    /**
     * Build the base URL
     * @return The URL.
     */
    public static URL buildUrl() {
        URL url = null;
        try {
            url = new URL(ABFITNESS_BASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String fetchJson(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static SharedPreferences getSharedPreferences() {
        return sPreferences;
    }
}
