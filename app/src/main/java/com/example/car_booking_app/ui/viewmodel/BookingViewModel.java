package com.example.car_booking_app.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.car_booking_app.data.entity.Booking;
import com.example.car_booking_app.data.repository.BookingRepository;
import com.example.car_booking_app.data.repository.RepositoryCallback;

import com.example.car_booking_app.data.entity.BookingWithCar;

import java.util.List;

public class BookingViewModel extends ViewModel {

    private BookingRepository bookingRepository;
    private com.example.car_booking_app.data.repository.UserRepository userRepository;
    private com.example.car_booking_app.data.dao.NotificationDao notificationDao;
    private MutableLiveData<String> bookingStatus = new MutableLiveData<>();

    public BookingViewModel(BookingRepository bookingRepository, com.example.car_booking_app.data.repository.UserRepository userRepository, com.example.car_booking_app.data.dao.NotificationDao notificationDao) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.notificationDao = notificationDao;
    }

    public BookingViewModel(BookingRepository bookingRepository, com.example.car_booking_app.data.repository.UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public BookingViewModel(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        // Backward compatibility or secondary constructor if needed
    }

    public LiveData<List<Booking>> getBookingsForUser(int userId) {
        return bookingRepository.getBookingsByUser(userId);
    }

    public LiveData<List<BookingWithCar>> getBookingsWithCarForUser(int userId) {
        return bookingRepository.getBookingsWithCarByUser(userId);
    }

    public LiveData<List<BookingWithCar>> getCurrentBookings(int userId) {
        return androidx.lifecycle.Transformations.map(getBookingsWithCarForUser(userId), bookings -> {
            List<BookingWithCar> current = new java.util.ArrayList<>();
            
            // Get start of today
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            long startOfToday = calendar.getTimeInMillis();

            for (BookingWithCar b : bookings) {
                // If booking ends today or in the future, it is current
                if (b.booking.endDate >= startOfToday) {
                    current.add(b);
                }
            }
            return current;
        });
    }

    public LiveData<List<BookingWithCar>> getPastBookings(int userId) {
        return androidx.lifecycle.Transformations.map(getBookingsWithCarForUser(userId), bookings -> {
            List<BookingWithCar> past = new java.util.ArrayList<>();
            
            // Get start of today
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            long startOfToday = calendar.getTimeInMillis();

            for (BookingWithCar b : bookings) {
                // If booking ended before today, it is past
                if (b.booking.endDate < startOfToday) {
                    past.add(b);
                }
            }
            return past;
        });
    }

    public void createBooking(int userId, int carId, long startDate, long endDate, double pricePerDay) {
        // Validation check for dates
        if (startDate == 0 || endDate == 0) {
            bookingStatus.postValue("Invalid dates selected");
            return;
        }

        // Check if user exists first to prevent Foreign Key constraint failure
        if (userRepository != null) {
            userRepository.getUserById(userId, user -> {
                if (user == null) {
                    bookingStatus.postValue("User session invalid. Please logout and login again.");
                    return;
                }
                
                // Proceed with booking
                performBooking(userId, carId, startDate, endDate, pricePerDay);
            });
        } else {
            // Fallback for old constructor usage (should be avoided)
            performBooking(userId, carId, startDate, endDate, pricePerDay);
        }
    }

    private void performBooking(int userId, int carId, long startDate, long endDate, double pricePerDay) {
        // Calculate total price based on days
        long diff = endDate - startDate;
        long days = diff / (1000 * 60 * 60 * 24);
        if (days < 1) days = 1;
        double totalPrice = days * pricePerDay;

        Booking booking = new Booking();
        booking.userId = userId;
        booking.carId = carId;
        booking.startDate = startDate;
        booking.endDate = endDate;
        booking.totalPrice = totalPrice;
        booking.status = "active";

        bookingRepository.insertBooking(booking, new RepositoryCallback<Long>() {
            @Override
            public void onComplete(Long result) {
                if (result != -1) {
                    // Create Notification entry
                    if (notificationDao != null) {
                        new Thread(() -> {
                            com.example.car_booking_app.data.entity.Notification notification = new com.example.car_booking_app.data.entity.Notification(
                                "Booking Confirmed",
                                "Your booking has been successfully placed.",
                                System.currentTimeMillis(),
                                "CONFIRMATION",
                                result.intValue()
                            );
                            notificationDao.insert(notification);
                        }).start();
                    }
                    bookingStatus.postValue("Booking Successful");
                } else {
                    bookingStatus.postValue("Booking Failed: Database Error (Check Logcat)");
                }
            }
        });
    }

    public LiveData<String> getBookingStatus() {
        return bookingStatus;
    }

    public void submitRating(int bookingId, int carId, float rating) {
        bookingRepository.submitRating(bookingId, carId, rating);
    }
}
