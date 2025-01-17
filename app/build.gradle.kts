val DependencyHandlerScope.implementation: Unit
    get() {

    }

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.pr7kot"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pr7kot"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.core)
    implementation(libs.mockito.core)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.exoplayer.robolectricutils)
    implementation(libs.junit)
    implementation(libs.robolectric)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.junit)
    implementation(libs.android.all)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}