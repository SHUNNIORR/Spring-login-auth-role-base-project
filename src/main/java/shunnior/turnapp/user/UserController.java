package shunnior.turnapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shunnior.turnapp.auth.controller.RegisterRequest;
import shunnior.turnapp.auth.controller.TokenResponse;
import shunnior.turnapp.auth.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

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

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TokenResponse> registerAdmin(@RequestBody RegisterRequest request) {
        final TokenResponse response = authService.register(request,true);
        return ResponseEntity.ok(response);
    }

}
