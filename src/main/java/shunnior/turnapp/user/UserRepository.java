package shunnior.turnapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<List<User>> findByIsBusyFalseAndRoles(String role);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = 'ROLE_USER' AND u.isBusy = false")
    List<User> findAvailableEmployees();
}
