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


