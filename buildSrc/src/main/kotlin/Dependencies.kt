object Dependencies {

    const val picasso = "com.squareup.picasso:picasso:2.8"

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:3.8.0"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.9.0"

    object Kotlin {
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
    }

    object Android {
        const val androidXCoreLibrary = "androidx.core:core-ktx:1.3.1"
        const val androidXAppCompat = "androidx.appcompat:appcompat:1.2.0"
        const val androidXConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.1"
        const val androidXRecyclerview = "androidx.recyclerview:recyclerview:1.2.0"
    }

    object Test {
        const val junit = "junit:junit:4.12"
        const val androidXjUnit = "androidx.test.ext:junit:1.1.2"
        const val androidXEspresso = "androidx.test.espresso:espresso-core:3.3.0"
        const val mockK = "io.mockk:mockk:${Versions.Test.mockK}"
        const val androidMockK = "io.mockk:mockk-android:${Versions.Test.mockK}"
    }
}