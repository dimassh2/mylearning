package com.firebyte.elearning;

public class User {
    private String uid;
    private String nama;
    private String email;
    private String fotoUrl;
    private String nim;
    private String jurusan;

    // Constructor kosong wajib untuk Firestore
    public User() {}

    public User(String uid, String nama, String email, String fotoUrl) {
        this.uid = uid;
        this.nama = nama;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.nim = ""; // Default value
        this.jurusan = ""; // Default value
    }

    // Getters
    public String getUid() { return uid; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getFotoUrl() { return fotoUrl; }
    public String getNim() { return nim; }
    public String getJurusan() { return jurusan; }
}
