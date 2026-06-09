# ──────────────────────────────────────────────
# App code — keep everything in our package
# R8 may still optimize third-party libraries
# ──────────────────────────────────────────────
-keep class com.kotoba.takarabako.** { *; }
-keepclassmembers class com.kotoba.takarabako.** { *; }

# ──────────────────────────────────────────────
# Crash stack traces (keep file/line info)
# ──────────────────────────────────────────────
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepattributes InnerClasses,EnclosingMethod

# ──────────────────────────────────────────────
# Kotlin
# ──────────────────────────────────────────────
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# ──────────────────────────────────────────────
# Kotlin Coroutines
# ──────────────────────────────────────────────
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }

# ──────────────────────────────────────────────
# Gson — data models for JSON parsing
# ──────────────────────────────────────────────
-dontwarn sun.misc.**
-keep class com.kotoba.takarabako.data.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ──────────────────────────────────────────────
# Room
# R8 full mode: AppDatabase_Impl is instantiated by Class.forName() at runtime.
# Entity fields and Dao methods are accessed via reflection.
# ──────────────────────────────────────────────
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
# Explicitly keep KAPT-generated _Impl classes (Class.forName target)
-keep class **.*_Impl { *; }
-keep class **.*_Impl$* { *; }
-dontwarn androidx.room.paging.**

# ──────────────────────────────────────────────
# ViewModel — constructors called via reflection by ViewModelProvider
# ──────────────────────────────────────────────
-keep class * extends androidx.lifecycle.ViewModel { <init>(...); }
-keep class * extends androidx.lifecycle.AndroidViewModel { <init>(...); }

# ──────────────────────────────────────────────
# DataStore Preferences
# ──────────────────────────────────────────────
-keep class androidx.datastore.** { *; }
-keep class androidx.datastore.preferences.** { *; }
-keep class androidx.datastore.core.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { <fields>; }

# ──────────────────────────────────────────────
# AdMob / Google Play Services
# (SDK bundles its own rules; these are belt-and-suspenders)
# ──────────────────────────────────────────────
-keep public class com.google.android.gms.ads.** { *; }
-keep public class com.google.ads.** { *; }
-dontwarn com.google.android.gms.**
