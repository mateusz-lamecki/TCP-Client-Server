package com.skdwa.tcp;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

public class FieldsValidator {

    public static ValidationStatus hostValidator(String host) {
        if (Strings.isNullOrEmpty(host)) {
            return new ValidationStatus(false, "Cannot be null or empty");
        }
        if (host.contains(":")) {
            return new ValidationStatus(false, "Cannot contains \":\"");
        }
        return checkContainsSeparator(host);
    }

    public static ValidationStatus portValidator(String port) {
        if (Strings.isNullOrEmpty(port)) {
            return new ValidationStatus(false, "Cannot be null or empty");
        }
        if (!StringUtils.isNumeric(port)) {
            return new ValidationStatus(false, "Have to be numeric");
        }
        if (port.contains(":")) {
            return new ValidationStatus(false, "Cannot contains \":\"");
        }
        return checkContainsSeparator(port);
    }

    /**
     * Checks that field is not null, not empty and do not contains "@@@" separator;
     *
     * @param field field to check
     * @return {@link ValidationStatus} witch isValid field true when field is valid, otherwise isValid field false with
     * error message.
     */
    public static ValidationStatus commonValidator(String field) {
        if (Strings.isNullOrEmpty(field)) {
            return new ValidationStatus(false, "Cannot be null or empty");
        }
        if (!StringUtils.isAlphanumeric(field)) {
            return new ValidationStatus(false, "Have to be alphanumeric");
        }
        return checkContainsSeparator(field);
    }

    private static ValidationStatus checkContainsSeparator(String field) {
        if (field.contains("@@@")) {
            return new ValidationStatus(false, "Cannot contains \"@@@\"");
        } else {
            return new ValidationStatus(true);
        }
    }
}
