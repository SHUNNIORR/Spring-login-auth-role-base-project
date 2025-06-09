package shunnior.turnapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import shunnior.turnapp.history.ServiceHistory;
import shunnior.turnapp.history.ServiceHistoryRepository;
import shunnior.turnapp.service.dto.AssignEmployeeResponse;
import shunnior.turnapp.service.dto.CreateServiceRequest;
import shunnior.turnapp.service.dto.ServiceResponse;
import shunnior.turnapp.service.exceptions.*;
import shunnior.turnapp.user.User;
import shunnior.turnapp.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceImpl {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceHistoryRepository serviceHistoryRepository;
    @Transactional
    public ResponseEntity<ServiceResponse> createService(CreateServiceRequest serviceRequest, User createdBy){
        boolean existsPending = serviceRepository.existsByCreatedByAndStatus(createdBy, ServiceStatus.PENDING);

        if (existsPending) {
            throw new PendingServiceExistsException();
        }

        Service newService = Service.builder()
                .description(serviceRequest.description())
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .assignedTo(null)
                .status(ServiceStatus.PENDING)
                .build();
        Service createdService = serviceRepository.save(newService);

        ServiceResponse response = new ServiceResponse(
                createdService.getId(),
                createdService.getDescription(),
                createdBy.getEmail(),
                createdService.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<AssignEmployeeResponse> assignRandomEmployee(@PathVariable Integer serviceId) {
        List<User> availableUsers = userRepository.findAvailableEmployees();

        if (availableUsers.isEmpty()) {
            throw new NoAvailableEmployeesException();
        }

        User selectedUser = availableUsers.get(new Random().nextInt(availableUsers.size()));
        selectedUser.setBusy(true); // marcar como ocupado
        userRepository.save(selectedUser);

        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            throw new ServiceNotFoundException(serviceId);
        }

        Service service = optionalService.get();
        service.setAssignedTo(selectedUser);
        serviceRepository.save(service);

        ServiceHistory serviceHistory = ServiceHistory.builder()
                .service(service)
                .employee(selectedUser)
                .assignedAt(LocalDateTime.now())
                .build();

        serviceHistoryRepository.save(serviceHistory);

        AssignEmployeeResponse assignEmployeeResponse = new AssignEmployeeResponse(
                "Empleado asignado correctamente",
                selectedUser.getEmail(),
                service.getId()
        );
        return ResponseEntity.ok(assignEmployeeResponse);
    }

    @Transactional
    public ResponseEntity<String> closeService(@PathVariable Integer serviceId, User loggedUser){
        Optional<Service> optionalService = this.serviceRepository.findById(serviceId);

        if (optionalService.isEmpty()) {
            throw new ServiceNotFoundException(serviceId);
        }

        Service serviceToClose = optionalService.get();
        System.out.println("ID del asignado al servicio: "+serviceToClose.getAssignedTo().getId());
        System.out.println("Id del loggueado: "+ loggedUser.getId());
        if (serviceToClose.getAssignedTo() == null ||
                !serviceToClose.getAssignedTo().getId().equals(loggedUser.getId())) {
            throw new UserNotAssignToServiceException(loggedUser.getEmail());
        }

        if (serviceToClose.getStatus() == ServiceStatus.COMPLETED) {
            throw new ServiceAlreadyCompletedException(serviceId);
        }

        serviceToClose.setStatus(ServiceStatus.COMPLETED);
        serviceRepository.save(serviceToClose);

        User assignedUser = serviceToClose.getAssignedTo();
        assignedUser.setBusy(false);
        userRepository.save(assignedUser);

        return ResponseEntity.ok("Servicio finalizado con exito.");
    }
}
