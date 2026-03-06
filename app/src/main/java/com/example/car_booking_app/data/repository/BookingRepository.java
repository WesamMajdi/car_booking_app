package com.example.car_booking_app.data.repository;

import androidx.lifecycle.LiveData;
import com.example.car_booking_app.data.dao.BookingDao;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.Booking;
import java.util.List;

public class BookingRepository {
    private BookingDao bookingDao;
    private com.example.car_booking_app.data.dao.CarDao carDao;

    public BookingRepository(BookingDao bookingDao, com.example.car_booking_app.data.dao.CarDao carDao) {
        this.bookingDao = bookingDao;
        this.carDao = carDao;
    }

    public void submitRating(int bookingId, int carId, float rating) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            // 1. Update booking rating
            bookingDao.updateBookingRating(bookingId, rating);

            // 2. Update car average rating
            com.example.car_booking_app.data.entity.Car car = carDao.getCarById(carId);
            if (car != null) {
                float currentTotal = car.averageRating * car.ratingCount;
                float newTotal = currentTotal + rating;
                int newCount = car.ratingCount + 1;
                float newAverage = newTotal / newCount;
                
                carDao.updateCarRating(carId, newAverage, newCount);
            }
        });
    }

    public void insertBooking(Booking booking, final RepositoryCallback<Long> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long id = bookingDao.insertBooking(booking);
                if (callback != null) {
                    callback.onComplete(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(-1L);
                }
            }
        });
    }

    public LiveData<List<Booking>> getBookingsByUser(int userId) {
        return bookingDao.getBookingsByUser(userId);
    }

    public void getBookingById(int id, final RepositoryCallback<Booking> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            Booking booking = bookingDao.getBookingById(id);
            if (callback != null) {
                callback.onComplete(booking);
            }
        });
    }

    public void updateBookingStatus(int id, String status) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            bookingDao.updateBookingStatus(id, status);
        });
    }

    public LiveData<List<com.example.car_booking_app.data.entity.BookingWithCar>> getBookingsWithCarByUser(int userId) {
        return bookingDao.getBookingsWithCarByUser(userId);
    }
}
