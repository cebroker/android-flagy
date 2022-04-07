plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.evercheck.flagly.testsample"
        minSdk =AndroidConstants.MIN_SDK_VERSION
        targetSdk = AndroidConstants.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = AndroidConstants.TEST_RUNNER
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        getByName(BuildTypesConstants.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_JDK)
    implementation(Dependencies.ANDROID_X_CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL)

    implementation(Dependencies.ESPRESSO_CONTRIB)
    implementation(Dependencies.ESPRESSO_INTENTS)
    implementation(Dependencies.ESPRESSO_CORE)
    implementation(Dependencies.TEST_RUNNER)
    implementation(Dependencies.TEST_JUNIT_KTX)

    androidTestImplementation(Dependencies.ESPRESSO_CONTRIB)
    androidTestImplementation(Dependencies.ESPRESSO_INTENTS)
    androidTestImplementation(Dependencies.ESPRESSO_CORE)
    androidTestImplementation(Dependencies.TEST_RUNNER)

    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.CORE_TESTING)
    testImplementation(Dependencies.MOCKK)
    testImplementation(Dependencies.KOTLINX_COROUTINES_TEST)

    implementation(project(mapOf("path" to ":app")))
}