package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.car_booking_app.R;
import com.example.car_booking_app.ui.adapter.BookingAdapter;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.BookingViewModel;
import com.example.car_booking_app.util.SessionManager;

public class PastBookingsFragment extends Fragment {

    private BookingViewModel bookingViewModel;
    private BookingAdapter adapter;
    private TextView tvNoBookings;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookings_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_bookings);
        tvNoBookings = view.findViewById(R.id.tv_no_bookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnRateClickListener(booking -> {
            showRatingDialog(booking);
        });

        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        bookingViewModel = new ViewModelProvider(this, factory).get(BookingViewModel.class);
        SessionManager sessionManager = new SessionManager(requireContext());

        bookingViewModel.getPastBookings(sessionManager.getUserId()).observe(getViewLifecycleOwner(), bookings -> {
            if (bookings != null && !bookings.isEmpty()) {
                adapter.setBookings(bookings);
                tvNoBookings.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvNoBookings.setVisibility(View.VISIBLE);
                tvNoBookings.setText(R.string.no_past_bookings);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showRatingDialog(com.example.car_booking_app.data.entity.Booking booking) {
        android.view.View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating, null);
        android.widget.RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton(getString(R.string.submit), (dialog, which) -> {
                float rating = ratingBar.getRating();
                if (rating > 0) {
                    bookingViewModel.submitRating(booking.id, booking.carId, rating);
                    android.widget.Toast.makeText(requireContext(), getString(R.string.update_success), android.widget.Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(getString(R.string.cancel), null)
            .show();
    }
}