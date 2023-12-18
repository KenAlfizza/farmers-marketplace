package com.kenazalfizza.farmersmarketplace.session.current;

public class UserCurrent {

    private static String userId;

    private static String userName;

    private static String userEmail;

    private static String userPhone;


    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserCurrent.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserCurrent.userName = userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        UserCurrent.userEmail = userEmail;
    }

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        UserCurrent.userPhone = userPhone;
    }

}
