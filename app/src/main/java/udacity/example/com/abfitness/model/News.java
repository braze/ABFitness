package udacity.example.com.abfitness.model;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    private String newsHeader;
    private String newsPicUrl;
    private String newsBody;

    public News(String newsHeader, String newsPicUrl, String newsBody) {
        this.newsHeader = newsHeader;
        this.newsPicUrl = newsPicUrl;
        this.newsBody = newsBody;
    }

    protected News(Parcel in) {
        newsHeader = in.readString();
        newsPicUrl = in.readString();
        newsBody = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsHeader);
        dest.writeString(newsPicUrl);
        dest.writeString(newsBody);
    }
}
