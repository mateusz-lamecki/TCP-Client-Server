package com.skdwa.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {
    private final boolean isOk;
    private String message;
}
