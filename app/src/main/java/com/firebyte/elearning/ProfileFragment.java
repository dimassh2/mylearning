package com.firebyte.elearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebyte.elearning.databinding.FragmentProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private GoogleSignInClient mGoogleSignInClient;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userRef = db.collection("users").document(currentUser.getUid());
            loadUserProfile();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        binding.logoutButton.setOnClickListener(v -> logout());
        binding.btnEditProfile.setOnClickListener(v -> toggleEditMode(true));
        binding.btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfile() {
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    binding.profileName.setText(user.getNama());
                    binding.profileEmail.setText(user.getEmail());
                    binding.profileNim.setText("NIM: " + (user.getNim().isEmpty() ? "Masukkan Nim!" : user.getNim()));
                    binding.profileJurusan.setText("Jurusan: " + (user.getJurusan().isEmpty() ? "Masukkan Jurusan!" : user.getJurusan()));

                    Glide.with(this)
                            .load(user.getFotoUrl())
                            .placeholder(R.drawable.ic_profile)
                            .circleCrop()
                            .into(binding.profileImage);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Gagal memuat profil", Toast.LENGTH_SHORT).show());
    }

    private void toggleEditMode(boolean isEditing) {
        if (isEditing) {
            // Masuk ke mode edit
            binding.profileName.setVisibility(View.GONE);
            binding.profileNim.setVisibility(View.GONE);
            binding.profileJurusan.setVisibility(View.GONE);
            binding.btnEditProfile.setVisibility(View.GONE);

            binding.editProfileName.setVisibility(View.VISIBLE);
            binding.editProfileNim.setVisibility(View.VISIBLE);
            binding.editProfileJurusan.setVisibility(View.VISIBLE);
            binding.btnSaveProfile.setVisibility(View.VISIBLE);

            // Isi EditText dengan data saat ini
            binding.editProfileName.setText(binding.profileName.getText());
            binding.editProfileNim.setText(binding.profileNim.getText().toString().replace("NIM: ", ""));
            binding.editProfileJurusan.setText(binding.profileJurusan.getText().toString().replace("Jurusan: ", ""));

        } else {
            // Kembali ke mode tampilan
            binding.profileName.setVisibility(View.VISIBLE);
            binding.profileNim.setVisibility(View.VISIBLE);
            binding.profileJurusan.setVisibility(View.VISIBLE);
            binding.btnEditProfile.setVisibility(View.VISIBLE);

            binding.editProfileName.setVisibility(View.GONE);
            binding.editProfileNim.setVisibility(View.GONE);
            binding.editProfileJurusan.setVisibility(View.GONE);
            binding.btnSaveProfile.setVisibility(View.GONE);
        }
    }

    private void saveProfileChanges() {
        String newName = binding.editProfileName.getText().toString().trim();
        String newNim = binding.editProfileNim.getText().toString().trim();
        String newJurusan = binding.editProfileJurusan.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("nama", newName);
        updates.put("nim", newNim);
        updates.put("jurusan", newJurusan);

        userRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    loadUserProfile(); // Muat ulang data yang sudah diperbarui
                    toggleEditMode(false); // Kembali ke mode tampilan
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show());
    }

    private void logout() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
