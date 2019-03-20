package udacity.example.com.abfitness.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "abfitness")
public class User {

    @PrimaryKey
    private int userId;
    private String userName;
    private int gender;
    private int age;
    private int weight;
    private int height;

    public User(int userId, String userName, int gender, int age, int weight, int height) {
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
