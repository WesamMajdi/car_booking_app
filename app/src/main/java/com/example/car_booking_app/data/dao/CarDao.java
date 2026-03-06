package com.example.car_booking_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.car_booking_app.data.entity.Car;
import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM cars")
    LiveData<List<Car>> getAllCars();

    @Query("SELECT * FROM cars WHERE category = :category")
    LiveData<List<Car>> getCarsByCategory(String category);

    @Query("SELECT * FROM cars WHERE isFavorite = 1")
    LiveData<List<Car>> getFavoriteCars();

    @Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
    Car getCarById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCar(Car car);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Car> cars);

    @Update
    void updateCar(Car car);

    @Query("UPDATE cars SET averageRating = :averageRating, ratingCount = :ratingCount WHERE id = :id")
    void updateCarRating(int id, float averageRating, int ratingCount);

    @Query("DELETE FROM cars")
    void deleteAllCars();

    @Query("SELECT * FROM cars WHERE " +
           "(:minPrice IS NULL OR pricePerDay >= :minPrice) AND " +
           "(:maxPrice IS NULL OR pricePerDay <= :maxPrice) AND " +
           "(:transmission IS NULL OR transmission = :transmission) AND " +
           "(:fuelType IS NULL OR fuelType = :fuelType) AND " +
           "(:minSeats IS NULL OR seats >= :minSeats)")
    LiveData<List<Car>> searchCars(Double minPrice, Double maxPrice, String transmission, String fuelType, Integer minSeats);

    @Query("SELECT COUNT(*) FROM cars")
    int getCount();
}
