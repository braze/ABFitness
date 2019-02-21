package udacity.example.com.abfitness.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import udacity.example.com.abfitness.CoolDownFragment;
import udacity.example.com.abfitness.WarmUpFragment;
import udacity.example.com.abfitness.WorkoutFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private String mWarmUpVideoUrl;
    private ArrayList<String> mWarmUpDescription;
    private String mWorkOutVideoUrl;
    private ArrayList<String> mWorkOutDescription;
    private String mCoolDownVideoUrl;
    private ArrayList<String> mCoolDownDescription;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,
                        String warmUpVideoUrl, ArrayList<String> warmUpDescription,
                        String workOutVideoUrl, ArrayList<String> workOutDescription,
                        String coolDownVideoUrl, ArrayList<String> coolDownDescription) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mWarmUpVideoUrl = warmUpVideoUrl;
        this.mWarmUpDescription = warmUpDescription;
        this.mWorkOutVideoUrl = workOutVideoUrl;
        this.mWorkOutDescription = workOutDescription;
        this.mCoolDownVideoUrl = coolDownVideoUrl;
        this.mCoolDownDescription = coolDownDescription;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                WarmUpFragment tab1 = WarmUpFragment.newInstance(mWarmUpVideoUrl, mWarmUpDescription);
                return tab1;
            case 1:
                WorkoutFragment tab2 = WorkoutFragment.newInstance(mWorkOutVideoUrl, mWorkOutDescription);
                return tab2;
            case 2:
                CoolDownFragment tab3 = CoolDownFragment.newInstance(mCoolDownVideoUrl, mCoolDownDescription);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
