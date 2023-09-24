package pl.adrianpacholak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.service.TeacherService;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        teacherService.createTeacher(teacherDTO);
    }
}
