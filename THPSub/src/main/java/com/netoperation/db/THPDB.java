package com.netoperation.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.netoperation.default_db.DaoConfiguration;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoSectionList;
import com.netoperation.default_db.DaoSubSectionArticle;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableSectionList;
import com.netoperation.default_db.TableSubSectionArticle;

@Database(entities = {TableSubscriptionArticle.class, TableBookmark.class,
        TableBreifing.class, TableUserProfile.class, TableMP.class,
        TableHomeArticle.class, TableSectionArticle.class,
        TableSectionList.class, TableSubSectionArticle.class, TableConfiguration.class},
        version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class THPDB extends RoomDatabase {

    private static volatile THPDB INSTANCE;

    public abstract DaoSubscriptionArticle dashboardDao();
    public abstract DaoBookmark bookmarkTableDao();
    public abstract DaoBreifing breifingDao();
    public abstract DaoUserProfile userProfileDao();
    public abstract DaoMP mpTableDao();

    public abstract DaoHomeArticle daoHomeArticle();
    public abstract DaoSectionArticle daoSectionArticle();
    public abstract DaoSectionList daoSectionList();
    public abstract DaoSubSectionArticle daoSubSectionArticle();
    public abstract DaoConfiguration daoConfiguration();

    public static THPDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (THPDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            THPDB.class, "THPDB.db")
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN morningTime TEXT");
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN noonTime TEXT");
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN eveningTime TEXT");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            //Create the TableMP Table
            database.execSQL("CREATE TABLE IF NOT EXISTS `MPTable` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cycleUniqueId` TEXT, `cycleName` TEXT, `networkCurrentTimeInMilli` INTEGER NOT NULL, `allowedTimeInSecs` INTEGER NOT NULL, `expiryTimeInMillis` INTEGER NOT NULL, `startTimeInMillis` INTEGER NOT NULL, `allowedArticleCounts` INTEGER NOT NULL, `isMpFeatureEnabled` INTEGER NOT NULL, `isTaboolaNeeded` INTEGER NOT NULL, `isMpBannerNeeded` INTEGER NOT NULL, `mpBannerMsg` TEXT, `showFullAccessBtn` INTEGER NOT NULL, `fullAccessBtnName` TEXT, `showSignInBtn` INTEGER NOT NULL, `signInBtnName` TEXT, `signInBtnNameBoldWord` TEXT, `showSignUpBtn` INTEGER NOT NULL, `signUpBtnName` TEXT, `signUpBtnNameBoldWord` TEXT, `nonSignInBlockerTitle` TEXT, `nonSignInBlockerDescription` TEXT, `expiredUserBlockerTitle` TEXT, `expiredUserBlockerDescription` TEXT, `readArticleIds` TEXT)");
        }
    };
}
