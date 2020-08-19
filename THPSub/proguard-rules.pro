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

-keep interface * {
   <methods>;
}
-keepattributes *Annotation*, Signature, Exception
-keepattributes LocalVariableTable, LocalVariableTypeTable
-keepattributes SourceFile, LineNumberTable, EnclosingMethod, Deprecated, InnerClasses

#-repackageclasses

-keep public class com.main.AdsBase
-keep public class com.main.DFPAds
-keep public class com.main.DFPConsent
-keep public class com.main.PeriodicWork
-keep public class com.main.SectionListingAds
-keep public class com.main.SuperApp

-keep public class com.netoperation.db.Converters
-keep public class com.netoperation.db.DaoBookmark
-keep public class com.netoperation.db.DaoBreifing
-keep public class com.netoperation.db.DaoMP
-keep public class com.netoperation.db.DaoSubscriptionArticle
-keep public class com.netoperation.db.DaoTemperoryArticle
-keep public class com.netoperation.db.TableBookmark
-keep public class com.netoperation.db.TableBreifing
-keep public class com.netoperation.db.TableMP
-keep public class com.netoperation.db.TableSubscriptionArticle
-keep public class com.netoperation.db.TableUserProfile
-keep public class com.netoperation.db.THPDB

-keep public class com.netoperation.default_db.DaoBanner
-keep public class com.netoperation.default_db.DaoConfiguration
-keep public class com.netoperation.default_db.DaoHomeArticle
-keep public class com.netoperation.default_db.DaoMPReadArticle
-keep public class com.netoperation.default_db.DaoPersonaliseDefault
-keep public class com.netoperation.default_db.DaoRead
-keep public class com.netoperation.default_db.DaoRelatedArticle
-keep public class com.netoperation.default_db.DaoSection
-keep public class com.netoperation.default_db.DaoSectionArticle
-keep public class com.netoperation.default_db.DaoTableOptional
-keep public class com.netoperation.default_db.DaoTempWork
-keep public class com.netoperation.default_db.DaoWidget

-keep public class com.netoperation.default_db.TableBanner
-keep public class com.netoperation.default_db.TableConfiguration
-keep public class com.netoperation.default_db.TableHomeArticle
-keep public class com.netoperation.default_db.TableMPReadArticle
-keep public class com.netoperation.default_db.TablePersonaliseDefault
-keep public class com.netoperation.default_db.TableRead
-keep public class com.netoperation.default_db.TableRelatedArticle
-keep public class com.netoperation.default_db.TableSection
-keep public class com.netoperation.default_db.TableSectionArticle
-keep public class com.netoperation.default_db.TableOptional
-keep public class com.netoperation.default_db.TableTempWork
-keep public class com.netoperation.default_db.TableWidget
-keep public class com.netoperation.default_db.TableTemperoryArticle

-keep public class com.netoperation.net.ApiManager
-keep public class com.netoperation.net.DefaultTHApiManager
-keep public class com.netoperation.net.RequestCallback
-keep public class com.netoperation.retrofit.ServiceFactory

-keep public class androidx.core.app.NotificationCompat {
    *;
}

-keep class androidx.core.graphics.TypefaceCompatBaseImpl
-keep class com.google.android.material.floatingactionbutton.FloatingActionButtonImpl

-keep class androidx.core.graphics.TypefaceCompatBaseImpl {
    *;
}

-keepclassmembernames class * {
    public protected  native <methods>;
}

-keep public class androidx.loader.content.Loader

-keep public class okhttp3.OkHttpClient {
    *;
}
-dontwarn okio.


### 18 Aug 2020
### 18 Aug 2020
### 18 Aug 2020

-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Preserve some attributes that may be required for reflection.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick.
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class **.R$* {
        <fields>;
    }

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Preserve annotated Javascript interface methods.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**

# This class is deprecated, but remains for backward compatibility.
-dontwarn android.util.FloatMath

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# These classes are duplicated between android.jar and org.apache.http. .jar.
-dontnote org.apache.http.**
-dontnote android.net.http.**

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

-keep class com.netoperation.model.USPData { *; }
-keep class com.netoperation.model.USPData$USPDATABean { *; }
-keep class com.netoperation.model.USPData$USPDATABean$GuideOverlay { *; }
-keep class com.netoperation.model.ForceUpdateModel { *; }
-keep class com.netoperation.model.ForceUpdateModel$DATAModel { *; }
-keep class com.netoperation.net.DefaultTHApiManager { *; }
-keep class com.netoperation.net.ApiManager { *; }



# if you don't want all inner class and members in some package to be obfuscated you can add lines in proguard-rules.pro
-keep class com.netoperation.model.*$* {
        *;
    }
-keep class com.netoperation.model.* {
        *;
    }

-keepclassmembers class com.netoperation.model.*$* {
            *;
        }
-keepclassmembers class com.netoperation.model.* {
            *;
        }

-keepnames class com.netoperation.model.*$* {
            *;
        }
-keepnames class com.netoperation.model.* {
            *;
        }

-keepclassmembernames class com.netoperation.model.*$* {
            *;
        }
-keepclassmembernames class com.netoperation.model.* {
            *;
        }



