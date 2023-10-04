package pl.adrianpacholak.lessonservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.dto.RegistrationResponse;
import pl.adrianpacholak.lessonservice.service.RegistrationService;

@RestController
@RequestMapping("/lessons/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void openRegistration(@Valid @RequestBody RegistrationRequest request) {
        registrationService.openRegistration(request);
    }

    @GetMapping
    public Page<RegistrationResponse> getRegistrations(@RequestHeader("username") String username, Pageable pageable) {
        return registrationService.getRegistrations(username, pageable);
    }
}
