package com.example.car_booking_app.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.car_booking_app.data.dao.NotificationDao;
import com.example.car_booking_app.data.entity.Notification;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private NotificationDao notificationDao;

    public NotificationViewModel(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public LiveData<List<Notification>> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }
}