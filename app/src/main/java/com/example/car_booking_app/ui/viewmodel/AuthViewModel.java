package com.example.car_booking_app.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.car_booking_app.data.entity.User;
import com.example.car_booking_app.data.repository.RepositoryCallback;
import com.example.car_booking_app.data.repository.UserRepository;

public class AuthViewModel extends ViewModel {
    private UserRepository repository;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();

    public AuthViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void register(User user) {
        isLoading.setValue(true);
        repository.registerUser(user, new RepositoryCallback<Long>() {
            @Override
            public void onComplete(Long result) {
                isLoading.postValue(false);
                if (result != -1L) {
                    currentUser.postValue(user);
                    error.postValue(null);
                } else {
                    error.postValue("Registration failed");
                }
            }
        });
    }

    public void login(String email, String password) {
        isLoading.setValue(true);
        repository.loginUser(email, new RepositoryCallback<User>() {
            @Override
            public void onComplete(User user) {
                isLoading.postValue(false);
                if (user != null && user.password.equals(password)) {
                    currentUser.postValue(user);
                    error.postValue(null);
                } else {
                    error.postValue("Invalid email or password");
                }
            }
        });
    }

    public void logout() {
        currentUser.setValue(null);
    }

    public void updateUserProfile(int userId, String name, String phone) {
        isLoading.setValue(true);
        repository.getUserById(userId, new RepositoryCallback<User>() {
            @Override
            public void onComplete(User user) {
                if (user != null) {
                    user.name = name;
                    user.phone = phone;
                    repository.updateUser(user, new RepositoryCallback<Boolean>() {
                        @Override
                        public void onComplete(Boolean success) {
                            isLoading.postValue(false);
                            if (success) {
                                currentUser.postValue(user);
                                error.postValue(null);
                            } else {
                                error.postValue("Update failed");
                            }
                        }
                    });
                } else {
                    isLoading.postValue(false);
                    error.postValue("User not found");
                }
            }
        });
    }

    public void changePassword(int userId, String oldPassword, String newPassword) {
        isLoading.setValue(true);
        repository.getUserById(userId, new RepositoryCallback<User>() {
            @Override
            public void onComplete(User user) {
                if (user != null) {
                    if (user.password.equals(oldPassword)) {
                        user.password = newPassword;
                        repository.updateUser(user, new RepositoryCallback<Boolean>() {
                            @Override
                            public void onComplete(Boolean success) {
                                isLoading.postValue(false);
                                if (success) {
                                    error.postValue(null); // Success
                                } else {
                                    error.postValue("Password update failed");
                                }
                            }
                        });
                    } else {
                        isLoading.postValue(false);
                        error.postValue("Incorrect old password");
                    }
                } else {
                    isLoading.postValue(false);
                    error.postValue("User not found");
                }
            }
        });
    }

    public void checkSession(int userId, RepositoryCallback<Boolean> callback) {
        repository.getUserById(userId, new RepositoryCallback<User>() {
            @Override
            public void onComplete(User user) {
                callback.onComplete(user != null);
            }
        });
    }
}
