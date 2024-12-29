plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maps.secrets)
}

android {
    namespace = "bd.com.albin.weather"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "bd.com.albin.weather"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.named("debug").get()
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
        compose = true
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "secrets.properties"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.accompanist.permissions)
    implementation(libs.google.location.service)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.androidx.core.splashscreen)
//    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.androidx.material.android)
    implementation(libs.androidx.compose.material.iconsExtended)
//    implementation(libs.androidx.compose.material3.windowSizeClass)
//
//    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)


//    implementation(libs.room.ktx)
//    implementation(libs.room.runtime)
//    implementation(libs.room.paging)
//    ksp(libs.room.compiler)

    implementation(libs.kotlinx.datetime)

//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.android)
//    implementation(libs.ktor.client.logging)
//    implementation(libs.ktor.client.content.negotiation)
//    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.serialization.jvm)

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.serialization.json)

//    implementation(libs.androidx.paging.runtime.ktx)
//    implementation(libs.androidx.paging.compose)

}