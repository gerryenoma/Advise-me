plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.adviseme"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.adviseme"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"  // Use latest stable Kotlin Compose compiler
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {


    // Compose BOM to manage consistent versions
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))

    // Material 3 UI Components
    implementation("androidx.compose.material3:material3")



    // Material Icons Extended (optional)
    implementation("androidx.compose.material:material-icons-extended")

    // Core Compose UI
    implementation( "androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.foundation:foundation")

    // Compose Tooling support for previews
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")

    // Activity Compose for Compose lifecycle integration
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Lifecycle runtime for ViewModel etc.
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Retrofit for network calls (optional)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation ("com.squareup.okhttp3:okhttp:4.10.0")


    implementation ("androidx.compose.ui:ui-text")




    // For network requests
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // For JSON parsing
    implementation("org.json:json:20240303")

    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2") // or latest stable


    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore") // optional if storing roles

    implementation ("com.google.android.gms:play-services-auth:21.1.0")

    // Import the Firebase BoM so all Firebase libs stay in sync
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))

    // Add the Firebase libraries you actually need (no versions here)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    // e.g. implementation("com.google.firebase:firebase-firestore-ktx")

    implementation ("com.google.firebase:firebase-firestore-ktx")


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")  // Apr 2025 :contentReference[oaicite:0]{index=0}







}

apply(plugin = "com.google.gms.google-services") // ✅ ADD THIS at the end
