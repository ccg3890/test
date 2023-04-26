package com.mktback.application.domain.enumeration;

/**
 * The CategoryGrade enumeration.
 */
public enum CategoryGrade {
    A("All"),
    B("Anaylist"),
    C("Public");

    private final String value;

    CategoryGrade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
