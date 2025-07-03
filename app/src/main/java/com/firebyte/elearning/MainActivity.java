package com.firebyte.elearning;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firebyte.elearning.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menampilkan fragment Jadwal sebagai halaman default saat pertama kali dibuka
        if (savedInstanceState == null) {
            replaceFragment(new ScheduleFragment());
        }

        // Mengatur listener untuk menu navigasi bawah
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_schedule) {
                    replaceFragment(new ScheduleFragment());
                } else if (itemId == R.id.nav_material) {
                    replaceFragment(new MaterialFragment());
                } else if (itemId == R.id.nav_profile) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });
    }

    // Method untuk mengganti fragment di dalam FrameLayout
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
