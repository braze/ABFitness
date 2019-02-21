package udacity.example.com.abfitness;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import udacity.example.com.abfitness.Utils.JsonUtils;
import udacity.example.com.abfitness.Utils.NetworkUtils;
import udacity.example.com.abfitness.Utils.PagerAdapter;
import udacity.example.com.abfitness.model.Fitness;

import static udacity.example.com.abfitness.BaseActivity.ARG_INTENT_EXERCISES;
import static udacity.example.com.abfitness.Utils.NetworkUtils.THE_JSON;

public class Exercises extends AppCompatActivity implements
                WarmUpFragment.OnFragmentInteractionListener,
                WorkoutFragment.OnFragmentInteractionListener,
                CoolDownFragment.OnFragmentInteractionListener {

    private static final String TAG = Exercises.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Fitness fitness = null;
        String warmUpVideoUrl = null;
        ArrayList<String> warmUpDescription = null;
        String workOutVideoUrl = null;
        ArrayList<String> workOutDescription = null;
        String coolDownVideoUrl = null;
        ArrayList<String> coolDownDescription = null;

        Intent intent = getIntent();
        if (intent != null) {
            String jsonPositionName = intent.getStringExtra(ARG_INTENT_EXERCISES);
            String jsonString = NetworkUtils.getSharedPreferences().getString(THE_JSON,"");
            fitness = JsonUtils.getExercises(jsonString, jsonPositionName);
            warmUpVideoUrl = fitness.getWarmUpVideoUrl();
            warmUpDescription = fitness.getWarmUpDescription();
            workOutVideoUrl = fitness.getWorkOutVideoUrl();
            workOutDescription = fitness.getWorkOutDescription();
            coolDownVideoUrl = fitness.getCoolDownVideoUrl();
            coolDownDescription = fitness.getCoolDownDescription();
            ((AppCompatActivity)this).getSupportActionBar().setTitle(jsonPositionName + " activity");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Warm Up"));
        tabLayout.addTab(tabLayout.newTab().setText("Work Out"));
        tabLayout.addTab(tabLayout.newTab().setText("Cool Down"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),
                        warmUpVideoUrl, warmUpDescription, workOutVideoUrl,
                        workOutDescription, coolDownVideoUrl, coolDownDescription);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
