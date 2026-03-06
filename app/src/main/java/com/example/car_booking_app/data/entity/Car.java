package com.example.car_booking_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cars")
public class Car {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String model;
    public int year; // Added year field
    public String description;
    public double pricePerDay;
    public String imageUrl;
    public String category;
    public boolean isAvailable;
    public String transmission;
    public String fuelType;
    public int seats;
    public boolean isFavorite;
    public float averageRating;
    public int ratingCount;

    public Car(String name, String model, int year, String description, double pricePerDay, String imageUrl, String category, boolean isAvailable, String transmission, String fuelType, int seats, boolean isFavorite) {
        this.name = name;
        this.model = model;
        this.year = year;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isAvailable = isAvailable;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.seats = seats;
        this.isFavorite = isFavorite;
        this.averageRating = 0;
        this.ratingCount = 0;
    }

    public Car() {}
}
