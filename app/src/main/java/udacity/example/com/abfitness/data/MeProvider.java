package udacity.example.com.abfitness.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import udacity.example.com.abfitness.data.MeContract.UserEntry;

public class MeProvider extends ContentProvider {

    public static final String TAG = MeProvider.class.getSimpleName();
    private static final int USERS = 1000;
    private static final int USER_ID = 1001;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MeContract.CONTENT_AUTHORITY, MeContract.PATH_ABFIT, USERS);
        sUriMatcher.addURI(MeContract.CONTENT_AUTHORITY, MeContract.PATH_ABFIT + "/#", USER_ID);
    }


    private MeDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MeDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case USER_ID:
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
            throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            case USER_ID:
                return UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(UserEntry.COLUMN_USER_NAME);
        if (name == null) {
            throw new IllegalArgumentException("User requires a name");
        }

        // If the age is provided, check that it's greater than or equal to 0 kg
        Integer age = values.getAsInteger(UserEntry.COLUMN_USER_AGE);
        if (age != null && age < 0) {
            throw new IllegalArgumentException("User requires valid age");
        }

        // Check that the gender is valid
        Integer gender = values.getAsInteger(UserEntry.COLUMN_USER_GENDER);
        if (gender == null || !UserEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("User requires valid gender");
        }

        // If the height is provided, check that it's greater than or equal to 0 kg
        Integer height = values.getAsInteger(UserEntry.COLUMN_USER_HEIGHT);
        if (height != null && height < 50) {
            throw new IllegalArgumentException("User requires valid height");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(UserEntry.COLUMN_USER_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("User requires valid weight");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(UserEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(UserEntry.COLUMN_USER_NAME)) {
            String name = values.getAsString(UserEntry.COLUMN_USER_NAME);
            if (name == null) {
                throw new IllegalArgumentException("User requires a name");
            }
        }

        if (values.containsKey(UserEntry.COLUMN_USER_AGE)) {
            Integer age = values.getAsInteger(UserEntry.COLUMN_USER_AGE);
            if (age != null && age < 0) {
                throw new IllegalArgumentException("User requires valid age");
            }
        }

        if (values.containsKey(UserEntry.COLUMN_USER_GENDER)) {
            Integer gender = values.getAsInteger(UserEntry.COLUMN_USER_GENDER);
            if (gender == null || !UserEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("User requires valid gender");
            }
        }

        if (values.containsKey(UserEntry.COLUMN_USER_HEIGHT)) {
            Integer height = values.getAsInteger(UserEntry.COLUMN_USER_HEIGHT);
            if (height != null && height < 0) {
                throw new IllegalArgumentException("User requires valid height");
            }
        }

        if (values.containsKey(UserEntry.COLUMN_USER_WEIGHT)) {
            Integer weight = values.getAsInteger(UserEntry.COLUMN_USER_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("User requires valid weight");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


}
