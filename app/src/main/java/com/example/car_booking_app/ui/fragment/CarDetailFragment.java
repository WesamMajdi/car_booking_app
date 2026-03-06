package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.core.util.Pair;

import com.example.car_booking_app.R;
import com.example.car_booking_app.data.entity.Car;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.BookingViewModel;
import com.example.car_booking_app.ui.viewmodel.CarViewModel;
import com.example.car_booking_app.util.SessionManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CarDetailFragment extends Fragment {

    private CarViewModel carViewModel;
    private BookingViewModel bookingViewModel;
    private SessionManager sessionManager;
    private int carId;
    private Car currentCar;
    private long startDate = 0;
    private long endDate = 0;

    private ImageView ivCarImage;
    private TextView tvCarName, tvCarPrice, tvCarDescription, tvDates, tvRatingCount;
    private android.widget.RatingBar ratingBar;
    private Button btnSelectDates, btnBookNow;
    private android.widget.ImageButton btnFavorite;
    private Chip chipModel, chipTransmission, chipFuel, chipSeats;

    private final androidx.activity.result.ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.RequestPermission(), isGranted -> {
                // Permission granted or denied. We can continue.
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carId = getArguments().getInt("carId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Request notification permission for Android 13+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), 
                    android.Manifest.permission.POST_NOTIFICATIONS) != 
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        sessionManager = new SessionManager(requireContext());
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        carViewModel = new ViewModelProvider(this, factory).get(CarViewModel.class);
        bookingViewModel = new ViewModelProvider(this, factory).get(BookingViewModel.class);

        ivCarImage = view.findViewById(R.id.iv_detail_image);
        tvCarName = view.findViewById(R.id.tv_detail_name);
        tvCarPrice = view.findViewById(R.id.tv_detail_price);
        tvCarDescription = view.findViewById(R.id.tv_detail_description);
        tvDates = view.findViewById(R.id.tv_selected_dates);
        ratingBar = view.findViewById(R.id.rating_bar_detail);
        tvRatingCount = view.findViewById(R.id.tv_rating_count);
        btnSelectDates = view.findViewById(R.id.btn_select_dates);
        btnBookNow = view.findViewById(R.id.fab_book_now);
        btnFavorite = view.findViewById(R.id.btn_favorite);
        
        chipModel = view.findViewById(R.id.chip_model);
        chipTransmission = view.findViewById(R.id.chip_transmission);
        chipFuel = view.findViewById(R.id.chip_fuel);
        chipSeats = view.findViewById(R.id.chip_seats);

        carViewModel.getCarById(carId);
        carViewModel.getSelectedCar().observe(getViewLifecycleOwner(), car -> {
            if (car != null) {
                currentCar = car;
                tvCarName.setText(car.name);
                tvCarPrice.setText(getString(R.string.price_per_day, String.valueOf(car.pricePerDay)));
                tvCarDescription.setText(car.description != null ? car.description : getString(R.string.description));
                
                if (car.ratingCount > 0) {
                    ratingBar.setVisibility(View.VISIBLE);
                    tvRatingCount.setVisibility(View.VISIBLE);
                    ratingBar.setRating(car.averageRating);
                    tvRatingCount.setText("(" + car.ratingCount + ")");
                } else {
                    ratingBar.setVisibility(View.GONE);
                    tvRatingCount.setVisibility(View.GONE);
                }

                if (car.model != null) chipModel.setText(getString(R.string.model) + ": " + car.model);
                if (car.transmission != null) chipTransmission.setText(car.transmission);
                if (car.fuelType != null) chipFuel.setText(car.fuelType);
                chipSeats.setText(car.seats + " " + getString(R.string.seats));

                if (car.isFavorite) {
                    btnFavorite.setImageResource(R.drawable.ic_heart_filled);
                    btnFavorite.setColorFilter(requireContext().getResources().getColor(R.color.error));
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_heart_outline);
                    btnFavorite.setColorFilter(requireContext().getResources().getColor(android.R.color.darker_gray));
                }

                com.bumptech.glide.Glide.with(requireContext())
                        .load(car.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error)
                        .into(ivCarImage);
            }
        });

        btnSelectDates.setOnClickListener(v -> showDatePicker());
        
        btnFavorite.setOnClickListener(v -> {
            if (currentCar != null) {
                carViewModel.toggleFavorite(currentCar);
                
                // Update UI immediately
                if (currentCar.isFavorite) {
                    btnFavorite.setImageResource(R.drawable.ic_heart_filled);
                    btnFavorite.setColorFilter(requireContext().getResources().getColor(R.color.error));
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_heart_outline);
                    btnFavorite.setColorFilter(requireContext().getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        btnBookNow.setOnClickListener(v -> {
            if (currentCar == null) return;
            if (startDate == 0 || endDate == 0) {
                Toast.makeText(requireContext(), getString(R.string.select_dates), Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(requireContext(), getString(R.string.login_required), Toast.LENGTH_SHORT).show();
                return;
            }

            showBookingConfirmationDialog();
        });

        bookingViewModel.getBookingStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                String message = status;
                if (status.contains("Successful")) {
                    message = getString(R.string.booking_success);
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    
                    // Show Notification
                    com.example.car_booking_app.util.NotificationHelper.showNotification(
                        requireContext(),
                        "Booking Confirmed",
                        "Your booking has been successfully placed.",
                        (int) System.currentTimeMillis()
                    );

                    // Navigate to home or my bookings
                    Navigation.findNavController(view).navigate(R.id.action_carDetailFragment_to_myBookingsFragment);
                } else if (status.contains("Failed")) {
                    message = getString(R.string.booking_failed);
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBookingConfirmationDialog() {
        if (currentCar == null) return;

        long diff = endDate - startDate;
        long days = diff / (1000 * 60 * 60 * 24);
        if (days < 1) days = 1;
        double total = days * currentCar.pricePerDay;

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String dateRange = sdf.format(new Date(startDate)) + " - " + sdf.format(new Date(endDate));

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_booking))
            .setMessage(getString(R.string.booking_summary) + "\n\n" +
                        getString(R.string.car) + ": " + currentCar.name + "\n" +
                        getString(R.string.dates) + ": " + dateRange + "\n" +
                        getString(R.string.duration) + ": " + days + " " + getString(R.string.days) + "\n" +
                        getString(R.string.total_price) + ": " + String.format(Locale.getDefault(), "$%.2f", total))
            .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                showPaymentDialog();
            })
            .setNegativeButton(getString(R.string.cancel), null)
            .show();
    }

    private void showPaymentDialog() {
        android.view.View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_payment, null);
        
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.payment_details))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.pay_now), (dialog, which) -> {
                // Simulate payment processing
                android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(requireContext());
                progressDialog.setMessage(getString(R.string.processing_payment));
                progressDialog.setCancelable(false);
                progressDialog.show();
                
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    progressDialog.dismiss();
                    // Toast.makeText(requireContext(), getString(R.string.payment_success), Toast.LENGTH_SHORT).show();
                    
                    // Calculate actual end date based on days to ensure at least 1 day duration
                    long diff = endDate - startDate;
                    long days = diff / (1000 * 60 * 60 * 24);
                    if (days < 1) days = 1;
                    long actualEndDate = startDate + (days * 24 * 60 * 60 * 1000);

                    bookingViewModel.createBooking(
                        sessionManager.getUserId(),
                        currentCar.id,
                        startDate,
                        actualEndDate,
                        currentCar.pricePerDay
                    );
                }, 2000);
            })
            .setNegativeButton(getString(R.string.cancel), null)
            .show();
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText(getString(R.string.select_dates));
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getChildFragmentManager(), "DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            startDate = selection.first;
            endDate = selection.second;
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateRange = sdf.format(new Date(startDate)) + " - " + sdf.format(new Date(endDate));
            tvDates.setText(dateRange);
            
            // Calculate and show total price if needed
            if (currentCar != null) {
                long diff = endDate - startDate;
                long days = diff / (1000 * 60 * 60 * 24);
                if (days < 1) days = 1;
                double total = days * currentCar.pricePerDay;
                btnBookNow.setText(getString(R.string.book_now) + " - " + String.format(Locale.getDefault(), "$%.2f", total));
            }
        });
    }
}
