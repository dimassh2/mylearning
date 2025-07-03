package com.firebyte.elearning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebyte.elearning.databinding.FragmentMaterialBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MaterialFragment extends Fragment {

    private FragmentMaterialBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference materiRef = db.collection("materi");
    private MateriAdapter adapter;

    public MaterialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

// Pengecekan admin yang baru (asynchronous)
        UserManager.checkAdminStatus(isAdmin -> {
            if (isAdmin) {
                binding.fabAddMateri.setVisibility(View.VISIBLE);
                binding.fabAddMateri.setOnClickListener(v -> showAddMateriDialog());
            } else {
                binding.fabAddMateri.setVisibility(View.GONE);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = materiRef.orderBy("createdAt", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Materi> options = new FirestoreRecyclerOptions.Builder<Materi>()
                .setQuery(query, Materi.class)
                .build();

        adapter = new MateriAdapter(options, getContext());

        binding.recyclerViewMateri.setHasFixedSize(true);
        binding.recyclerViewMateri.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMateri.setAdapter(adapter);

        adapter.setOnItemClickListener(documentSnapshot -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Hapus Materi")
                    .setMessage("Apakah Anda yakin ingin menghapus materi ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        documentSnapshot.getReference().delete();
                        Toast.makeText(getContext(), "Materi dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    private void showAddMateriDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_materi, null);
        builder.setView(dialogView);

        final EditText etJudul = dialogView.findViewById(R.id.et_judul_materi);
        final EditText etDeskripsi = dialogView.findViewById(R.id.et_deskripsi_materi);
        final EditText etLink = dialogView.findViewById(R.id.et_link_materi);
        final Button btnSimpan = dialogView.findViewById(R.id.btn_simpan_materi);

        final AlertDialog dialog = builder.create();

        btnSimpan.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();
            String link = etLink.getText().toString().trim();

            if (judul.isEmpty() || link.isEmpty()) {
                Toast.makeText(getContext(), "Judul dan Link wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            materiRef.add(new Materi(judul, deskripsi, link));
            Toast.makeText(getContext(), "Materi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
