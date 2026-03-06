package com.example.car_booking_app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "bookings",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Car.class,
            parentColumns = "id",
            childColumns = "carId",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Booking {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int carId;
    public long startDate;
    public long endDate;
    public double totalPrice;
    public String status;
    public float rating;

    public Booking(int userId, int carId, long startDate, long endDate, double totalPrice, String status) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.rating = 0;
    }

    public Booking() {}
}
