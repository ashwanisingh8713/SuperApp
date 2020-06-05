package com.ns.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class BusinessLineRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();


        if (oldVersion == 0) {
            RealmObjectSchema articleSchema = schema.get("SectionTable");

            // add new field here
            articleSchema.addField("from", String.class);

            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("parentId", String.class);

            oldVersion++;
        }
        if(oldVersion == 1) {
            RealmObjectSchema articleSchema = schema.get("ArticleBean");

            // add new field here
            articleSchema.addField("page", int.class);

            oldVersion++;
        }

        if(oldVersion == 2) {
            // SectionTable's New Fields
            RealmObjectSchema sectionSchema = schema.get("SectionTable");
            sectionSchema.addField("isStaticPageEnable", boolean.class);
            sectionSchema.addField("staticPageUrl", String.class);
            sectionSchema.addField("positionInListView", int.class);
            oldVersion++;
        }

        if (oldVersion == 3) {
            // Add Thumb image v2
            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("im_thumbnail_v2", String.class);

            RealmObjectSchema imageBeanSchema = schema.get("ImageBean");
            imageBeanSchema.addField("im_v2", String.class);

            RealmObjectSchema selectionSchema = schema.get("SectionTable");
            selectionSchema.addField("image_v2", String.class);
            selectionSchema.addField("staticPageSid", int.class);

            oldVersion++;
        }

        if (oldVersion == 4) {

            RealmObjectSchema articleSchema = schema.get("ArticleBean");
            articleSchema.addField("location", String.class);

            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("location", String.class);

            oldVersion++;
        }

        if (oldVersion == 5) {
            // add new filed in BookmarkBean
            RealmObjectSchema bookmarkSchema = schema.get("BookmarkBean");
            bookmarkSchema.addField("comm_count", String.class);
            bookmarkSchema.addField("isArticleRestricted", String.class);
            bookmarkSchema.addField("im_thumbnail_v2", String.class);
            bookmarkSchema.addField("p4_pos", int.class);

            oldVersion++;
        }

    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BusinessLineRealmMigration);
    }

    @Override
    public int hashCode() {
        return 37;
    }
}
