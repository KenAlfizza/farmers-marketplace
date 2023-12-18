package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;

public class CommunityMember {
    @SerializedName("user_id")
    String userId;

    @SerializedName("user_name")
    String userName;

    @SerializedName("user_role")
    String userRole;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }
}
