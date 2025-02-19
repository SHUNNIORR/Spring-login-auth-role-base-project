package shunnior.turnapp.auth.service.exceptions;

public enum UserEntityExceptionType {
    USER_ALREADY_EXISTS("Ya existe un usuario creado con ese email"),
    NOT_ALLOWED_USER("Este usuario no tiene los permisos necesarios."),
    USER_NOT_FOUND("El usuario no existe");

    private final String message;

    UserEntityExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}