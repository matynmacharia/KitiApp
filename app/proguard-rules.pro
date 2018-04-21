# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-renamesourcefileattribute SourceFile
-keepattributes InnerClasses
       -keep class app.kiti.com.kitiapp.models.RedeemRequestModel**
       -keepclassmembers class app.kiti.com.kitiapp.models.RedeemRequestModel** {
          *;
}

-keepattributes InnerClasses
 -keep class app.kiti.com.kitiapp.models.TransactionModel**
 -keepclassmembers class app.kiti.com.kitiapp.models.TransactionModel** {
    *;
 }

-keepattributes InnerClasses
 -keep class app.kiti.com.kitiapp.models.CompletedRequestModel**
 -keepclassmembers class app.kiti.com.kitiapp.models.CompletedRequestModel** {
    *;
 }

 -keepattributes InnerClasses
  -keep class app.kiti.com.kitiapp.models.EarningHolder**
  -keepclassmembers class app.kiti.com.kitiapp.models.EarningHolder** {
     *;
  }

  -keepattributes InnerClasses
   -keep class app.kiti.com.kitiapp.models.EarningModel**
   -keepclassmembers class app.kiti.com.kitiapp.models.EarningModel** {
      *;
   }

-keepattributes InnerClasses
-keep class app.kiti.com.kitiapp.utils.FirebaseDataField**
-keepclassmembers class app.kiti.com.kitiapp.utils.FirebaseDataField** {
     *;
}