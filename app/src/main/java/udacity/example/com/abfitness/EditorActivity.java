package udacity.example.com.abfitness;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = EditorActivity.class.getSimpleName();
    private static final int EXISTING_USER_LOADER = 0;

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

    private Uri mCurrentUserUri;
    private int mGender = UserEntry.GENDER_FEMALE;
    private boolean mUserHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
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

        Intent intent = getIntent();
        mCurrentUserUri = intent.getData();
        if (mCurrentUserUri != null) {
            getLoaderManager().initLoader(EXISTING_USER_LOADER, null, this);
        }

        mUserName.setOnTouchListener(mTouchListener);
        mAge.setOnTouchListener(mTouchListener);
        mHeight.setOnTouchListener(mTouchListener);
        mWeight.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
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
            mCurrentUserUri = null;
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
            int age = cursor.getInt(ageColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int height = cursor.getInt(heightColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);

            // Update the views on the screen with the values from the database
            mUserName.setText(name);
            mAge.setText(String.valueOf(age));
            mHeight.setText(String.valueOf(height));
            mWeight.setText(String.valueOf(weight));

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (gender) {
                case UserEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case UserEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(0);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUserName.setText("");
        mAge.setText("");
        mHeight.setText("");
        mWeight.setText("");
        mGenderSpinner.setSelection(0);
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

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
//                DialogInterface.OnClickListener discardButtonClickListener =
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // User clicked "Discard" button, navigate to parent activity.
//                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
//                            }
//                        };
//
//                // Show a dialog that notifies the user they have unsaved changes
//                showUnsavedChangesDialog(discardButtonClickListener);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUser() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String userNameString = mUserName.getText().toString().trim();
        String ageString = mAge.getText().toString().trim();
        String heightString = mHeight.getText().toString().trim();
        String weightString = mWeight.getText().toString().trim();

        if (mCurrentUserUri == null &&
                TextUtils.isEmpty(userNameString) && TextUtils.isEmpty(ageString) &&
                TextUtils.isEmpty(heightString) && TextUtils.isEmpty(weightString)) {
            Toast.makeText(this, getString(R.string.editor_insert_user_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER_NAME, userNameString);
        values.put(UserEntry.COLUMN_USER_GENDER, mGender);

        try {

            int age = 0;
            if (!TextUtils.isEmpty(weightString)) {
                age = Integer.parseInt(ageString);
            }
            values.put(UserEntry.COLUMN_USER_AGE, age);

            int height = 0;
            if (!TextUtils.isEmpty(weightString)) {
                height = Integer.parseInt(heightString);
            }
            values.put(UserEntry.COLUMN_USER_HEIGHT, height);

            int weight = 0;
            if (!TextUtils.isEmpty(weightString)) {
                weight = Integer.parseInt(weightString);
            }
            values.put(UserEntry.COLUMN_USER_WEIGHT, weight);
        }catch (ParseException e) {
            values.put(UserEntry.COLUMN_USER_AGE, 0);
            values.put(UserEntry.COLUMN_USER_HEIGHT, 0);
            values.put(UserEntry.COLUMN_USER_WEIGHT, 0);
        }

        Log.d(TAG, "mCurrentUserUri == null ? : " + (mCurrentUserUri == null));

        if (mCurrentUserUri == null) {
            Uri newUri = getContentResolver().insert(UserEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Log.d(TAG, "saveUser: FAIL");
                Toast.makeText(this, getString(R.string.editor_insert_user_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_user_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentUserUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_user_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_user_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}

