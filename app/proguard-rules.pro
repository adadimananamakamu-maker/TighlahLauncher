# ProGuard rules for TighlahLauncher

# Keep all TighlahLauncher classes
-keep class com.tighlah.launcher.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Kotlinx Serialization
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.SerializedName <methods>;
}

# Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Retrofit
-keep class retrofit2.** { *; }
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Android
-keep class android.** { *; }
-dontwarn android.**
