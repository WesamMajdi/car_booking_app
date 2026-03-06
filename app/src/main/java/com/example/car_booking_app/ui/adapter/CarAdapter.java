package com.example.car_booking_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.car_booking_app.R;
import com.example.car_booking_app.data.entity.Car;
import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> cars = new ArrayList<>();
    private OnCarClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public interface OnCarClickListener {
        void onCarClick(Car car);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Car car);
    }

    public void setOnCarClickListener(OnCarClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteListener = listener;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.bind(car);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, category, details, rating;
        ImageView image, favorite;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_car_name);
            price = itemView.findViewById(R.id.tv_car_price);
            category = itemView.findViewById(R.id.tv_car_category);
            details = itemView.findViewById(R.id.tv_car_details);
            rating = itemView.findViewById(R.id.tv_car_rating);
            image = itemView.findViewById(R.id.iv_car_image);
            favorite = itemView.findViewById(R.id.iv_favorite);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCarClick(cars.get(position));
                }
            });

            if (favorite != null) {
                favorite.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (favoriteListener != null && position != RecyclerView.NO_POSITION) {
                        favoriteListener.onFavoriteClick(cars.get(position));
                    }
                });
            }
        }

        public void bind(Car car) {
            if (name != null) name.setText(car.name);
            if (price != null) price.setText("$" + car.pricePerDay + "/day");
            if (category != null) category.setText(car.category);
            if (details != null) details.setText(car.model + " • " + car.transmission + " • " + car.fuelType);
            
            if (rating != null) {
                if (car.ratingCount > 0) {
                    rating.setText(String.format(java.util.Locale.getDefault(), "%.1f (%d)", car.averageRating, car.ratingCount));
                    ((View)rating.getParent()).setVisibility(View.VISIBLE);
                } else {
                    ((View)rating.getParent()).setVisibility(View.GONE);
                }
            }

            if (image != null) {
                com.bumptech.glide.Glide.with(itemView.getContext())
                        .load(car.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error)
                        .into(image);
            }

            if (favorite != null) {
                if (car.isFavorite) {
                    favorite.setImageResource(R.drawable.ic_heart_filled);
                    favorite.setColorFilter(itemView.getContext().getResources().getColor(R.color.error));
                } else {
                    favorite.setImageResource(R.drawable.ic_heart_outline);
                    favorite.setColorFilter(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
                }
            }
        }
    }
}
