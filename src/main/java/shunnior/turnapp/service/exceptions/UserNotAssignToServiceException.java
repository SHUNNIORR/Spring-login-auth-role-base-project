package shunnior.turnapp.service.exceptions;

import org.springframework.http.HttpStatus;
import shunnior.turnapp.utils.globalExceptionHandler.ApiException;

public class UserNotAssignToServiceException extends ApiException {
    public UserNotAssignToServiceException(String userEmail) {
        super("El usuario: "+ userEmail +" no está autorizado para finalizar el servicio", HttpStatus.FORBIDDEN);
    }
}