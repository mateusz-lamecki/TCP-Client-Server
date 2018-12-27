package com.skdwa.tcp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class ValidationStatus {
    private final boolean isValid;
    private String errorMessage;
}
