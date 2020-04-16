package com.netoperation.default_db;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TablePersonaliseDefault")
public class TablePersonaliseDefault {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String personaliseSecId;
    private String parentSecId;
    private String displayName;
    private String category;
    private String customScreenPri;
    private boolean isSubSection;
    private boolean isUserPreffered;

    public TablePersonaliseDefault(String category, String customScreenPri, String personaliseSecId, String parentSecId, String displayName, boolean isSubSection, boolean isUserPreffered) {
        this.category = category;
        this.customScreenPri = customScreenPri;
        this.personaliseSecId = personaliseSecId;
        this.parentSecId = parentSecId;
        this.displayName = displayName;
        this.isSubSection = isSubSection;
        this.isUserPreffered = isUserPreffered;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomScreenPri() {
        return customScreenPri;
    }

    public void setCustomScreenPri(String customScreenPri) {
        this.customScreenPri = customScreenPri;
    }

    public String getPersonaliseSecId() {
        return personaliseSecId;
    }

    public void setPersonaliseSecId(String personaliseSecId) {
        this.personaliseSecId = personaliseSecId;
    }

    public String getParentSecId() {
        return parentSecId;
    }

    public void setParentSecId(String parentSecId) {
        this.parentSecId = parentSecId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isSubSection() {
        return isSubSection;
    }

    public void setSubSection(boolean subSection) {
        isSubSection = subSection;
    }

    public boolean isUserPreffered() {
        return isUserPreffered;
    }

    public void setUserPreffered(boolean userPreffered) {
        isUserPreffered = userPreffered;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof TablePersonaliseDefault) {
            TablePersonaliseDefault personaliseDefault = (TablePersonaliseDefault) obj;
            return personaliseSecId.equals(personaliseDefault.getPersonaliseSecId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return personaliseSecId.hashCode();
    }
}
