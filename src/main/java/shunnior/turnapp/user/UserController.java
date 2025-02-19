package shunnior.turnapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shunnior.turnapp.auth.controller.RegisterRequest;
import shunnior.turnapp.auth.controller.TokenResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<UserResponse> changePassword() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getName(), user.getEmail()))
                .toList();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER')")
    //TODO: ver cómo se puede generar una excepción cuando un usuario no cuente con los permisos adecuados
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Bienvenido al dashboard de Admin");
    }

}
