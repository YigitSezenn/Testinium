plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.tapandtest.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tapandtest.app"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.koin.android)

    // Remember-Settings (KMP ile kullanılıyorsa)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.remember.settings)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.github.skydoves:landscapist-glide:2.4.7")
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    val nav_version = "2.8.9"
    implementation(libs.androidx.core.splashscreen)
    //noinspection UseTomlInstead
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    //noinspection UseTomlInstead
    implementation("com.github.skydoves:landscapist-glide:2.4.7")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    //noinspection UseTomlInstead
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    // navigation
    implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.animation:animation:1.7.8")
    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:2.9.0-alpha09")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.9.0-alpha09")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}