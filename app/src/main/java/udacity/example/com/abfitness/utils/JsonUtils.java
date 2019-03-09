package udacity.example.com.abfitness.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import udacity.example.com.abfitness.model.Fitness;
import udacity.example.com.abfitness.model.News;

public class JsonUtils {

    private static String TAG = JsonUtils.class.getSimpleName();

    private JsonUtils() {
    }

    public static ArrayList<String> getBaseList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");
        return list;
    }

    public static Fitness getExercises(String jsonString, String dayOfWeek) {
        Log.d(TAG, "getExercises: START JSONiNG");
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
//        String baseUrl = "https://www.youtube.com" + "/watch?v=";
        String baseUrl = "";
        String warmUpVideoUrl = null;
        ArrayList<String> warmUpDescription = new ArrayList<>();
        String workOutVideoUrl = null;
        ArrayList<String> workOutDescription = new ArrayList<>();
        String coolDownVideoUrl = null;
        ArrayList<String> coolDownDescription = new ArrayList<>();

        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(jsonString);
            JSONObject objectInArray = mainObj.getJSONObject(dayOfWeek);

            JSONObject warmUp = objectInArray.getJSONObject("warm_up");
            warmUpVideoUrl = baseUrl + warmUp.getString("video_url");
            Log.d(TAG, "getExercises: warmUpVideoUrl>>>>> " + warmUpVideoUrl);
            JSONArray warmUpDescriptionArr = warmUp.getJSONArray("description");
            for (int i = 0; i < warmUpDescriptionArr.length(); i++) {
                warmUpDescription.add(warmUpDescriptionArr.getString(i));
                Log.d(TAG, "getExercises: >>>>"+ warmUpDescriptionArr.getString(i));
            }
            JSONObject workOut = objectInArray.getJSONObject("work_out");
            workOutVideoUrl = baseUrl + workOut.getString("video_url");
            JSONArray workOutDescriptionArr = workOut.getJSONArray("description");
            for (int i = 0; i < workOutDescriptionArr.length(); i++) {
                workOutDescription.add(workOutDescriptionArr.getString(i));
            }
            JSONObject coolDown = objectInArray.getJSONObject("cool_down");
            coolDownVideoUrl = baseUrl + coolDown.getString("video_url");
            JSONArray coolDownDescriptionArr = coolDown.getJSONArray("description");
            for (int i = 0; i < coolDownDescriptionArr.length(); i++) {
                coolDownDescription.add(coolDownDescriptionArr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Fitness(warmUpVideoUrl, warmUpDescription, workOutVideoUrl,
                workOutDescription, coolDownVideoUrl, coolDownDescription);
    }

    public static Fitness getMealPlan(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        JSONObject mainObj = null;
        String breakfastPic = null;
        String breakfastText = null;
        String lunchPicUrl = null;
        String lunchText = null;
        String dinnerPicUrl = null;
        String dinnerText = null;
        String snacksPicUrl = null;
        String snacksText = null;
        try {
            mainObj = new JSONObject(jsonString);
//            JSONObject objectInArray = mainObj.getJSONObject(dayOfWeek);
            JSONObject mealPlan = mainObj.getJSONObject("meal_plan");

            JSONObject breakfast = mealPlan.getJSONObject("breakfast");
            breakfastPic = breakfast.getString("breakfast_pic_url");
            breakfastText = breakfast.getString("breakfast_text");
            JSONObject lunch = mealPlan.getJSONObject("lunch");
            lunchPicUrl = lunch.getString("lunch_pic_url");
            lunchText = lunch.getString("lunch_text");
            JSONObject dinner = mealPlan.getJSONObject("dinner");
            dinnerPicUrl = dinner.getString("dinner_pic_url");
            dinnerText = dinner.getString("dinner_text");
            JSONObject snacks = mealPlan.getJSONObject("snacks");
            snacksPicUrl = snacks.getString("snacks_pic_url");
            snacksText = snacks.getString("snacks_text");

//            Log.d(TAG, "breakfastPic: " + breakfastPic);
//            Log.d(TAG, "breakfastText: " + breakfastText);
//            Log.d(TAG, "lunchPicUrl: " + lunchPicUrl);
//            Log.d(TAG, "lunchText: " + lunchText);
//            Log.d(TAG, "dinnerPicUrl: " + dinnerPicUrl);
//            Log.d(TAG, "dinnerText: " + dinnerText);
//            Log.d(TAG, "snacksPicUrl: " + snacksPicUrl);
//            Log.d(TAG, "snacksText: " + snacksText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Fitness(breakfastPic, breakfastText, lunchPicUrl, lunchText,
                dinnerPicUrl, dinnerText, snacksPicUrl, snacksText);
    }

    public static Fitness getNews (String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        ArrayList<News> list =new ArrayList<>();
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(jsonString);
//            JSONObject objectInArray = mainObj.getJSONObject(dayOfWeek);
            JSONArray news = mainObj.getJSONArray("news");
            for (int i = 0; i < news.length(); i++) {
                JSONObject object = news.getJSONObject(i);
                String newsHeader = object.getString("news_header");
                String newsPicUrl = object.getString("news_pic_url");
                String newsBody = object.getString("news_body");
//                Log.d(TAG, "getNews: " + newsHeader);
//                Log.d(TAG, "getNews: " + newsPicUrl);
//                Log.d(TAG, "getNews: " + newsBody);
                list.add(new News(newsHeader, newsPicUrl, newsBody));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Fitness(list);
    }
}
