package com.kenazalfizza.farmersmarketplace.model;

import com.kenazalfizza.farmersmarketplace.CryptoHash;

public class UserModel {

    private static String userId;

    private static String userName;

    private static String userEmail;

    private static String userPhone;

    private static String userPassword;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserModel.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserModel.userName = userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        UserModel.userEmail = userEmail;
    }

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        UserModel.userPhone = userPhone;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static void setUserPassword(String userPassword) {
        CryptoHash cryptoHash = new CryptoHash(userPassword);
        userPassword = cryptoHash.run();

        UserModel.userPassword = userPassword;
    }

}
