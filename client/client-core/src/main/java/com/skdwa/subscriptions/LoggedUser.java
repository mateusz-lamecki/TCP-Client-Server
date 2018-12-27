package com.skdwa.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoggedUser {
    private String username;
    private String token;
}
