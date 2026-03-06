package com.example.car_booking_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.car_booking_app.R;
import com.example.car_booking_app.data.entity.BookingWithCar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<BookingWithCar> bookings = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private OnRateClickListener rateClickListener;

    public interface OnRateClickListener {
        void onRateClick(com.example.car_booking_app.data.entity.Booking booking);
    }

    public void setOnRateClickListener(OnRateClickListener listener) {
        this.rateClickListener = listener;
    }

    public void setBookings(List<BookingWithCar> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingWithCar booking = bookings.get(position);
        if (booking != null) {
            holder.bind(booking);
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDates, tvStatus, tvPrice;
        ImageView ivCar;
        com.google.android.material.button.MaterialButton btnRate;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_booking_car_name);
            tvDates = itemView.findViewById(R.id.tv_booking_dates);
            tvStatus = itemView.findViewById(R.id.tv_booking_status);
            tvPrice = itemView.findViewById(R.id.tv_booking_price);
            ivCar = itemView.findViewById(R.id.iv_booking_car);
            btnRate = itemView.findViewById(R.id.btn_rate);
        }

        public void bind(BookingWithCar item) {
            if (item.booking == null) return;

            long now = System.currentTimeMillis();
            if (item.booking.endDate < now && item.booking.rating == 0) {
                btnRate.setVisibility(View.VISIBLE);
                btnRate.setOnClickListener(v -> {
                    if (rateClickListener != null) {
                        rateClickListener.onRateClick(item.booking);
                    }
                });
            } else {
                btnRate.setVisibility(View.GONE);
            }

            if (item.car != null) {
                tvName.setText(item.car.name);
                // Load image with Glide
                com.bumptech.glide.Glide.with(itemView.getContext())
                        .load(item.car.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error)
                        .into(ivCar);
            } else {
                tvName.setText(itemView.getContext().getString(R.string.car_number, item.booking.carId));
                ivCar.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            
            String start = dateFormat.format(new Date(item.booking.startDate));
            String end = dateFormat.format(new Date(item.booking.endDate));
            tvDates.setText(itemView.getContext().getString(R.string.booking_dates, start, end));
            
            String rawStatus = item.booking.status != null ? item.booking.status : "unknown";
            String displayStatus;
            
            if ("pending".equalsIgnoreCase(rawStatus)) {
                displayStatus = itemView.getContext().getString(R.string.booking_status_pending);
            } else if ("active".equalsIgnoreCase(rawStatus) || "confirmed".equalsIgnoreCase(rawStatus)) {
                displayStatus = itemView.getContext().getString(R.string.booking_status_active);
            } else if ("completed".equalsIgnoreCase(rawStatus)) {
                displayStatus = itemView.getContext().getString(R.string.booking_status_completed);
            } else if ("cancelled".equalsIgnoreCase(rawStatus)) {
                displayStatus = itemView.getContext().getString(R.string.booking_status_cancelled);
            } else {
                displayStatus = itemView.getContext().getString(R.string.unknown);
            }

            tvStatus.setText(itemView.getContext().getString(R.string.booking_status, displayStatus));
            tvPrice.setText(itemView.getContext().getString(R.string.total_price, String.format(Locale.getDefault(), "%.2f", item.booking.totalPrice)));
            
            // Set status background tint
            tvStatus.setBackgroundResource(R.drawable.bg_status_chip);
            
            int color;
            if ("active".equalsIgnoreCase(rawStatus) || "confirmed".equalsIgnoreCase(rawStatus)) {
                color = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_dark);
            } else if ("pending".equalsIgnoreCase(rawStatus)) {
                color = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_orange_dark);
            } else if ("completed".equalsIgnoreCase(rawStatus)) {
                color = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_blue_dark);
            } else {
                color = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
            }
            tvStatus.getBackground().setTint(color);
        }
    }
}
