package com.skdwa.tcp.validate;

import com.google.common.base.Strings;
import com.skdwa.tcp.validate.ValidationStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsValidator {

    private static final String ILLEGAL_CHARACTERS = "(?=@{3})|(?=\\${3})";

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

    public static ValidationStatus checkPostMessage(String subject, String content){
        if(Strings.isNullOrEmpty(subject)){
            return new ValidationStatus(false, "Subject cannot be empty");
        }
        if(Strings.isNullOrEmpty(content)){
            return new ValidationStatus(false, "Content cannot be empty");
        }
        if(isStringContainValues(subject, ILLEGAL_CHARACTERS) || isStringContainValues(content, ILLEGAL_CHARACTERS)) {
            return new ValidationStatus(false, "Subject or content have to be alphanumeric");
        }
        if(content.length() > 256){
            return new ValidationStatus(false, "Maximum message length is 256 characters including end of lines");
        }
        if (subject.length() > 50) {
            return new ValidationStatus(false, "Maximum subject length is 50 characters including end of lines");
        }
        return new ValidationStatus(true);
    }

    public static ValidationStatus checkSubscriptionSubject(String subject){
        if(Strings.isNullOrEmpty(subject)){
            return new ValidationStatus(false, "Subject cannot be empty");
        }
        if(isStringContainValues(subject, ILLEGAL_CHARACTERS)){
            return new ValidationStatus(false, "Subject have to be alphanumeric");
        }
        if (subject.length() > 50) {
            return new ValidationStatus(false, "Maximum subject length is 50 characters including end of lines");
        }
        return new ValidationStatus(true);
    }

    private static ValidationStatus checkContainsSeparator(String field) {
        if (field.contains("@@@")) {
            return new ValidationStatus(false, "Cannot contains \"@@@\"");
        } else {
            return new ValidationStatus(true);
        }
    }

    private static boolean isStringContainValues(String string, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
