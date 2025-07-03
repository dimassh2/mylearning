// File: app/build.gradle.kts (Module :app)

plugins {
    // BENAR: Terapkan plugin di sini TANPA 'version' dan 'apply false'
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.firebyte.elearning"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.firebyte.elearning"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Firebase Bill of Materials (BOM)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Dependensi untuk Firebase Authentication dan Google Sign-In
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Dependensi untuk Firestore Database
    implementation("com.google.firebase:firebase-firestore")
// Library untuk memuat gambar dari internet dengan mudah
    implementation("com.github.bumptech.glide:glide:4.16.0")
// Tambahkan ini untuk FirestoreRecyclerAdapter
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}