package com.example.oldbooks.extras;

public class Enums {
    public enum BookCondition {
        NEW,
        USED
    }
    public enum BookCategory {
        ACADEMIC,
        GENERAL
    }
    public enum UserStatus {
        ACTIVE,
        NONACTIVE,
        SUSPENDED,
    }
    public enum BidStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
    public enum DialogType {
        ACKNOWLEDGEMENT,
        CONFIRMATION,
        INFORMATION,
        WARNING,
        NOINTERNET,
    }
}
