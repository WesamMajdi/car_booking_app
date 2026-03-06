package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.car_booking_app.R;
import com.example.car_booking_app.ui.viewmodel.AppViewModelFactory;
import com.example.car_booking_app.ui.viewmodel.AuthViewModel;
import com.example.car_booking_app.util.SessionManager;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Animation
        View logo = view.findViewById(R.id.iv_logo);
        View title = view.findViewById(R.id.tv_title);
        View subtitle = view.findViewById(R.id.tv_subtitle);

        logo.setAlpha(0f);
        logo.setScaleX(0.5f);
        logo.setScaleY(0.5f);
        logo.setTranslationY(100f);
        
        title.setAlpha(0f);
        title.setTranslationY(50f);
        
        subtitle.setAlpha(0f);
        subtitle.setTranslationY(50f);

        // Logo animation: Scale up + Fade in + Move up
        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .translationY(0f)
            .setDuration(1200)
            .setInterpolator(new android.view.animation.OvershootInterpolator())
            .setStartDelay(300)
            .start();

        // Title animation: Fade in + Move up
        title.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1000)
            .setInterpolator(new android.view.animation.DecelerateInterpolator())
            .setStartDelay(800)
            .start();

        // Subtitle animation: Fade in + Move up
        subtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1000)
            .setInterpolator(new android.view.animation.DecelerateInterpolator())
            .setStartDelay(1100)
            .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && view != null) {
                SessionManager sessionManager = new SessionManager(requireContext());
                if (sessionManager.isFirstRun()) {
                    // Check if onboarding action exists, otherwise go home/login
                    try {
                        Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_onboardingFragment);
                    } catch (Exception e) {
                        // Fallback if onboarding is disabled/removed
                         if (sessionManager.isLoggedIn()) {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
                        }
                    }
                } else if (sessionManager.isLoggedIn()) {
                    // Check if user exists in DB
                    AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
                    AuthViewModel authViewModel = new androidx.lifecycle.ViewModelProvider(this, factory).get(AuthViewModel.class);
                    
                    authViewModel.checkSession(sessionManager.getUserId(), exists -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (exists) {
                                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                            } else {
                                sessionManager.logoutUser();
                                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
                            }
                        });
                    });
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
                }
            }
        }, 2500); // 2.5 seconds delay for better experience
    }
}
