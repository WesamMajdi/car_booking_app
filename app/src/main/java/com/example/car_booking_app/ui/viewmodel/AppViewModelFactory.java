package com.example.car_booking_app.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.car_booking_app.data.repository.BookingRepository;
import com.example.car_booking_app.data.repository.CarRepository;
import com.example.car_booking_app.data.repository.UserRepository;

public class AppViewModelFactory implements ViewModelProvider.Factory {
    private UserRepository userRepository;
    private CarRepository carRepository;
    private BookingRepository bookingRepository;
    private com.example.car_booking_app.data.dao.NotificationDao notificationDao;

    private static AppViewModelFactory instance;

    public static AppViewModelFactory getInstance(android.app.Application application) {
        if (instance == null) {
            com.example.car_booking_app.data.database.CarDatabase db = com.example.car_booking_app.data.database.CarDatabase.getDatabase(application);
            com.example.car_booking_app.data.repository.UserRepository userRepository = new com.example.car_booking_app.data.repository.UserRepository(db.userDao());
            com.example.car_booking_app.data.repository.CarRepository carRepository = new com.example.car_booking_app.data.repository.CarRepository(db.carDao());
            com.example.car_booking_app.data.repository.BookingRepository bookingRepository = new com.example.car_booking_app.data.repository.BookingRepository(db.bookingDao(), db.carDao());
            instance = new AppViewModelFactory(userRepository, carRepository, bookingRepository, db.notificationDao());
        }
        return instance;
    }

    public AppViewModelFactory(UserRepository userRepository, CarRepository carRepository, BookingRepository bookingRepository, com.example.car_booking_app.data.dao.NotificationDao notificationDao) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
        this.notificationDao = notificationDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(CarViewModel.class)) {
            return (T) new CarViewModel(carRepository);
        }
        if (modelClass.isAssignableFrom(BookingViewModel.class)) {
            return (T) new BookingViewModel(bookingRepository, userRepository, notificationDao);
        }
        if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            return (T) new NotificationViewModel(notificationDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
