package pl.adrianpacholak.facultyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.service.FacultyService;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFaculty(@Valid @RequestBody FacultyRequest request) {
        facultyService.createFaculty(request);
    }
}
