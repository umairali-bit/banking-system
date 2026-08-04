package com.umair.banking.security.dto.response;


import java.util.Set;

public record UserResponse(

        Long id,
        String username,
        String email,
        Set<String> roles

) {
}
