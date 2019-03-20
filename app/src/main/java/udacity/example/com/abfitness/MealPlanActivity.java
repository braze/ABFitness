package udacity.example.com.abfitness;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.abfitness.data.MeContract.UserEntry;
import udacity.example.com.abfitness.model.User;
import udacity.example.com.abfitness.model.UserViewModel;
import udacity.example.com.abfitness.model.UserViewModelFactory;
import udacity.example.com.abfitness.room.UserDB;
import udacity.example.com.abfitness.utils.Calculate;

public class MealPlanActivity extends AppCompatActivity {

    private static final String TAG = MealPlanActivity.class.getSimpleName();

    private int mTotalCaloriesValue;
    private UserDB mDb;

    @BindView(R.id.total_calories)
    TextView mTotalCalories;

    @BindView(R.id.recovery_header)
    TextView mRecovery;

    @BindView(R.id.recovery_lv)
    ListView mRecoveryListView;

    @BindView(R.id.energy_header)
    TextView mEnergy;

    @BindView(R.id.energy_lv)
    ListView mEnergyListView;

    @BindView(R.id.vitality_header)
    TextView mVitality;

    @BindView(R.id.vitality_lv)
    ListView mVitalityListView;

    @BindView(R.id.oil_header)
    TextView mOil;

    @BindView(R.id.oil_lv)
    ListView mOilListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.meal_plan_title);

        ButterKnife.bind(this);
        mDb = UserDB.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            final int id = intent.getIntExtra("userId", 1);
            UserViewModelFactory factory = new UserViewModelFactory(mDb, id);
            UserViewModel viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
            viewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    if (user != null) {
                        int height = user.getHeight();
                        int gender = user.getGender();
                        mTotalCaloriesValue = Calculate.getCaloriesPlan(height, gender);
                        populateViews();
                    } else {
                        Intent intent = new Intent(MealPlanActivity.this, EditorActivity.class);
                        Uri currentPetUri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, 1);
                        intent.setData(currentPetUri);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void populateViews() {
        mTotalCalories.setText(getString(R.string.total_cal_per_day) + " " + String.valueOf(mTotalCaloriesValue));
        int recoveryValue = (int) (mTotalCaloriesValue * 0.3f);
        mRecovery.setText(getString(R.string.recovery_cal) + " " + String.valueOf(recoveryValue));
        mEnergy.setText(getString(R.string.energy_cal) + " " + String.valueOf(recoveryValue));
        int vitality = (int) (mTotalCaloriesValue * 0.35f);
        mVitality.setText(getString(R.string.vitality_cal) + " " + String.valueOf(vitality));
        int oil = (int) (mTotalCaloriesValue * 0.05f);
        mOil.setText(getString(R.string.oil_cal) + " " + String.valueOf(oil));

        ArrayAdapter recoveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_recovery, R.layout.exercises_list_view_item);
        mRecoveryListView.setAdapter(recoveryAdapter);
        setListViewHeightBasedOnChildren(mRecoveryListView);

        ArrayAdapter energyAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_energy, R.layout.exercises_list_view_item);
        mEnergyListView.setAdapter(energyAdapter);
        setListViewHeightBasedOnChildren(mEnergyListView);

        ArrayAdapter vitalityAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_vitality, R.layout.exercises_list_view_item);
        mVitalityListView.setAdapter(vitalityAdapter);
        setListViewHeightBasedOnChildren(mVitalityListView);

        ArrayAdapter oilAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_oil, R.layout.exercises_list_view_item);
        mOilListView.setAdapter(oilAdapter);
        setListViewHeightBasedOnChildren(mOilListView);
    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
