package com.example.car_booking_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String message;
    public long timestamp;
    public boolean isRead;
    public String type; // "REMINDER", "OVERDUE", "COMPLETED"
    public int bookingId; // To link with booking

    public Notification(String title, String message, long timestamp, String type, int bookingId) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.bookingId = bookingId;
        this.isRead = false;
    }

    public Notification() {}
}
