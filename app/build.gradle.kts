import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.jfrog.bintray")
    id("maven-publish")
}

apply {
    from("../install.gradle.kts")
}

android {
    compileSdk = AndroidConstants.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = AndroidConstants.MIN_SDK_VERSION
        targetSdk = AndroidConstants.TARGET_SDK_VERSION
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        isEnabled = true
    }
}

val properties: Properties = Properties()
properties.load(FileInputStream("local.properties"))

bintray {
    user = properties.getProperty(BintrayConstanst.BINTRAY_USER_KEY)
    key = properties.getProperty(BintrayConstanst.BINTRAY_PASSWORD_KEY)
    publish = true

    setPublications(LibraryConstants.PUBLICATION_NAME)

    pkg.apply {
        repo = BintrayConstanst.REPO_NAME
        name = LibraryConstants.ARTIFACT_GROUP
        userOrg = BintrayConstanst.USER_ORG
        githubRepo = BintrayConstanst.GITHUB_URL
        vcsUrl = BintrayConstanst.GITHUB_URL
        description = LibraryConstants.POM_DESCRIPTION
        setLabels("kotlin")
        setLicenses(BintrayConstanst.LICENSE)
        desc = LibraryConstants.POM_DESCRIPTION
        websiteUrl = BintrayConstanst.GITHUB_URL
        issueTrackerUrl = BintrayConstanst.GITHUB_URL
        githubReleaseNotesFile = BintrayConstanst.GITHUB_URL

        version.apply {
            name = LibraryConstants.VERSION
            desc = LibraryConstants.POM_DESCRIPTION
            vcsTag = "v${LibraryConstants.VERSION}"
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_JDK)
    implementation(Dependencies.ANDROID_X_CORE)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.RECYCLER_VIEW)
    implementation(Dependencies.COROUTINES)
    implementation(Dependencies.COROUTINES_ANDROID)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL)

    implementation(Dependencies.DAGGER)
    implementation(Dependencies.DAGGER_ANDROID_SUPPORT)
    kapt(Dependencies.DAGGER_COMPILER)
    kapt(Dependencies.DAGGER_ANDROID_PROCESSOR)

    implementation(Dependencies.CONSTRAINT_LAYOUT)

    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.CORE_TESTING)
    testImplementation(Dependencies.KOTLINX_COROUTINES_TEST)
    testImplementation(Dependencies.MOCKK)
}
