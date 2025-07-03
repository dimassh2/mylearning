package com.firebyte.elearning;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Materi {
    private String judul;
    private String deskripsi;
    private String link;

    @ServerTimestamp
    private Date createdAt;

    // Constructor kosong wajib ada untuk Firestore
    public Materi() {}

    public Materi(String judul, String deskripsi, String link) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.link = link;
    }

    // Getters
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getLink() { return link; }
    public Date getCreatedAt() { return createdAt; }
}
