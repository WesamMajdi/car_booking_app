package com.example.car_booking_app.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.car_booking_app.R;
import com.example.car_booking_app.data.dao.BookingDao;
import com.example.car_booking_app.data.dao.CarDao;
import com.example.car_booking_app.data.dao.NotificationDao;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.Booking;
import com.example.car_booking_app.data.entity.Car;
import com.example.car_booking_app.data.entity.Notification;

import java.util.List;

public class BookingNotificationWorker extends Worker {

    private BookingDao bookingDao;
    private NotificationDao notificationDao;
    private CarDao carDao;
    private static final String CHANNEL_ID = "booking_notifications";

    public BookingNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        CarDatabase db = CarDatabase.getDatabase(context);
        bookingDao = db.bookingDao();
        notificationDao = db.notificationDao();
        carDao = db.carDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            List<Booking> activeBookings = bookingDao.getAllActiveBookingsSync();
            long currentTime = System.currentTimeMillis();
            long oneHour = 60 * 60 * 1000;

            for (Booking booking : activeBookings) {
                long startTime = booking.startDate;
                long endTime = booking.endDate;

                Car car = carDao.getCarById(booking.carId);
                String carName = (car != null) ? car.name : "Car #" + booking.carId;

                // 1. One hour before start
                if (startTime > currentTime && (startTime - currentTime) <= oneHour) {
                    checkAndNotify(booking.id, "START_REMINDER",
                            "Upcoming Booking",
                            "Your booking for " + carName + " starts in less than 1 hour.");
                }

                // 2. One hour before end
                if (endTime > currentTime && (endTime - currentTime) <= oneHour) {
                    checkAndNotify(booking.id, "END_REMINDER",
                            "Booking Ending Soon",
                            "Your booking for " + carName + " ends in less than 1 hour.");
                }

                // 3. Overdue or Completed check
                if (currentTime >= endTime) {
                    long fifteenMins = 15 * 60 * 1000;
                    if ((currentTime - endTime) > fifteenMins) {
                         // Overdue (more than 15 mins late)
                        checkAndNotify(booking.id, "OVERDUE",
                                "Booking Overdue",
                                "Your booking for " + carName + " is overdue. Please return the car.");
                    } else {
                        // Just finished (within 15 mins window)
                        checkAndNotify(booking.id, "COMPLETED_ALERT",
                                "Booking Completed",
                                "Your booking for " + carName + " has ended.");
                    }
                }
            }

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private void checkAndNotify(int bookingId, String type, String title, String message) {
        Notification existing = notificationDao.getNotificationForBooking(bookingId, type);
        if (existing == null) {
            // Save to DB for app history
            Notification notification = new Notification(title, message, System.currentTimeMillis(), type, bookingId);
            notificationDao.insert(notification);

            // Show System Notification
            showSystemNotification(title, message, bookingId + type.hashCode());
        }
    }

    private void showSystemNotification(String title, String message, int notificationId) {
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Booking Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Should replace with app icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }
}
