package com.example.car_booking_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.car_booking_app.data.entity.Booking;
import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    long insertBooking(Booking booking);

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY startDate DESC")
    LiveData<List<Booking>> getBookingsByUser(int userId);

    @Query("SELECT * FROM bookings WHERE id = :id")
    Booking getBookingById(int id);

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    void updateBookingStatus(int id, String status);

    @Query("UPDATE bookings SET rating = :rating WHERE id = :id")
    void updateBookingRating(int id, float rating);

    @androidx.room.Transaction
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY startDate DESC")
    LiveData<List<com.example.car_booking_app.data.entity.BookingWithCar>> getBookingsWithCarByUser(int userId);

    @Query("SELECT * FROM bookings WHERE status LIKE 'active' OR status LIKE 'confirmed'")
    List<Booking> getAllActiveBookingsSync();
}
