import com.google.devtools.ksp.gradle.model.Ksp

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.chatterbox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatterbox"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.transportation.consumer)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.51.1")
    kspAndroidTest("com.google.dagger:hilt-compiler:2.51.1")

    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.51.1")
    kspTest("com.google.dagger:hilt-compiler:2.51.1")

    // system ui controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.35.0-alpha")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    // Paging 3 Library
    implementation("androidx.paging:paging-runtime-ktx:3.3.0-beta01")
    // alternatively - without Android dependencies for tests
    testImplementation("androidx.paging:paging-common-ktx:3.3.0-beta01")
    // optional - RxJava2 support
    implementation("androidx.paging:paging-rxjava2-ktx:3.3.0-beta01")
    // optional - RxJava3 support
    implementation("androidx.paging:paging-rxjava3:3.3.0-beta01")
    // optional - Guava ListenableFuture support
    implementation("androidx.paging:paging-guava:3.3.0-beta01")
    // optional - Jetpack Compose integration
    implementation("androidx.paging:paging-compose:3.3.0-beta01")

    // coil library for image
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Compose Navigation
    implementation ("androidx.navigation:navigation-compose:2.8.0-alpha07")

    //Compose Foundation
    implementation ("androidx.compose.foundation:foundation:1.7.0-alpha07")

    //swipe refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.34.0")

    // new Splash Screen API
    implementation ("androidx.core:core-splashscreen:1.1.0-alpha02")

    //material icon extended library
    implementation ("androidx.compose.material:material-icons-extended:1.7.0-alpha07")
}