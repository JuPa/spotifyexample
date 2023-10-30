plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.spotifyexample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.spotifyexample"
        minSdk = 32
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["redirectHostName"] = "auth"
        manifestPlaceholders["redirectSchemeName"] = "spotify-sdk"

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
}

dependencies {
    //implementation files('../lib/spotify-auth-release-2.0.1.aar')
    //implementation(files("../lib/spotify-auth-release-2.1.0.aar"))
    implementation("com.spotify.android:auth:2.1.0")// Maven dependency (Prefer
    //implementation 'com.spotify.android:auth:1.2.5' // Maven dependenc
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //implementation("com.spotify.android:auth:2.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}