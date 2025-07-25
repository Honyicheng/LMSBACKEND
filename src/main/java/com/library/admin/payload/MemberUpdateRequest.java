package com.library.admin.payload;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String username;
    private String email;
}
