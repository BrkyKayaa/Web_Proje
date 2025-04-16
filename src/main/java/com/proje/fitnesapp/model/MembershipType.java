package com.proje.fitnesapp.model;

public enum MembershipType {
    POOL_ONLY("Havuz Üyeliği"),
    FITNESS_ONLY("Fitness Üyeliği"),
    BOTH("Gold Üyelik");

    private final String displayName;

    MembershipType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
