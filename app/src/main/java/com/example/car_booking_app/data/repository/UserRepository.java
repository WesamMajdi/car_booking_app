package com.example.car_booking_app.data.repository;

import com.example.car_booking_app.data.dao.UserDao;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.User;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void registerUser(User user, final RepositoryCallback<Long> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            long id = userDao.insertUser(user);
            if (callback != null) {
                callback.onComplete(id);
            }
        });
    }

    public void loginUser(String email, final RepositoryCallback<User> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUserByEmail(email);
            if (callback != null) {
                callback.onComplete(user);
            }
        });
    }

    public void updateUser(User user, final RepositoryCallback<Boolean> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateUser(user);
            if (callback != null) {
                callback.onComplete(true);
            }
        });
    }

    public void getUserById(int id, final RepositoryCallback<User> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUserById(id);
            if (callback != null) {
                callback.onComplete(user);
            }
        });
    }
}
