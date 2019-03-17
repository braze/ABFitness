package udacity.example.com.abfitness;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.ArrayList;

import udacity.example.com.abfitness.adapters.PagerAdapter;
import udacity.example.com.abfitness.fragments.CoolDownFragment;
import udacity.example.com.abfitness.fragments.WarmUpFragment;
import udacity.example.com.abfitness.fragments.WorkoutFragment;
import udacity.example.com.abfitness.model.Fitness;
import udacity.example.com.abfitness.utils.JsonUtils;
import udacity.example.com.abfitness.utils.NetworkUtils;

import static udacity.example.com.abfitness.BaseActivity.ARG_INTENT_EXERCISES;
import static udacity.example.com.abfitness.utils.NetworkUtils.THE_JSON;

public class ExercisesActivity extends AppCompatActivity implements
                WarmUpFragment.OnFragmentInteractionListener,
                WorkoutFragment.OnFragmentInteractionListener,
                CoolDownFragment.OnFragmentInteractionListener {

    private static final String TAG = ExercisesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = findViewById(R.id.exercise_toolbar);
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
            this.getSupportActionBar().setTitle(jsonPositionName + getString(R.string.exercise_activity_title));
        }

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.warm_up_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.workout_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cool_down_title));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),
                        warmUpVideoUrl, warmUpDescription, workOutVideoUrl,
                        workOutDescription, coolDownVideoUrl, coolDownDescription);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
