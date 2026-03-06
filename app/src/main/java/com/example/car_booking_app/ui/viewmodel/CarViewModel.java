package com.example.car_booking_app.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.car_booking_app.data.entity.Car;
import com.example.car_booking_app.data.repository.CarRepository;
import com.example.car_booking_app.data.repository.RepositoryCallback;
import java.util.ArrayList;
import java.util.List;

public class CarViewModel extends ViewModel {
    private CarRepository repository;
    private LiveData<List<Car>> allCars;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private MutableLiveData<String> selectedCategory = new MutableLiveData<>(null);
    private MutableLiveData<Double> minPrice = new MutableLiveData<>(null);
    private MutableLiveData<Double> maxPrice = new MutableLiveData<>(null);
    private MutableLiveData<String> transmission = new MutableLiveData<>(null);
    private MutableLiveData<String> fuelType = new MutableLiveData<>(null);
    private MutableLiveData<Integer> minSeats = new MutableLiveData<>(null);
    private MutableLiveData<Integer> minYear = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> availableOnly = new MutableLiveData<>(false);
    private MutableLiveData<Car> selectedCar = new MutableLiveData<>();
    private MediatorLiveData<List<Car>> filteredCars = new MediatorLiveData<>();

    public CarViewModel(CarRepository repository) {
        this.repository = repository;
        this.allCars = repository.getAllCars();

        filteredCars.addSource(allCars, cars -> applyFilters());
        filteredCars.addSource(searchQuery, query -> applyFilters());
        filteredCars.addSource(selectedCategory, category -> applyFilters());
        filteredCars.addSource(minPrice, v -> applyFilters());
        filteredCars.addSource(maxPrice, v -> applyFilters());
        filteredCars.addSource(transmission, v -> applyFilters());
        filteredCars.addSource(fuelType, v -> applyFilters());
        filteredCars.addSource(minSeats, v -> applyFilters());
        filteredCars.addSource(minYear, v -> applyFilters());
        filteredCars.addSource(availableOnly, v -> applyFilters());
    }

    public void setAdvancedFilters(Double minP, Double maxP, String trans, String fuel, Integer seats, Integer year, Boolean available) {
        minPrice.setValue(minP);
        maxPrice.setValue(maxP);
        transmission.setValue(trans);
        fuelType.setValue(fuel);
        minSeats.setValue(seats);
        minYear.setValue(year);
        availableOnly.setValue(available);
    }

    private void applyFilters() {
        List<Car> cars = allCars.getValue();
        if (cars == null) {
            filteredCars.setValue(new ArrayList<>());
            return;
        }

        String query = searchQuery.getValue();
        String category = selectedCategory.getValue();
        Double minP = minPrice.getValue();
        Double maxP = maxPrice.getValue();
        String trans = transmission.getValue();
        String fuel = fuelType.getValue();
        Integer seats = minSeats.getValue();
        Integer year = minYear.getValue();
        Boolean available = availableOnly.getValue();

        List<Car> result = new ArrayList<>();
        for (Car car : cars) {
            boolean matchesQuery = true;
            if (query != null && !query.isEmpty()) {
                matchesQuery = car.name.toLowerCase().contains(query.toLowerCase()) ||
                               (car.model != null && car.model.toLowerCase().contains(query.toLowerCase()));
            }

            boolean matchesCategory = true;
            if (category != null && !category.isEmpty() && !category.equals("All")) {
                matchesCategory = category.equalsIgnoreCase(car.category);
            }

            boolean matchesPrice = true;
            if (minP != null && car.pricePerDay < minP) matchesPrice = false;
            if (maxP != null && car.pricePerDay > maxP) matchesPrice = false;

            boolean matchesTransmission = true;
            if (trans != null && !trans.isEmpty() && !trans.equals("Any")) {
                matchesTransmission = trans.equalsIgnoreCase(car.transmission);
            }

            boolean matchesFuel = true;
            if (fuel != null && !fuel.isEmpty() && !fuel.equals("Any")) {
                matchesFuel = fuel.equalsIgnoreCase(car.fuelType);
            }

            boolean matchesSeats = true;
            if (seats != null && car.seats < seats) matchesSeats = false;

            boolean matchesYear = true;
            if (year != null && car.year < year) matchesYear = false;

            boolean matchesAvailable = true;
            if (Boolean.TRUE.equals(available) && !car.isAvailable) matchesAvailable = false;

            if (matchesQuery && matchesCategory && matchesPrice && matchesTransmission && matchesFuel && matchesSeats && matchesYear && matchesAvailable) {
                result.add(car);
            }
        }
        filteredCars.setValue(result);
    }

    public LiveData<List<Car>> getCars() {
        return filteredCars; // Return filtered list by default
    }
    
    public LiveData<List<Car>> getAllCars() {
        return allCars;
    }

    public LiveData<List<Car>> getFavoriteCars() {
        return repository.getFavoriteCars();
    }
    
    public void onSearchQueryChanged(String query) {
        searchQuery.setValue(query);
    }

    public void onCategorySelected(String category) {
        selectedCategory.setValue(category);
    }

    public LiveData<Car> getSelectedCar() {
        return selectedCar;
    }

    public void selectCar(Car car) {
        selectedCar.setValue(car);
    }

    public void getCarById(int id) {
        repository.getCarById(id, new RepositoryCallback<Car>() {
            @Override
            public void onComplete(Car result) {
                selectedCar.postValue(result);
            }
        });
    }

    public void toggleFavorite(Car car) {
        car.isFavorite = !car.isFavorite;
        repository.insertCar(car);
    }
}
