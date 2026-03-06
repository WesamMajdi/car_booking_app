package com.example.car_booking_app.data.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.car_booking_app.data.dao.BookingDao;
import com.example.car_booking_app.data.dao.CarDao;
import com.example.car_booking_app.data.dao.UserDao;
import com.example.car_booking_app.data.dao.NotificationDao;
import com.example.car_booking_app.data.entity.Booking;
import com.example.car_booking_app.data.entity.Car;
import com.example.car_booking_app.data.entity.Notification;
import com.example.car_booking_app.data.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Car.class, Booking.class, Notification.class}, version = 8, exportSchema = false)
public abstract class CarDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CarDao carDao();
    public abstract BookingDao bookingDao();
    public abstract NotificationDao notificationDao();

    private static volatile CarDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CarDatabase.class, "car_booking_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CarDao dao = INSTANCE.carDao();
                populateDatabase(dao);
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                CarDao dao = INSTANCE.carDao();
                if (dao.getCount() == 0) {
                    populateDatabase(dao);
                }
            });
        }
    };

    private static void populateDatabase(CarDao dao) {
        // Only populate if empty to avoid wiping user data/bookings on restart
        if (dao.getCount() > 0) return;

        List<Car> cars = new ArrayList<>();
        // Manually assign IDs to ensure stability across app restarts for testing
        // Note: In production, we'd rely on auto-increment, but for this "fake data" setup, stability helps.
        // We can't easily set auto-generated ID in constructor, so we'll just insert and let SQLite handle it.
        // But to prevent data loss, we removed deleteAllCars().
        
        cars.add(new Car("Tesla Model S", "Model S", 2024, "Electric luxury sedan with high performance.", 150.0, "https://images.unsplash.com/photo-1617788138017-80ad40651399?w=500", "Luxury", true, "Automatic", "Electric", 5, false));
        cars.add(new Car("Toyota Camry", "Camry", 2023, "Reliable and fuel-efficient hybrid sedan.", 60.0, "https://images.unsplash.com/photo-1621007947382-bb3c3968e3bb?w=500", "Sedan", true, "Automatic", "Hybrid", 5, false));
        cars.add(new Car("Ford Mustang", "GT", 2022, "Classic American muscle car.", 120.0, "https://images.unsplash.com/photo-1584345604476-8ec5e12e42dd?w=500", "Sports", true, "Manual", "Petrol", 4, false));
        cars.add(new Car("Honda CR-V", "CR-V", 2023, "Spacious SUV for family trips.", 80.0, "https://images.unsplash.com/photo-1568844293986-8d0400bd4745?w=500", "SUV", true, "Automatic", "Petrol", 5, false));
        cars.add(new Car("BMW 3 Series", "3 Series", 2023, "Luxury compact executive car.", 110.0, "https://images.unsplash.com/photo-1555215695-3004980adade?w=500", "Luxury", true, "Automatic", "Diesel", 5, false));
        cars.add(new Car("Audi A4", "A4", 2023, "Sophisticated design and technology.", 105.0, "https://images.unsplash.com/photo-1606152421802-db97b9c7a11b?w=500", "Sedan", true, "Automatic", "Petrol", 5, false));
        cars.add(new Car("Mercedes C-Class", "C-Class", 2024, "Elegant and comfortable.", 130.0, "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=500", "Luxury", true, "Automatic", "Hybrid", 5, false));
        cars.add(new Car("Chevrolet Camaro", "SS", 2022, "High-performance sports car.", 115.0, "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=500", "Sports", true, "Manual", "Petrol", 4, false));

        dao.insertAll(cars);
    }
}
