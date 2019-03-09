package udacity.example.com.abfitness.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import udacity.example.com.abfitness.fragments.CoolDownFragment;
import udacity.example.com.abfitness.fragments.WarmUpFragment;
import udacity.example.com.abfitness.fragments.WorkoutFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = PagerAdapter.class.getSimpleName();
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
                return WarmUpFragment.newInstance(mWarmUpVideoUrl, mWarmUpDescription);
            case 1:
                return WorkoutFragment.newInstance(mWorkOutVideoUrl, mWorkOutDescription);
            case 2:
                return CoolDownFragment.newInstance(mCoolDownVideoUrl, mCoolDownDescription);
            default:
                return WarmUpFragment.newInstance(mWarmUpVideoUrl, mWarmUpDescription);
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
