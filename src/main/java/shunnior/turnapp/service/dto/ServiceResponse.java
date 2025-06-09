package shunnior.turnapp.service.dto;

public record ServiceResponse(
        Integer id,
        String description,
        String createdByEmail,
        String status
) {}
