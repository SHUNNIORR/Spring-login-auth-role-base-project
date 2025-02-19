package shunnior.turnapp.auth.controller;

public record AuthRequest(
        String email,
        String password
) {
}