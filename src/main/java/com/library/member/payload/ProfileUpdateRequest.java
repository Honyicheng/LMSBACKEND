package com.library.member.payload;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String username;
    private String email;
    private String password;
}
