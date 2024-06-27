plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.kotlin.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.alterok.mausamlive"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alterok.mausamlive"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "2024.1.0.0-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.navigationKtx)
    implementation(libs.navigationUIKtx)
    implementation(libs.navigationDynamicFeatures)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.livedataKtx)
    implementation(libs.viewmodelKtx)
    implementation(libs.activityKtx)

    implementation(libs.room)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)
    implementation(libs.roomPaging)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofitLogging)
    implementation(libs.converter.gson)
    implementation(libs.logUtils)
    implementation(libs.dataResult)
    implementation(libs.kotlinEssentialExt)
    implementation(libs.lottie)
    implementation(libs.mapbox)
    implementation(libs.location)
}