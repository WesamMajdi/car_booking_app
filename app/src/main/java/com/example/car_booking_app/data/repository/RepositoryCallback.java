package com.example.car_booking_app.data.repository;

public interface RepositoryCallback<T> {
    void onComplete(T result);
}
