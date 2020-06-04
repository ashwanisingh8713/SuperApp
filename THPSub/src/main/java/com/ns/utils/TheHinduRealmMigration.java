package com.ns.utils;

import java.util.Set;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class TheHinduRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();


        if (oldVersion == 0) {
            RealmObjectSchema articleSchema = schema.get("ArticleBean");

            // add new field here
            articleSchema.addField("im_thumbnail", String.class);
            articleSchema.addField("short_de", String.class);

            oldVersion++;
        }
        if (oldVersion == 1) {
            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("articleType", String.class, FieldAttribute.REQUIRED).transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    obj.set("articleType", "article");
                }
            });
            articleSchema.addField("opid", int.class);
            RealmObjectSchema notificationSchema = schema.get("NotificationBean");
            notificationSchema.addField("isRead", boolean.class, FieldAttribute.REQUIRED).transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    obj.set("isRead", true);
                }
            });
            notificationSchema.addField("articleType", String.class, FieldAttribute.REQUIRED).transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    obj.set("articleType", "article");
                }
            });
            oldVersion++;
        }

        if (oldVersion == 2) {
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");

            // add new field here
            bookmarkSchema.addField("vid", String.class);
            oldVersion++;
        }
        /*From Version 3.3 onwards  */
        if (oldVersion == 3) {
            RealmObjectSchema sectionTable = schema.get("SectionTable");

            // add new field here
            sectionTable.addField("link", String.class);
            sectionTable.addField("customScreen", int.class);
            sectionTable.addField("customScreenPri", int.class);
            oldVersion++;

        }

        boolean isVersionFourExecuted = false;

        if (oldVersion == 4) {
            RealmObjectSchema DataBeanSchema = schema.create("DataBean");
            RealmObjectSchema DigestBeanSchema = schema.create("NewsDigestBean");
            DigestBeanSchema.addRealmObjectField("data", DataBeanSchema);
            RealmObjectSchema bSchema = schema.get("ArticleBean");
            bSchema.addField("isNewsDigest", boolean.class);
            DataBeanSchema.addField("da", String.class);
            DataBeanSchema.addField("yd", String.class);
            DataBeanSchema.addField("s", int.class);
            DataBeanSchema.addRealmObjectField("article", bSchema);

            oldVersion++;

            isVersionFourExecuted = true;
        }

        if (oldVersion == 5) {
            if (isVersionFourExecuted == true) {
                oldVersion++;
            } else if (schema != null && schema.contains("DataBean")) {
                oldVersion++;
            } else {
                RealmObjectSchema DataBeanSchema = schema.create("DataBean");
                if (schema != null && schema.contains("NewsDigestBean")) {
                    oldVersion++;
                } else {
                    RealmObjectSchema DigestBeanSchema = schema.create("NewsDigestBean");
                    DigestBeanSchema.addRealmObjectField("data", DataBeanSchema);
                    RealmObjectSchema bSchema = schema.get("ArticleBean");
                    Set<String> st = bSchema.getFieldNames();
                    if (st.contains("isNewsDigest")) {
                        oldVersion++;
                    } else {
                        bSchema.addField("isNewsDigest", boolean.class);
                        DataBeanSchema.addField("da", String.class);
                        DataBeanSchema.addField("yd", String.class);
                        DataBeanSchema.addField("s", int.class);
                        DataBeanSchema.addRealmObjectField("article", bSchema);

                        oldVersion++;
                    }
                }
            }
        }

        if (oldVersion == 6) {
            RealmObjectSchema sectionTable = schema.get("SectionTable");

            sectionTable.addField("custom", boolean.class);
            sectionTable.addField("webLink", String.class);
            oldVersion++;
        }

        if(oldVersion ==7) {
            RealmObjectSchema sectionTable = schema.get("SectionTable");

            Set<String> st = sectionTable.getFieldNames();
            if(!st.contains("custom")) {
                sectionTable.addField("custom", boolean.class);
            }
            if(!st.contains("webLink")) {
                sectionTable.addField("webLink", String.class);
            }
            oldVersion++;
        }

        if(oldVersion == 8) {
            RealmObjectSchema articleSchema = schema.get("ArticleBean");

            // add new field here
            articleSchema.addField("page", int.class);

            oldVersion++;
        }

        if (oldVersion == 9) {
            RealmObjectSchema articleSchema = schema.get("ArticleBean");

            // add new filed in isRead page
            articleSchema.addField("isRead", boolean.class);

            oldVersion++;
        }

        if (oldVersion == 10) {
            RealmObjectSchema imgBeanSchema = schema.get("ImageBean");
            imgBeanSchema.addField("im_v2", String.class);

            // add new filed in ArticleBean
            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("location", String.class);
            articleSchema.addField("im_thumbnail_v2", String.class);

            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("location", String.class);
            bookmarkSchema.addField("im_thumbnail_v2", String.class);

            // SectionTable's New Fields
            RealmObjectSchema sectionSchema = schema.get("SectionTable");
            sectionSchema.addField("isStaticPageEnable", boolean.class);
            sectionSchema.addField("staticPageUrl", String.class);
            sectionSchema.addField("positionInListView", int.class);

            oldVersion++;
        }

        if (oldVersion == 11) {
            // add new filed in BookmarkBean
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("comm_count", String.class);

            oldVersion++;
        }

        if (oldVersion == 12) {
            // add new filed in ArticleBean
            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("isArticleRestricted", String.class);
            // add new filed in BookmarkBean
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("isArticleRestricted", String.class);

           oldVersion++;
        }
        if (oldVersion == 13) {
            // add new filed in ArticleBean
            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("p4_pos", int.class);
            // add new filed in BookmarkBean
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("p4_pos", int.class);

            oldVersion++;
        }
        if (oldVersion == 14) {
            // add new filed in BookmarkBean
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("parentId", String.class);
            //Add company data table
            schema.create("CompanyData")
                    .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class)
                    .addField("bse", int.class)
                    .addField("nse", int.class)
                    .addField("gp", String.class);

            oldVersion++;
        }
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TheHinduRealmMigration);
    }

    @Override
    public int hashCode() {
        return 37;
    }
}
