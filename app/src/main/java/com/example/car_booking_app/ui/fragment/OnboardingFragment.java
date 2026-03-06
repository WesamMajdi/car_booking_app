package com.example.car_booking_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.example.car_booking_app.R;
import com.example.car_booking_app.ui.adapter.OnboardingAdapter;
import com.example.car_booking_app.util.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class OnboardingFragment extends Fragment {

    private ViewPager2 viewPager;
    private Button btnNext, btnSkip;
    private OnboardingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        btnNext = view.findViewById(R.id.btn_next);
        btnSkip = view.findViewById(R.id.btn_skip);

        List<OnboardingAdapter.OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingAdapter.OnboardingItem(
                "https://images.unsplash.com/photo-1552519507-da8b1227ad38?q=80&w=800&auto=format&fit=crop",
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1)
        ));
        items.add(new OnboardingAdapter.OnboardingItem(
                "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?q=80&w=800&auto=format&fit=crop",
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2)
        ));
        items.add(new OnboardingAdapter.OnboardingItem(
                "https://images.unsplash.com/photo-1503376763036-066120622c74?q=80&w=800&auto=format&fit=crop",
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3)
        ));

        adapter = new OnboardingAdapter(items);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == items.size() - 1) {
                    btnNext.setText(R.string.start);
                } else {
                    btnNext.setText(R.string.next);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < items.size() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        SessionManager sessionManager = new SessionManager(requireContext());
        sessionManager.setFirstRun(false);
        Navigation.findNavController(requireView()).navigate(R.id.action_onboardingFragment_to_loginFragment);
    }
}
