package com.starmediadev.plugins.starterritories.objects.flag;

public enum FlagValue {
    ALLOW, DENY, RESTRICTED, DISABLED, UNDEFINED;
    
    public static FlagValue getPriorityValue(FlagValue value1, FlagValue value2) {
        if (value1 == value2) {
            return value1;
        } else if (value1 == ALLOW && value2 == DENY) {
            return DENY;
        } else if (value1 == DENY && value2 == ALLOW) {
            return DENY;
        } else if (value1 == UNDEFINED) {
            return value2;
        } else if (value2 == UNDEFINED) {
            return value1;
        }
        return UNDEFINED;
    }
}
