# Add project specific ProGuard rules here.

# ---- Kotlin ----
-keepclassmembers class **$WhenMappings { <fields>; }
-keep class kotlin.Metadata { *; }

# ---- Hilt / Dagger ----
-keepclasseswithmembernames class * { @dagger.* <fields>; }
-keepclasseswithmembernames class * { @javax.inject.* <fields>; }
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.**

# ---- DataStore ----
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* { <fields>; }

# ---- Compose ----
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ---- Coroutines ----
-keepclassmembernames class kotlinx.** { volatile <fields>; }
