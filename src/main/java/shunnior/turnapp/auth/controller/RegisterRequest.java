package shunnior.turnapp.auth.controller;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}