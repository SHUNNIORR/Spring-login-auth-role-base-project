package shunnior.turnapp.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("user_name")
        String username,

        @JsonProperty("email")
        String email
) {
}