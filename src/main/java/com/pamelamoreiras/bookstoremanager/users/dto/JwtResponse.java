package com.pamelamoreiras.bookstoremanager.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private final String token;
}
