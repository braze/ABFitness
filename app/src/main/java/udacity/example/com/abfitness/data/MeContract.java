package udacity.example.com.abfitness.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MeContract {

    private MeContract() {
    }

    public static final String CONTENT_AUTHORITY = "udacity.example.com.abfitness";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ABFIT = "abfit";

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ABFIT);

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ABFIT;

        public final static String TABLE_NAME = "abfitness";

        /**
         * Unique ID number for the user (possible future field).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * UserName.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_NAME = "name";

        /**
         * User age
         *
         * Type: INTEGER
         */
        public final static String COLUMN_USER_AGE = "age";

        /**
         * Gender of the user.
         *
         * The only possible values are {@link #GENDER_MALE},{ @link #GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_USER_GENDER = "gender";

        /**
         * User's weight.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_USER_WEIGHT = "weight";

        /**
         * User's height.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_USER_HEIGHT = "height";

        /**
         * Possible values user's gender.
         */
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 0;

        public static boolean isValidGender(int gender) {
            return gender == GENDER_MALE || gender == GENDER_FEMALE;
        }

    }
}
