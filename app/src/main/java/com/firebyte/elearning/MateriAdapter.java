package com.firebyte.elearning;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MateriAdapter extends FirestoreRecyclerAdapter<Materi, MateriAdapter.MateriViewHolder> {

    private OnItemClickListener listener;
    private Context context;

    public MateriAdapter(@NonNull FirestoreRecyclerOptions<Materi> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MateriViewHolder holder, int position, @NonNull Materi model) {
        holder.tvJudul.setText(model.getJudul());
        holder.tvDeskripsi.setText(model.getDeskripsi());

        UserManager.checkAdminStatus(isAdmin -> {
            if (isAdmin) {
                holder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                holder.btnDelete.setVisibility(View.GONE);
            }
        });

        holder.tvBukaLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink()));
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new MateriViewHolder(view);
    }

    class MateriViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvDeskripsi, tvBukaLink;
        ImageButton btnDelete;

        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul_materi);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_materi);
            tvBukaLink = itemView.findViewById(R.id.tv_buka_link);
            btnDelete = itemView.findViewById(R.id.delete_button_materi);

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(getSnapshots().getSnapshot(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
