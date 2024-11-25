plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.selfcheckout"
    compileSdk = 34
    buildFeatures {
        buildConfig = true // Enable BuildConfig generation
    }

    defaultConfig {
        applicationId = "com.example.selfcheckout"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val consumerKey = providers.gradleProperty("CONSUMER_KEY").get()
        val consumerSecret = providers.gradleProperty("CONSUMER_SECRET").get()
        val passkey = providers.gradleProperty("PASSKEY").get()
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "CONSUMER_KEY", "\"$consumerKey\"")
            buildConfigField("String", "CONSUMER_SECRET", "\"$consumerSecret\"")
            buildConfigField("String", "PASSKEY", "\"$passkey\"")
        }
        debug {
            buildConfigField("String", "CONSUMER_KEY", "\"$consumerKey\"")
            buildConfigField("String", "CONSUMER_SECRET", "\"$consumerSecret\"")
            buildConfigField("String", "PASSKEY", "\"$passkey\"")
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
        viewBinding = true
    }


}

dependencies {
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}