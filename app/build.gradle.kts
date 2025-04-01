import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.example.tempora"
    compileSdk = 35

    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(file))

    defaultConfig {
        applicationId = "com.example.tempora"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "appidSafe",properties.getProperty("appid"))
        buildConfigField("String", "appKeySafe",properties.getProperty("appKey"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Location
    implementation("com.google.android.gms:play-services-location:21.1.0")
    //Google Maps in Jetpack Compose
    implementation("com.google.maps.android:maps-compose:6.4.1")
    //Google Places API and Jetpack Compose support for Places
    implementation("com.google.android.libraries.places:places:3.1.0")
    implementation("com.google.maps.android:places-compose:0.1.2")
    //Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    //ConstrainLayout in Jetpack Compose
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    //Animated Navigation Bar
    implementation("com.exyte:animated-navigation-bar:1.0.0")
    //Navigation
    implementation("androidx.navigation:navigation-compose:2.8.8")
    //Serialization for NavArgs
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    //AndroidX Lifecycle and provides ViewModel support in Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")
    //Gson
    implementation ("com.google.code.gson:gson:2.8.9")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    //LiveData in Jetpack Compose
    val compose_version = "1.0.0"
    implementation ("androidx.compose.runtime:runtime-livedata:$compose_version")
    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    // Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    //Datastore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //Kotlin + workManager
    implementation("androidx.work:work-runtime-ktx:2.7.1")


    // Dependencies for local unit tests
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.hamcrest:hamcrest-all:1.3")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.robolectric:robolectric:4.5.1")

    // AndroidX Test - JVM testing
    testImplementation ("androidx.test:core-ktx:1.6.1")
    //testImplementation "androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion"

    // AndroidX Test - Instrumented testing
    androidTestImplementation ("androidx.test:core:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    //Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // hamcrest
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")


    // AndroidX and Robolectric
    testImplementation ("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation ("androidx.test:core-ktx:1.6.1")
    testImplementation ("org.robolectric:robolectric:4.11")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    //kotlinx-coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    implementation ("org.jetbrains.kotlin:kotlin-test:2.0.0")

    //Mockk
    testImplementation ("io.mockk:mockk-android:1.13.17")
    testImplementation ("io.mockk:mockk-agent:1.13.17")

    //Google Truth Dependency
    testImplementation("com.google.truth:truth:1.1.3")


}