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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.car_booking_app.R;
import com.example.car_booking_app.ui.adapter.CarAdapter;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.CarViewModel;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private CarViewModel carViewModel;
    private CarAdapter adapter;
    private View emptyStateLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_favorites);
        emptyStateLayout = view.findViewById(R.id.layout_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CarAdapter();
        adapter.setOnCarClickListener(car -> {
            Bundle bundle = new Bundle();
            bundle.putInt("carId", car.id);
            Navigation.findNavController(view).navigate(R.id.action_favoritesFragment_to_carDetailFragment, bundle);
        });
        
        adapter.setOnFavoriteClickListener(car -> {
            carViewModel.toggleFavorite(car);
        });
        
        recyclerView.setAdapter(adapter);

        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        carViewModel = new ViewModelProvider(this, factory).get(CarViewModel.class);

        carViewModel.getFavoriteCars().observe(getViewLifecycleOwner(), cars -> {
            if (cars != null && !cars.isEmpty()) {
                adapter.setCars(cars);
                emptyStateLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                emptyStateLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}