package udacity.example.com.abfitness.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import udacity.example.com.abfitness.data.MeContract.UserEntry;

public class MeDbHelper extends SQLiteOpenHelper {

    public static final String TAG = MeDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "abfitness.db";

    private static final int DATABASE_VERSION = 1;

    public MeDbHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + "("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_AGE + " INTEGER NOT NULL, "
                + UserEntry.COLUMN_USER_GENDER + " INTEGER NOT NULL, "
                + UserEntry.COLUMN_USER_HEIGHT + " INTEGER NOT NULL, "
                + UserEntry.COLUMN_USER_WEIGHT + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
