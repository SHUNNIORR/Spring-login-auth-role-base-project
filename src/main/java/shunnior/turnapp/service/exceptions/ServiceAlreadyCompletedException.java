package shunnior.turnapp.service.exceptions;

public class ServiceAlreadyCompletedException extends RuntimeException {
    public ServiceAlreadyCompletedException(Integer serviceId) {
        super("El servicio con ID " + serviceId + " ya está finalizado.");
    }
}

