package shunnior.turnapp.service;

import org.springframework.data.jpa.repository.JpaRepository;
import shunnior.turnapp.user.User;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    //Optional<List<User>> findByIsBusyFalseAndRole(String role);
    boolean existsByCreatedByAndStatus(User createdBy, ServiceStatus status);
    boolean existsByIdAndAssignedTo_Id(Integer serviceId, Integer userId);
}
