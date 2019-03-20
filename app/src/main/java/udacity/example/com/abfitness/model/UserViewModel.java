package udacity.example.com.abfitness.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import udacity.example.com.abfitness.room.UserDB;

public class UserViewModel extends ViewModel {

    private LiveData<User> user;

    public UserViewModel(UserDB database, int userId) {
        user = database.userDao().getUserById(userId);
    }

    public LiveData<User> getUser() {
        return user;
    }

}
