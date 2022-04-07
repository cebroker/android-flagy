private const val COROUTINES_VERSION = "1.6.0"
private const val DAGGER_VERSION = "2.37"
private const val KOTLIN_VERSION = "1.6.10"
private const val ANDROID_X_CORE_VERSION = "1.6.0"
private const val ESPRESSO_VERSION = "3.2.0"
private const val TEST_RUNNER_VERSION = "1.2.0"
private const val LIFECYCLE_VIEWMODEL_VERSION = "2.4.0"
private const val MOCKK_VERSION = "1.12.3"
private const val KOTLINX_COROUTINES_TEST_VERSION = "1.6.0"
private const val CORE_TESTING_VERSION = "2.1.0"
private const val TEST_JUNIT_VERSION = "1.1.3"

object Dependencies {

    const val KOTLIN_JDK = "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"

    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION"
    const val LIFECYCLE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VIEWMODEL_VERSION"

    const val DAGGER = "com.google.dagger:dagger:$DAGGER_VERSION"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:$DAGGER_VERSION"
    const val DAGGER_ANDROID_PROCESSOR =
        "com.google.dagger:dagger-android-processor:$DAGGER_VERSION"
    const val DAGGER_ANDROID_SUPPORT = "com.google.dagger:dagger-android-support:$DAGGER_VERSION"

    const val ANDROID_X_CORE = "androidx.core:core-ktx:$ANDROID_X_CORE_VERSION"
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.1.0"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:1.1.0"

    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:1.1.3"

    const val JUNIT = "junit:junit:4.13.2"

    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:$ESPRESSO_VERSION"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
    const val ESPRESSO_INTENTS = "androidx.test.espresso:espresso-intents:$ESPRESSO_VERSION"

    const val TEST_RUNNER = "androidx.test:runner:$TEST_RUNNER_VERSION"

    const val TEST_JUNIT_KTX = "androidx.test.ext:junit-ktx:$TEST_JUNIT_VERSION"

    const val MOCKK = "io.mockk:mockk:$MOCKK_VERSION"
    const val KOTLINX_COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:$KOTLINX_COROUTINES_TEST_VERSION"
    const val CORE_TESTING = "androidx.arch.core:core-testing:$CORE_TESTING_VERSION"
}