package com.skdwa.tcp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {
    public static final String CONNECTION_ERROR = "Connection problem, restart app or try later";
    public static final String UNKNOWN_ERROR = "There was an error. Contact admin";
}
