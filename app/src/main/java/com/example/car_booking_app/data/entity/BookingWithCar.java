package com.example.car_booking_app.data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BookingWithCar {
    @Embedded
    public Booking booking;

    @Relation(
            parentColumn = "carId",
            entityColumn = "id"
    )
    public Car car;
}
