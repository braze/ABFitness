package udacity.example.com.abfitness.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import udacity.example.com.abfitness.room.UserDB;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final UserDB mDb;
    private final int mUserId;

    public UserViewModelFactory(UserDB database, int userId) {
        mDb = database;
        mUserId = userId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UserViewModel(mDb, mUserId);
    }
}
