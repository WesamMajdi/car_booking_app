package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.car_booking_app.R;
import com.example.car_booking_app.data.database.CarDatabase;
import com.example.car_booking_app.data.entity.User;
import com.example.car_booking_app.data.repository.BookingRepository;
import com.example.car_booking_app.data.repository.CarRepository;
import com.example.car_booking_app.data.repository.UserRepository;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.AuthViewModel;

import com.example.car_booking_app.util.SessionManager;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    private SessionManager sessionManager;
    private EditText etName, etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnRegister;
    private TextView tvLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModel
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etPhone = view.findViewById(R.id.et_phone);
        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(name, email, password, phone, "customer");
            authViewModel.register(user);
        });

        tvLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
        });

        // Observe registration result
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                sessionManager.createLoginSession(user.id, user.name, user.email, user.phone);
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_homeFragment);
            }
        });
    }
}
