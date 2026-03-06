package com.example.car_booking_app.data.repository;

import androidx.lifecycle.LiveData;
import com.example.car_booking_app.data.dao.CarDao;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.Car;
import java.util.List;

public class CarRepository {
    private CarDao carDao;
    private LiveData<List<Car>> allCars;

    public CarRepository(CarDao carDao) {
        this.carDao = carDao;
        this.allCars = carDao.getAllCars();
    }

    public LiveData<List<Car>> getAllCars() {
        return allCars;
    }

    public LiveData<List<Car>> getCarsByCategory(String category) {
        return carDao.getCarsByCategory(category);
    }

    public LiveData<List<Car>> getFavoriteCars() {
        return carDao.getFavoriteCars();
    }

    public void getCarById(int id, final RepositoryCallback<Car> callback) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            Car car = carDao.getCarById(id);
            if (callback != null) {
                callback.onComplete(car);
            }
        });
    }

    public void insertCar(Car car) {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            carDao.insertCar(car);
        });
    }
}
