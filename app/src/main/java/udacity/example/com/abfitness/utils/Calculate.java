package udacity.example.com.abfitness.utils;

import udacity.example.com.abfitness.data.MeContract.UserEntry;

public class Calculate {

    private Calculate() {
    }

    public static int getCaloriesPlan (int height, int gender) {
        int calories = 2000;
        if (gender == UserEntry.GENDER_FEMALE) {
            if (height <= 165){
                calories = 1500;
            } else if (height <= 175) {
                calories = 1650;
            } else if (height <= 185 ) {
                calories = 1800;
            } else if (height > 186) {
                calories = 2000;
            }

        } else {
            if (height <= 165){
                calories = 1900;
            } else if (height <= 175) {
                calories = 2050;
            } else if (height <= 185 ) {
                calories = 2200;
            } else if (height > 186) {
                calories = 2450;
            }
        }
        return calories;
    }

}
