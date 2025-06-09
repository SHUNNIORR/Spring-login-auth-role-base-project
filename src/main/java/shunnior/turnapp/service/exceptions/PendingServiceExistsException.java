package shunnior.turnapp.service.exceptions;

import org.springframework.http.HttpStatus;
import shunnior.turnapp.utils.globalExceptionHandler.ApiException;

public class PendingServiceExistsException extends ApiException {
    public PendingServiceExistsException() {
        super("Ya existe un servicio pendiente creado por este usuario.", HttpStatus.CONFLICT);
    }
}
