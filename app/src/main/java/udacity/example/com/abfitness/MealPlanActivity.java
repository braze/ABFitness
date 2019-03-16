package udacity.example.com.abfitness;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.abfitness.data.MeContract.UserEntry;
import udacity.example.com.abfitness.utils.Calculate;

public class MealPlanActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MealPlanActivity.class.getSimpleName();
    private static final int EXISTING_USER_LOADER = 3450;
    private Uri mCurrentUserUri;
    private int mAge;
    private int mGender;
    private int mHeight;
    private int mWeight;
    private int mTotalCaloriesValue;

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
        setTitle("Meal plan");

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentUserUri = intent.getData();
        if (mCurrentUserUri != null) {
            getLoaderManager().initLoader(EXISTING_USER_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                UserEntry._ID,
                UserEntry.COLUMN_USER_NAME,
                UserEntry.COLUMN_USER_AGE,
                UserEntry.COLUMN_USER_GENDER,
                UserEntry.COLUMN_USER_HEIGHT,
                UserEntry.COLUMN_USER_WEIGHT };

        return new CursorLoader(this,
                mCurrentUserUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            Toast.makeText(this, getString(R.string.meal_plan_failed),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MealPlanActivity.this, EditorActivity.class);
            Uri currentPetUri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, 1);
            intent.setData(currentPetUri);
            startActivity(intent);
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_USER_NAME);
            int ageColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_USER_AGE);
            int genderColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_USER_GENDER);
            int heightColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_USER_HEIGHT);
            int weightColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_USER_WEIGHT);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            mAge = cursor.getInt(ageColumnIndex);
            mGender = cursor.getInt(genderColumnIndex);
            mHeight = cursor.getInt(heightColumnIndex);
            mWeight = cursor.getInt(weightColumnIndex);

            mTotalCaloriesValue = Calculate.getCaloriesPlan(mHeight, mGender);

            fillViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void fillViews() {
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
}
