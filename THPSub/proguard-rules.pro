# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/arvind/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep public class com.mobstac.** {
  *;
}


-keepattributes *Annotation*,JavascriptInterface,Exceptions,InnerClasses,Signature,SourceFile,LineNumberTable,InnerClasses

# VMAX Ads settings
-keep public class com.vmax.android.ads.** {
    public <fields>;
    public <methods>;
}
-keep public class com.vmax.android.ads.nativeHelper.** {
 *;
}
-keep class com.vmax.android.ads.mediation.partners.** {
    public <fields>;
    public <methods>;
 }
-dontwarn android.support.v4.app**
-keep public class android.support.v4.content.ContextCompat { *;}
-keep public class android.support.v4.app.ActivityCompat { *;}
-keep class android.support.v7.widget.SearchView { *; }
#Make sure if you apply Shrink mode than keep below Vmax resources.
#For more details refer below link:
#http://developer.android.com/tools/help/proguard.html

-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn com.google.ads.**
-dontwarn com.vmax.android.ads.volley.toolbox.**
-dontwarn com.chartboost.sdk.impl.**
-dontwarn com.google.android.gms.ads.**
-dontwarn com.vmax.android.ads.**

# Google Play Services library
-keep public class com.google.android.gms.ads.** {public *;}
-keep public class com.google.android.gms.common.ConnectionResult { public *;}
-keep public class com.google.android.gms.common.GooglePlayServicesUtil { public *;}
-dontwarn com.google.android.gms**
-keep class * extends java.util.ListResourceBundle { protected Object[][] getContents(); }
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }
#Facebook:
-keep public class com.facebook.ads.** { public *;}
#Adcolony:
-keep class com.jirbo.** { public *; }

-dontwarn android.webkit.**

#Chartboost:
-keep class com.chartboost.** { *; }


#Tapjoy:
-keep class com.tapjoy.** { *; }


#Vungle Ad Sdk settings

-keep class com.vungle.** { public *; }
-keep class javax.inject.*
-keepattributes *Annotation*
-keepattributes Signature
-keep class dagger.*


#Millennial Media Ad SDK Settings

-keepclassmembers class com.millennialmedia.android.* {
public *;
}
-keep class com.millennialmedia.android.**



-dontwarn com.millennialmedia.android.NVASpeechKit*



#Flurry Ad SDK Settings

-keepattributes *Annotation*,EnclosingMethod
-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
#Bee7 SDK Settings
-keep class com.bee7.sdk.**{ *; }
-keep class com.bee7.gamewall.**{ *; }
-dontwarn com.bee7.**

#inmobi

-keep class com.inmobi.** { *; }

-dontwarn com.inmobi.**



#Additional Settings:

-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn com.google.ads.**
-dontwarn com.chartboost.sdk.impl.**
-dontwarn okio.**
-dontwarn com.vuforia.**
-dontwarn com.comscore.**
-dontwarn com.app.pokktsdk.**
-dontwarn com.amazonaws.util.json.**
-dontwarn com.moat.analytics.**
-dontwarn com.squareup.okhttp.**

#Event Bus

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#Retrofit

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


#realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**
-keep class io.realm.internal.OsObject { *; }


-keepnames public class * extends io.realm.RealmObject
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**



-keep class com.google.android.gms.gcm.** { *; }
-keep class com.google.android.gms.iid.** { *; }
-keep class com.google.android.gms.location.** { *; }


-keep class com.delight.**  { *; }


#crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#comscore
-keep class com.comscore.** { *; }
-dontwarn com.comscore.**



#Jsoup
-keeppackagenames org.jsoup.nodes