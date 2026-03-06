package com.example.car_booking_app.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.car_booking_app.data.dao.NotificationDao;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.Notification;

import java.util.List;

public class NotificationRepository {
    private NotificationDao notificationDao;
    private LiveData<List<Notification>> allNotifications;

    public NotificationRepository(Application application) {
        CarDatabase db = CarDatabase.getDatabase(application);
        notificationDao = db.notificationDao();
        allNotifications = notificationDao.getAllNotifications();
    }

    public LiveData<List<Notification>> getAllNotifications() {
        return allNotifications;
    }

    public void markAllAsRead() {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.markAllAsRead();
        });
    }

    public void insert(Notification notification) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.insert(notification);
        });
    }
}
