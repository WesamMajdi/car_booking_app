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
import java.util.List;

public class FeaturedCarAdapter extends RecyclerView.Adapter<FeaturedCarAdapter.FeaturedCarViewHolder> {

    private List<Car> cars;
    private OnFeaturedCarClickListener listener;

    public interface OnFeaturedCarClickListener {
        void onCarClick(Car car);
    }

    public void setOnFeaturedCarClickListener(OnFeaturedCarClickListener listener) {
        this.listener = listener;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeaturedCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_car, parent, false);
        return new FeaturedCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedCarViewHolder holder, int position) {
        if (cars != null) {
            Car car = cars.get(position);
            holder.bind(car);
        }
    }

    @Override
    public int getItemCount() {
        return cars != null ? cars.size() : 0;
    }

    class FeaturedCarViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textName;
        private TextView textPrice;

        public FeaturedCarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_car_image);
            textName = itemView.findViewById(R.id.tv_car_name);
            textPrice = itemView.findViewById(R.id.tv_car_price);
            
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onCarClick(cars.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Car car) {
            textName.setText(car.name + " " + car.model);
            textPrice.setText("$" + car.pricePerDay + "/day");
            
            com.bumptech.glide.Glide.with(itemView.getContext())
                    .load(car.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView);
        }
    }
}
