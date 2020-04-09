package com.netoperation.db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Set;

@Entity
public class TableMP {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String cycleUniqueId;

    private String cycleName;

    private long networkCurrentTimeInMilli;

    private long allowedTimeInSecs;
    private long expiryTimeInMillis;
    private long startTimeInMillis;

    private int allowedArticleCounts;

    private boolean isMpFeatureEnabled;
    private boolean isTaboolaNeeded;
    private boolean isMpBannerNeeded;

    private String mpBannerMsg;

    private boolean showFullAccessBtn;
    private String fullAccessBtnName;

    private boolean showSignInBtn;
    private String signInBtnName;
    private String signInBtnNameBoldWord;

    private boolean showSignUpBtn;
    private String signUpBtnName;
    private String signUpBtnNameBoldWord;

    private String nonSignInBlockerTitle;
    private String nonSignInBlockerDescription;

    private String expiredUserBlockerTitle;
    private String expiredUserBlockerDescription;

    private Set<String> readArticleIds = new HashSet<>();

    public Set<String> getReadArticleIds() {
        return readArticleIds;
    }

    public long getAllowedTimeInSecs() {
        return allowedTimeInSecs;
    }

    public void setAllowedTimeInSecs(long allowedTimeInSecs) {
        this.allowedTimeInSecs = allowedTimeInSecs;
    }

    public long getExpiryTimeInMillis() {
        return expiryTimeInMillis;
    }

    public void setExpiryTimeInMillis(long expiryTimeInMillis) {
        this.expiryTimeInMillis = expiryTimeInMillis;
    }

    public void setReadArticleIds(Set<String> readArticleIds) {
        this.readArticleIds = readArticleIds;
    }

    public void addReadArticleId(String readArticleId) {
        this.readArticleIds.add(readArticleId);
    }

    public void clearArticleCounts() {
        this.readArticleIds.clear();
    }


    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getCycleUniqueId() {
        return cycleUniqueId;
    }

    public void setCycleUniqueId(String uniqueId) {
        this.cycleUniqueId = uniqueId;
    }

    public int getAllowedArticleCounts() {
        return allowedArticleCounts;
    }

    public void setAllowedArticleCounts(int allowedArticleCounts) {
        this.allowedArticleCounts = allowedArticleCounts;
    }

    public boolean isMpFeatureEnabled() {
        return isMpFeatureEnabled;
    }

    public void setMpFeatureEnabled(boolean mpFeatureEnabled) {
        isMpFeatureEnabled = mpFeatureEnabled;
    }

    public boolean isTaboolaNeeded() {
        return isTaboolaNeeded;
    }

    public void setTaboolaNeeded(boolean taboolaNeeded) {
        isTaboolaNeeded = taboolaNeeded;
    }

    public boolean isMpBannerNeeded() {
        return isMpBannerNeeded;
    }

    public void setMpBannerNeeded(boolean mpBannerNeeded) {
        isMpBannerNeeded = mpBannerNeeded;
    }

    public String getMpBannerMsg() {
        return mpBannerMsg;
    }

    public void setMpBannerMsg(String mpBannerMsg) {
        this.mpBannerMsg = mpBannerMsg;
    }

    public boolean isShowFullAccessBtn() {
        return showFullAccessBtn;
    }

    public void setShowFullAccessBtn(boolean showFullAccessBtn) {
        this.showFullAccessBtn = showFullAccessBtn;
    }

    public String getFullAccessBtnName() {
        return fullAccessBtnName;
    }

    public void setFullAccessBtnName(String fullAccessBtnName) {
        this.fullAccessBtnName = fullAccessBtnName;
    }

    public boolean isShowSignInBtn() {
        return showSignInBtn;
    }

    public void setShowSignInBtn(boolean showSignInBtn) {
        this.showSignInBtn = showSignInBtn;
    }

    public String getSignInBtnName() {
        return signInBtnName;
    }

    public void setSignInBtnName(String signInBtnName) {
        this.signInBtnName = signInBtnName;
    }

    public boolean isShowSignUpBtn() {
        return showSignUpBtn;
    }

    public void setShowSignUpBtn(boolean showSignUpBtn) {
        this.showSignUpBtn = showSignUpBtn;
    }

    public String getSignUpBtnName() {
        return signUpBtnName;
    }

    public void setSignUpBtnName(String signUpBtnName) {
        this.signUpBtnName = signUpBtnName;
    }

    public String getNonSignInBlockerTitle() {
        return nonSignInBlockerTitle;
    }

    public void setNonSignInBlockerTitle(String nonSignInBlockerTitle) {
        this.nonSignInBlockerTitle = nonSignInBlockerTitle;
    }

    public String getNonSignInBlockerDescription() {
        return nonSignInBlockerDescription;
    }

    public void setNonSignInBlockerDescription(String nonSignInBlockerDescription) {
        this.nonSignInBlockerDescription = nonSignInBlockerDescription;
    }

    public String getExpiredUserBlockerTitle() {
        return expiredUserBlockerTitle;
    }

    public void setExpiredUserBlockerTitle(String expiredUserBlockerTitle) {
        this.expiredUserBlockerTitle = expiredUserBlockerTitle;
    }

    public String getExpiredUserBlockerDescription() {
        return expiredUserBlockerDescription;
    }

    public void setExpiredUserBlockerDescription(String expiredUserBlockerDescription) {
        this.expiredUserBlockerDescription = expiredUserBlockerDescription;
    }

    public String getSignInBtnNameBoldWord() {
        return signInBtnNameBoldWord;
    }

    public void setSignInBtnNameBoldWord(String signInBtnNameBoldWord) {
        this.signInBtnNameBoldWord = signInBtnNameBoldWord;
    }

    public String getSignUpBtnNameBoldWord() {
        return signUpBtnNameBoldWord;
    }

    public void setSignUpBtnNameBoldWord(String signUpBtnNameBoldWord) {
        this.signUpBtnNameBoldWord = signUpBtnNameBoldWord;
    }

    public long getNetworkCurrentTimeInMilli() {
        return networkCurrentTimeInMilli;
    }

    public void setNetworkCurrentTimeInMilli(long networkCurrentTimeInMilli) {
        this.networkCurrentTimeInMilli = networkCurrentTimeInMilli;
    }


    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public void setStartTimeInMillis(long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
    }
}
