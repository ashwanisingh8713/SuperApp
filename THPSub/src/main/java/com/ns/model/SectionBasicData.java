package com.ns.model;


public class SectionBasicData {

    private String personaliseSecId;
    private String parentSecId;
    private String displayName;
    private boolean isSubSection;
    private boolean isUserPreffered;

    public SectionBasicData(boolean isUserPreffered, String personaliseSecId, String displayName, boolean isSubSection, String parentSecId) {
        this.isUserPreffered = isUserPreffered;
        this.personaliseSecId = personaliseSecId;
        this.displayName = displayName;
        this.isSubSection = isSubSection;
        this.parentSecId = parentSecId;
    }

    public String getPersonaliseSecId() {
        return personaliseSecId;
    }

    public String getDisplayName() {
        return displayName;
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
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof SectionBasicData)) return false;
        SectionBasicData otherStudent = (SectionBasicData) obj;
        return otherStudent.personaliseSecId.equals(this.personaliseSecId);
    }
}
