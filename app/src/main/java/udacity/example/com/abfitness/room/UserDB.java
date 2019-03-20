package udacity.example.com.abfitness.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import udacity.example.com.abfitness.model.User;

@Database(entities = {User.class},version = 2, exportSchema = false)
public abstract class UserDB extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "abfitness";
    private static UserDB sInstance;

    public static UserDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        UserDB.class, UserDB.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract UserDAO userDao();

}
