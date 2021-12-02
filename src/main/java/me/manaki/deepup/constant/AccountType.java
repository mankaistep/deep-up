package me.manaki.deepup.constant;

public enum AccountType {

    DEFAULT,
    GOOGLE;

    public static AccountType parse(String registerId) {
        return AccountType.valueOf(registerId.toUpperCase());
    }

}
