package com.netoperation.model;

import android.text.TextUtils;

import java.util.ArrayList;

public class UserProfile {

    private String emailId;
    private String contact;
    private String redirectUrl;
    private String userId;
    private String reason;

    private String authors_preference;
    private String cities_preference;
    private String topics_preference;

    private String address_state;
    private String address_pincode;
    private String address_house_no;
    private String address_city;
    private String address_street;
    private String address_fulllname;
    private String address_landmark;
    private String address_default_option;
    private String address_location;
    private String Profile_Country;
    private String Profile_State;

    private String FullName;
    private String Gender;
    private String DOB;

    private String isNew;
    private String fid;
    private String tid;
    private String gid;
    private String loginSource;
    private String nextRenewalDate;
    private String userMigrated;

    private boolean hasSubscribedPlan;
    private boolean hasFreePlan;


    public String getUserEmailOrContact() {
        if(emailId != null && !TextUtils.isEmpty(emailId)) {
            return emailId;
        }
        return contact;
    }

    private ArrayList<TxnDataBean> userPlanList;

    public String getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(String nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public String getUserMigrated() {
        return userMigrated;
    }

    public void setUserMigrated(String userMigrated) {
        this.userMigrated = userMigrated;
    }

    public ArrayList<TxnDataBean> getUserPlanList() {
        return userPlanList;
    }

    public void addUserPlanList(TxnDataBean planInfoData) {
        if(this.userPlanList == null) {
            this.userPlanList = new ArrayList<>();
        }
        this.userPlanList.add(planInfoData);
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuthors_preference() {
        return authors_preference;
    }

    public void setAuthors_preference(String authors_preference) {
        this.authors_preference = authors_preference;
    }

    public String getCities_preference() {
        return cities_preference;
    }

    public void setCities_preference(String cities_preference) {
        this.cities_preference = cities_preference;
    }

    public String getTopics_preference() {
        return topics_preference;
    }

    public void setTopics_preference(String topics_preference) {
        this.topics_preference = topics_preference;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public String getAddress_pincode() {
        return address_pincode;
    }

    public void setAddress_pincode(String address_pincode) {
        this.address_pincode = address_pincode;
    }

    public String getAddress_house_no() {
        return address_house_no;
    }

    public void setAddress_house_no(String address_house_no) {
        this.address_house_no = address_house_no;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_fulllname() {
        return address_fulllname;
    }

    public void setAddress_fulllname(String address_fulllname) {
        this.address_fulllname = address_fulllname;
    }

    public String getAddress_landmark() {
        return address_landmark;
    }

    public void setAddress_landmark(String address_landmark) {
        this.address_landmark = address_landmark;
    }

    public String getAddress_default_option() {
        return address_default_option;
    }

    public void setAddress_default_option(String address_default_option) {
        this.address_default_option = address_default_option;
    }

    public String getAddress_location() {
        return address_location;
    }

    public void setAddress_location(String address_location) {
        this.address_location = address_location;
    }

    public String getProfile_Country() {
        return Profile_Country;
    }

    public void setProfile_Country(String profile_Country) {
        Profile_Country = profile_Country;
    }

    public String getProfile_State() {
        return Profile_State;
    }

    public void setProfile_State(String profile_State) {
        Profile_State = profile_State;
    }

    public String getFullName() {
        if(FullName != null && !TextUtils.isEmpty(FullName)) {
            return FullName;
        }
        if(emailId != null && !TextUtils.isEmpty(emailId)) {
            return emailId;
        }
        if(contact != null && !TextUtils.isEmpty(contact)) {
            return contact;
        }
        return FullName;
    }

    public String getFullNameForProfile() {
        if(FullName != null && !TextUtils.isEmpty(FullName)) {
            return FullName;
        }
        return "User";
    }

    public String getFullNameForPersonalInfo() {
        if(FullName != null && !TextUtils.isEmpty(FullName)) {
            return FullName;
        }
        return "";
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    public boolean isHasSubscribedPlan() {
        return hasSubscribedPlan;
    }

    public void setHasSubscribedPlan(boolean hasSubscribedPlan) {
        this.hasSubscribedPlan = hasSubscribedPlan;
    }

    public boolean isHasFreePlan() {
        return hasFreePlan;
    }

    public void setHasFreePlan(boolean hasFreePlan) {
        this.hasFreePlan = hasFreePlan;
    }

    public String getAddressFormatted() {

        String houseNo = getAddress_house_no().trim();
        String street = getAddress_street().trim();
        String landmark = getAddress_landmark().trim();
        String city = getAddress_city().trim();
        String pincode = getAddress_pincode().trim();
        String state = getAddress_state().trim();

        String addressFormatted = "";

        if(houseNo != null && !TextUtils.isEmpty(houseNo)) {
            addressFormatted = houseNo;
        }

        if(addressFormatted == null) {
            addressFormatted = "";
        }

        if(street != null && !TextUtils.isEmpty(street)) {
            if(addressFormatted.contains(houseNo)) {
                addressFormatted += ", " + street;
            } else {
                addressFormatted += street;
            }
        }
        if(landmark != null && !TextUtils.isEmpty(landmark)) {
            if(addressFormatted.contains(houseNo) || addressFormatted.contains(street)) {
                addressFormatted += ", " + landmark;
            } else {
                addressFormatted += landmark;
            }
        }
        if(city != null && !TextUtils.isEmpty(city)) {
            if(addressFormatted.contains(houseNo) || addressFormatted.contains(street) || addressFormatted.contains(landmark)) {
                addressFormatted += ", " + city;
            } else {
                addressFormatted += city;
            }
        }
        if(pincode != null && !TextUtils.isEmpty(pincode)) {
            if(addressFormatted.contains(houseNo) || addressFormatted.contains(street)
                    || addressFormatted.contains(landmark) || addressFormatted.contains(city)) {
                addressFormatted += ", " + pincode;
            } else {
                addressFormatted += pincode;
            }
        }
        if(state != null && !TextUtils.isEmpty(state)) {
            if(addressFormatted.contains(houseNo) || addressFormatted.contains(street)
                    || addressFormatted.contains(landmark) || addressFormatted.contains(city) || addressFormatted.contains(pincode)) {
                addressFormatted += ", " + state;
            } else {
                addressFormatted += ", " + state;
            }
        }

        return addressFormatted;
    }

}
