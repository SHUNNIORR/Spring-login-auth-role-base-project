package shunnior.turnapp.service.dto;

public record AssignEmployeeResponse(
        String message,
        String employee,
        Integer serviceId
) {
}
