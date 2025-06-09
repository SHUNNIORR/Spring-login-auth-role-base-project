package shunnior.turnapp.service.exceptions;

import org.springframework.http.HttpStatus;
import shunnior.turnapp.utils.globalExceptionHandler.ApiException;

public class NoAvailableEmployeesException extends ApiException {
    public NoAvailableEmployeesException() {
        super("No hay empleados disponibles actualmente", HttpStatus.NOT_FOUND);
    }
}
