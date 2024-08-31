package com.example.authenticationjwt.enums;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {
    USER("USER"),
    ADMIN("ADMIN")
    ;

    Role(String name) {
        this.name = name;
    }

    String name;
}
