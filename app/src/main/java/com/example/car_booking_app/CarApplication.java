package com.example.car_booking_app;

import android.app.Application;

import androidx.work.PeriodicWorkRequest;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.repository.BookingRepository;
import com.example.car_booking_app.data.repository.CarRepository;
import com.example.car_booking_app.data.repository.UserRepository;
import com.example.car_booking_app.worker.BookingNotificationWorker;

import java.util.concurrent.TimeUnit;

public class CarApplication extends Application {
    
    private CarDatabase database;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private BookingRepository bookingRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        database = CarDatabase.getDatabase(this);
        userRepository = new UserRepository(database.userDao());
        carRepository = new CarRepository(database.carDao());
        bookingRepository = new BookingRepository(database.bookingDao(), database.carDao());

        // Schedule notification worker
        scheduleNotificationWorker();
    }

    private void scheduleNotificationWorker() {
        // Run immediately once
        OneTimeWorkRequest oneTimeWork = new OneTimeWorkRequest.Builder(BookingNotificationWorker.class).build();
        WorkManager.getInstance(this).enqueue(oneTimeWork);

        // Schedule periodic
        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(
                BookingNotificationWorker.class,
                15, TimeUnit.MINUTES) // Minimum interval is 15 minutes
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "BookingNotificationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationWork);
    }

    public CarDatabase getDatabase() {
        return database;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CarRepository getCarRepository() {
        return carRepository;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }
}
