package udacity.example.com.abfitness;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.abfitness.data.MeContract.UserEntry;
import udacity.example.com.abfitness.model.User;
import udacity.example.com.abfitness.model.UserViewModel;
import udacity.example.com.abfitness.model.UserViewModelFactory;
import udacity.example.com.abfitness.room.UserDB;
import udacity.example.com.abfitness.utils.AppExecutor;

public class EditorActivity extends AppCompatActivity {

    private static final String TAG = EditorActivity.class.getSimpleName();

    @BindView(R.id.edit_username)
    EditText mUserName;

    @BindView(R.id.edit_user_age)
    EditText mAge;

    @BindView(R.id.edit_user_weight)
    EditText mWeight;

    @BindView(R.id.edit_user_height)
    EditText mHeight;

    @BindView(R.id.spinner_gender)
    Spinner mGenderSpinner;

    private int mGender = UserEntry.GENDER_FEMALE;
    private boolean mUserHasChanged = false;
    private UserDB mDb;
    private User mUser;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mUserHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

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
                        mUser = user;
                        populateUi();
                    }
                }
            });
        }

        mUserName.setOnTouchListener(mTouchListener);
        mAge.setOnTouchListener(mTouchListener);
        mHeight.setOnTouchListener(mTouchListener);
        mWeight.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void populateUi() {
        String name = mUser.getUserName();
        int age = mUser.getAge();
        int gender = mUser.getGender();
        int height = mUser.getHeight();
        int weight = mUser.getWeight();

        mUserName.setText(name);
        mAge.setText(String.valueOf(age));
        mHeight.setText(String.valueOf(height));
        mWeight.setText(String.valueOf(weight));
        mGender = gender;
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = UserEntry.GENDER_MALE;
                    } else {
                        mGender = UserEntry.GENDER_FEMALE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = UserEntry.GENDER_FEMALE;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveUser();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case android.R.id.home:
                if (!mUserHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUser() {
        String userNameString = mUserName.getText().toString().trim();
        String ageString = mAge.getText().toString().trim();
        String heightString = mHeight.getText().toString().trim();
        String weightString = mWeight.getText().toString().trim();

        if (  TextUtils.isEmpty(userNameString) && TextUtils.isEmpty(ageString) &&
                TextUtils.isEmpty(heightString) && TextUtils.isEmpty(weightString)) {
            Toast.makeText(this, getString(R.string.editor_insert_user_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        int age = 0;
        int height = 0;
        int weight = 0;
        try {
            age = Integer.parseInt(ageString);
        } catch (ParseException e) {
            age = 0;
        }

        try {
            height = Integer.parseInt(heightString);
        } catch (ParseException e) {
            height = 0;
        }

        try {
            weight = Integer.parseInt(weightString);

        } catch (ParseException e) {
            weight = 0;
        }

        final User user  = new User(1, userNameString, mGender, age, weight, height);

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.userDao().insertUser(user);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

