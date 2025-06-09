package shunnior.turnapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shunnior.turnapp.service.dto.AssignEmployeeResponse;
import shunnior.turnapp.service.dto.CreateServiceRequest;
import shunnior.turnapp.service.dto.ServiceResponse;
import shunnior.turnapp.user.User;

@RestController
@RequestMapping("/api/admin/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceImpl serviceImplement;
    @PostMapping()
    public ResponseEntity<ServiceResponse> createService(
            @RequestBody CreateServiceRequest request,
            @AuthenticationPrincipal User user
    ){
        return this.serviceImplement.createService(request, user);
    }

    @PostMapping("/assign/{serviceId}")
    public ResponseEntity<AssignEmployeeResponse> assignRandomEmployee(@PathVariable Integer serviceId) {
        return this.serviceImplement.assignRandomEmployee(serviceId);
    }

    @PostMapping("/close/{serviceId}")
    public ResponseEntity<String> closeService(
            @PathVariable Integer serviceId,
            @AuthenticationPrincipal User user
    ){
        return this.serviceImplement.closeService(serviceId, user);
    }
}
