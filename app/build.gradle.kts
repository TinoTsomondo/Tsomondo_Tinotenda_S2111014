plugins {
    id("com.android.application")
}

android {
    compileSdkVersion(34)
    defaultConfig {
        applicationId = "com.example.skycast"
        minSdkVersion(24)
        targetSdkVersion(34)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        namespace = "com.example.skycast"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Define versions here
    val appcompatVersion = "1.3.1"
    val materialVersion = "1.5.0" // Updated Material Design version
    val activityVersion = "1.3.1"
    val constraintLayoutVersion = "2.1.0"
    val junitVersion = "4.13.2"
    val espressoVersion = "3.4.0"
    val playServicesVersion = "18.0.1" // Replace with the latest version

    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:$materialVersion") // Material Design 1.5.0
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("com.google.android.gms:play-services-maps:$playServicesVersion") // Google Maps

    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    // Add Android Support Annotations library as compileOnly
    compileOnly("com.android.support:support-annotations:28.0.0")
}
