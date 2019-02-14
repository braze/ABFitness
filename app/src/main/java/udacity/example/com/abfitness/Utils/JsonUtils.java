package udacity.example.com.abfitness.Utils;

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

    public static ArrayList<String> getBaseListFromJson() {
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
            warmUpVideoUrl = warmUp.getString("video_url");
            JSONArray warmUpDescriptionArr = warmUp.getJSONArray("description");
            for (int i = 0; i < warmUpDescriptionArr.length(); i++) {
                warmUpDescription.add(warmUpDescriptionArr.getString(i));
            }
            JSONObject workOut = objectInArray.getJSONObject("work_out");
            workOutVideoUrl = workOut.getString("video_url");
            JSONArray workOutDescriptionArr = workOut.getJSONArray("description");
            for (int i = 0; i < workOutDescriptionArr.length(); i++) {
                workOutDescription.add(workOutDescriptionArr.getString(i));
            }
            JSONObject coolDown = objectInArray.getJSONObject("cool_down");
            coolDownVideoUrl = coolDown.getString("video_url");
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

    public static Fitness getMealPlan(String jsonString, String dayOfWeek) {
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
            JSONObject objectInArray = mainObj.getJSONObject(dayOfWeek);
            JSONObject mealPlan = objectInArray.getJSONObject("meal_plan");

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Fitness(breakfastPic, breakfastText, lunchPicUrl, lunchText,
                dinnerPicUrl, dinnerText, snacksPicUrl, snacksText);
    }

    public static Fitness getNews (String jsonString, String dayOfWeek) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        ArrayList<News> list =new ArrayList<>();
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(jsonString);
            JSONObject objectInArray = mainObj.getJSONObject(dayOfWeek);
            JSONArray news = objectInArray.getJSONArray("news");
            for (int i = 0; i < news.length(); i++) {
                JSONObject object = news.getJSONObject(i);
                String newsHeader = object.getString("news_header");
                String newsPicUrl = object.getString("news_pic_url");
                String newsBody = object.getString("news_body");
                list.add(new News(newsHeader, newsPicUrl, newsBody));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Fitness(list);
    }
}
