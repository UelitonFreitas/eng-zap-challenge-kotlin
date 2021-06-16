plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.Android.SDKCompile)
    buildToolsVersion(Versions.buildToolsVersion)

    defaultConfig {
        applicationId = "com.zap.zaprealestate"
        minSdkVersion(Versions.Android.minSdkVersion)
        targetSdkVersion(Versions.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("developer") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Dependencies.Kotlin.kotlinStdLib)
    implementation(Dependencies.Android.androidXCoreLibrary)
    implementation(Dependencies.Android.androidXAppCompat)
    implementation(Dependencies.Android.androidXConstraintLayout)
    implementation(Dependencies.Android.androidXRecyclerview)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gson)
    implementation(Dependencies.gsonConverter)
    implementation(Dependencies.loggingInterceptor)
    implementation(Dependencies.picasso)
    implementation(Dependencies.Android.swipeToRefresh)
    implementation("com.android.support:design:24.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.viewpager:viewpager:1.0.0")

    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.mockK)

    androidTestImplementation(Dependencies.Test.androidXjUnit)
    androidTestImplementation(Dependencies.Test.androidXEspresso)
    androidTestImplementation(Dependencies.Test.androidMockK)
}

tasks.withType(Test::class).all {
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    maxParallelForks = Runtime.getRuntime().availableProcessors() / 2
}

