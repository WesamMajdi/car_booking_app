package com.example.car_booking_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.car_booking_app.data.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(Notification notification);

    @Update
    void update(Notification notification);

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    LiveData<List<Notification>> getAllNotifications();

    @Query("SELECT * FROM notifications WHERE bookingId = :bookingId AND type = :type LIMIT 1")
    Notification getNotificationForBooking(int bookingId, String type);

    @Query("DELETE FROM notifications")
    void deleteAll();

    @Query("UPDATE notifications SET isRead = 1 WHERE isRead = 0")
    void markAllAsRead();
}
