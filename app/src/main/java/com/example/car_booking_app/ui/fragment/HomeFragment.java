package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.car_booking_app.R;
import com.example.car_booking_app.data.entity.Car;
import com.example.car_booking_app.ui.adapter.CarAdapter;
import com.example.car_booking_app.ui.adapter.FeaturedCarAdapter;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.CarViewModel;
import com.example.car_booking_app.util.SessionManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CarViewModel carViewModel;
    private CarAdapter adapter;
    private FeaturedCarAdapter featuredAdapter;
    private SessionManager sessionManager;
    private ChipGroup chipGroupCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        TextView tvWelcome = view.findViewById(R.id.tv_welcome);
        tvWelcome.setText(sessionManager.getUserName());
        
        // Make welcome text clickable to go to profile
        view.findViewById(R.id.tv_welcome_label).setOnClickListener(v -> navigateToProfile());
        tvWelcome.setOnClickListener(v -> navigateToProfile());
        
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        carViewModel = new ViewModelProvider(this, factory).get(CarViewModel.class);

        // Search Functionality
        EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carViewModel.onSearchQueryChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter Button
        view.findViewById(R.id.btn_filter).setOnClickListener(v -> showFilterBottomSheet());

        // Featured Slider
        ViewPager2 viewPager = view.findViewById(R.id.viewPager_featured);
        featuredAdapter = new FeaturedCarAdapter();
        featuredAdapter.setOnFeaturedCarClickListener(car -> {
            Bundle bundle = new Bundle();
            bundle.putInt("carId", car.id);
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_carDetailFragment, bundle);
        });
        viewPager.setAdapter(featuredAdapter);

        // All Cars List
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_cars);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CarAdapter();
        recyclerView.setAdapter(adapter);
        
        // Chip Group for Categories
        chipGroupCategories = view.findViewById(R.id.chip_group_categories);
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            String category = null;
            if (checkedId == R.id.chip_family) {
                category = "Sedan";
            } else if (checkedId == R.id.chip_suv) {
                category = "SUV";
            } else if (checkedId == R.id.chip_luxury) {
                category = "Luxury";
            } else if (checkedId == R.id.chip_sports) {
                category = "Sports";
            }
            // For chip_all or any other case, category remains null
            carViewModel.onCategorySelected(category);
        });

        // Click Listener for Car Item
        adapter.setOnCarClickListener(car -> {
            Bundle bundle = new Bundle();
            bundle.putInt("carId", car.id);
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_carDetailFragment, bundle);
        });
        
        adapter.setOnFavoriteClickListener(car -> {
            carViewModel.toggleFavorite(car);
        });

        // Observe Filtered Cars for the main list
        carViewModel.getCars().observe(getViewLifecycleOwner(), cars -> {
            adapter.setCars(cars);
        });
        
        // Observe All Cars for Featured Section (independent of search/filter)
        carViewModel.getAllCars().observe(getViewLifecycleOwner(), cars -> {
            if (cars != null && !cars.isEmpty()) {
                List<Car> featured = new ArrayList<>();
                // Logic for "Featured": take first 5 cars or expensive ones
                // For now, just take top 5
                for (int i = 0; i < Math.min(5, cars.size()); i++) {
                    featured.add(cars.get(i));
                }
                featuredAdapter.setCars(featured);
            }
        });


    }
    
    private void navigateToProfile() {
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_profileFragment);
    }

    private void showFilterBottomSheet() {
        com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = 
            new com.google.android.material.bottomsheet.BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_sheet_filter, null);
            
        com.google.android.material.slider.RangeSlider sliderPrice = bottomSheetView.findViewById(R.id.slider_price);
        ChipGroup chipGroupTransmission = bottomSheetView.findViewById(R.id.chip_group_transmission);
        ChipGroup chipGroupFuel = bottomSheetView.findViewById(R.id.chip_group_fuel);
        ChipGroup chipGroupSeats = bottomSheetView.findViewById(R.id.chip_group_seats);
        ChipGroup chipGroupYear = bottomSheetView.findViewById(R.id.chip_group_year);
        com.google.android.material.switchmaterial.SwitchMaterial switchAvailable = bottomSheetView.findViewById(R.id.switch_available_only);
        com.google.android.material.button.MaterialButton btnApply = bottomSheetView.findViewById(R.id.btn_apply_filter);
        
        btnApply.setOnClickListener(v -> {
            List<Float> values = sliderPrice.getValues();
            Double minPrice = null;
            Double maxPrice = null;
            if (values.size() >= 2) {
                minPrice = values.get(0).doubleValue();
                maxPrice = values.get(1).doubleValue();
            }
            
            String transmission = null;
            int transId = chipGroupTransmission.getCheckedChipId();
            if (transId == R.id.chip_trans_auto) {
                transmission = "Automatic";
            } else if (transId == R.id.chip_trans_manual) {
                transmission = "Manual";
            }
            
            String fuel = null;
            int fuelId = chipGroupFuel.getCheckedChipId();
            if (fuelId == R.id.chip_fuel_petrol) {
                fuel = "Petrol";
            } else if (fuelId == R.id.chip_fuel_diesel) {
                fuel = "Diesel";
            } else if (fuelId == R.id.chip_fuel_electric) {
                fuel = "Electric";
            }

            Integer seats = null;
            int seatsId = chipGroupSeats.getCheckedChipId();
            if (seatsId == R.id.chip_seats_4) {
                seats = 4;
            } else if (seatsId == R.id.chip_seats_5) {
                seats = 5;
            } else if (seatsId == R.id.chip_seats_7) {
                seats = 7;
            }

            Integer year = null;
            int yearId = chipGroupYear.getCheckedChipId();
            if (yearId == R.id.chip_year_2020) {
                year = 2020;
            } else if (yearId == R.id.chip_year_2022) {
                year = 2022;
            } else if (yearId == R.id.chip_year_2023) {
                year = 2023;
            }
            
            boolean isAvailable = switchAvailable.isChecked();
            
            carViewModel.setAdvancedFilters(minPrice, maxPrice, transmission, fuel, seats, year, isAvailable);
            bottomSheetDialog.dismiss();
        });
        
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
