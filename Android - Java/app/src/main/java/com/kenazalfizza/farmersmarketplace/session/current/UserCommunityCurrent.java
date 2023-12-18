package com.kenazalfizza.farmersmarketplace.session.current;

import com.google.gson.annotations.SerializedName;
import com.kenazalfizza.farmersmarketplace.model.CommunityMember;

import java.util.ArrayList;

public class UserCommunityCurrent {

    @SerializedName("community_id")
    String communityId;

    @SerializedName("community_name")
    String communityName;

    @SerializedName("community_province")
    String communityProvince;

    @SerializedName("community_members")
    ArrayList<CommunityMember> communityMembers;
}
