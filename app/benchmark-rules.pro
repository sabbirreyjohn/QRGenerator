# Benchmark-specific ProGuard rules
# Add project specific ProGuard rules here.

# Keep benchmark classes
-keep class com.example.qrcodebuilder.** { *; }

# Keep QR code library classes
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }

# Keep Compose runtime
-keep class androidx.compose.runtime.** { *; }