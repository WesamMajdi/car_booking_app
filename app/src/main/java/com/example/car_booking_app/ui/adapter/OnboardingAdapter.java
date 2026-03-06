package com.example.car_booking_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.car_booking_app.R;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private List<OnboardingItem> items;

    public OnboardingAdapter(List<OnboardingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardingItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textTitle;
        private TextView textDescription;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_onboarding_image);
            textTitle = itemView.findViewById(R.id.tv_onboarding_title);
            textDescription = itemView.findViewById(R.id.tv_onboarding_description);
        }

        public void bind(OnboardingItem item) {
            textTitle.setText(item.getTitle());
            textDescription.setText(item.getDescription());
            
            com.bumptech.glide.Glide.with(itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imageView);
        }
    }

    public static class OnboardingItem {
        private String imageUrl;
        private String title;
        private String description;

        public OnboardingItem(String imageUrl, String title, String description) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.description = description;
        }

        public String getImageUrl() { return imageUrl; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
}
