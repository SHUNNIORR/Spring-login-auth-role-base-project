package shunnior.turnapp.service.exceptions;

import org.springframework.http.HttpStatus;
import shunnior.turnapp.utils.globalExceptionHandler.ApiException;

public class ServiceNotFoundException extends ApiException {
    public ServiceNotFoundException(Integer serviceId) {
        super("Servicio con ID " + serviceId + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
