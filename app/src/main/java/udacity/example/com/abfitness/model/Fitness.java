package udacity.example.com.abfitness.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Fitness implements Parcelable {

    private String warmUpVideoUrl;
    private ArrayList<String> warmUpDescription;
    private String workOutVideoUrl;
    private ArrayList<String> workOutDescription;
    private String coolDownVideoUrl;
    private ArrayList<String> coolDownDescription;
    private String breakfastPicUrl;
    private String breakfastText;
    private String lunchPicUrl;
    private String lunchText;
    private String dinnerUicUrl;
    private String dinnerText;
    private String snacksPicUrl;
    private String snacksText;
    private ArrayList<News> news;

    public Fitness() {
    }

    public Fitness(String warmUpVideoUrl, ArrayList<String> warmUpDescription,
                   String workOutVideoUrl, ArrayList<String> workOutDescription,
                   String coolDownVideoUrl, ArrayList<String> coolDownDescription) {
        this.warmUpVideoUrl = warmUpVideoUrl;
        this.warmUpDescription = warmUpDescription;
        this.workOutVideoUrl = workOutVideoUrl;
        this.workOutDescription = workOutDescription;
        this.coolDownVideoUrl = coolDownVideoUrl;
        this.coolDownDescription = coolDownDescription;
    }

    public Fitness(String breakfastPicUrl, String breakfastText,
                   String lunchPicUrl, String lunchText,
                   String dinnerUicUrl, String dinnerText,
                   String snacksPicUrl, String snacksText) {
        this.breakfastPicUrl = breakfastPicUrl;
        this.breakfastText = breakfastText;
        this.lunchPicUrl = lunchPicUrl;
        this.lunchText = lunchText;
        this.dinnerUicUrl = dinnerUicUrl;
        this.dinnerText = dinnerText;
        this.snacksPicUrl = snacksPicUrl;
        this.snacksText = snacksText;
    }

    public Fitness(ArrayList<News> news) {
        this.news = news;
    }

    public String getWarmUpVideoUrl() {
        return warmUpVideoUrl;
    }

    public void setWarmUpVideoUrl(String warmUpVideoUrl) {
        this.warmUpVideoUrl = warmUpVideoUrl;
    }

    public ArrayList<String> getWarmUpDescription() {
        return warmUpDescription;
    }

    public void setWarmUpDescription(ArrayList<String> warmUpDescription) {
        this.warmUpDescription = warmUpDescription;
    }

    public String getWorkOutVideoUrl() {
        return workOutVideoUrl;
    }

    public void setWorkOutVideoUrl(String workOutVideoUrl) {
        this.workOutVideoUrl = workOutVideoUrl;
    }

    public ArrayList<String> getWorkOutDescription() {
        return workOutDescription;
    }

    public void setWorkOutDescription(ArrayList<String> workOutDescription) {
        this.workOutDescription = workOutDescription;
    }

    public String getCoolDownVideoUrl() {
        return coolDownVideoUrl;
    }

    public void setCoolDownVideoUrl(String coolDownVideoUrl) {
        this.coolDownVideoUrl = coolDownVideoUrl;
    }

    public ArrayList<String> getCoolDownDescription() {
        return coolDownDescription;
    }

    public void setCoolDownDescription(ArrayList<String> coolDownDescription) {
        this.coolDownDescription = coolDownDescription;
    }

    public String getBreakfastPicUrl() {
        return breakfastPicUrl;
    }

    public void setBreakfastPicUrl(String breakfastPicUrl) {
        this.breakfastPicUrl = breakfastPicUrl;
    }

    public String getBreakfastText() {
        return breakfastText;
    }

    public void setBreakfastText(String breakfastText) {
        this.breakfastText = breakfastText;
    }

    public String getLunchPicUrl() {
        return lunchPicUrl;
    }

    public void setLunchPicUrl(String lunchPicUrl) {
        this.lunchPicUrl = lunchPicUrl;
    }

    public String getLunchText() {
        return lunchText;
    }

    public void setLunchText(String lunchText) {
        this.lunchText = lunchText;
    }

    public String getDinnerUicUrl() {
        return dinnerUicUrl;
    }

    public void setDinnerUicUrl(String dinnerUicUrl) {
        this.dinnerUicUrl = dinnerUicUrl;
    }

    public String getDinnerText() {
        return dinnerText;
    }

    public void setDinnerText(String dinnerText) {
        this.dinnerText = dinnerText;
    }

    public String getSnacksPicUrl() {
        return snacksPicUrl;
    }

    public void setSnacksPicUrl(String snacksPicUrl) {
        this.snacksPicUrl = snacksPicUrl;
    }

    public String getSnacksText() {
        return snacksText;
    }

    public void setSnacksText(String snacksText) {
        this.snacksText = snacksText;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    protected Fitness(Parcel in) {
        warmUpVideoUrl = in.readString();
        warmUpDescription = in.createStringArrayList();
        workOutVideoUrl = in.readString();
        workOutDescription = in.createStringArrayList();
        coolDownVideoUrl = in.readString();
        coolDownDescription = in.createStringArrayList();
        breakfastPicUrl = in.readString();
        breakfastText = in.readString();
        lunchPicUrl = in.readString();
        lunchText = in.readString();
        dinnerUicUrl = in.readString();
        dinnerText = in.readString();
        snacksPicUrl = in.readString();
        snacksText = in.readString();
        news = in.createTypedArrayList(News.CREATOR);
    }

    public static final Creator<Fitness> CREATOR = new Creator<Fitness>() {
        @Override
        public Fitness createFromParcel(Parcel in) {
            return new Fitness(in);
        }

        @Override
        public Fitness[] newArray(int size) {
            return new Fitness[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(warmUpVideoUrl);
        dest.writeStringList(warmUpDescription);
        dest.writeString(workOutVideoUrl);
        dest.writeStringList(workOutDescription);
        dest.writeString(coolDownVideoUrl);
        dest.writeStringList(coolDownDescription);
        dest.writeString(breakfastPicUrl);
        dest.writeString(breakfastText);
        dest.writeString(lunchPicUrl);
        dest.writeString(lunchText);
        dest.writeString(dinnerUicUrl);
        dest.writeString(dinnerText);
        dest.writeString(snacksPicUrl);
        dest.writeString(snacksText);
        dest.writeTypedList(news);
    }
}
