plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.chatapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


    }

    packaging {
        resources {
            excludes += "/META-INF/smack-config.xml"
            excludes += "/META-INF/smack-config-*.xml"
            excludes += "/META-INF/versions/*"
        }
    }
    configurations {
        all {
            exclude(
                group = "xmlpull",
                module = "xmlpull"
            )
            exclude(
                group = "xpp3",
                module = "xpp3_min"
            )
            exclude(
                group = "xpp3",
                module = "xpp3"
            )
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")


    implementation("org.igniterealtime.smack:smack-android:4.4.4")

    implementation("org.igniterealtime.smack:smack-tcp:4.4.4")

    implementation("org.igniterealtime.smack:smack-im:4.4.4")

    implementation("org.igniterealtime.smack:smack-extensions:4.4.4")

    //timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //navigation
    implementation ("androidx.navigation:navigation-compose:2.7.5")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


}

