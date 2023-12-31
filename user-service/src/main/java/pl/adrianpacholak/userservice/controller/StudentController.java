package pl.adrianpacholak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.dto.StudentResponse;
import pl.adrianpacholak.userservice.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        studentService.createStudent(studentDTO);
    }

    @GetMapping("/exists/username")
    public Map<String, Boolean> checkStudentExists(@RequestParam String username) {
        return Collections.singletonMap("exists", studentService.checkStudentExists(username));
    }

    @GetMapping("/{studentId}")
    public StudentResponse getStudent(@PathVariable Integer studentId) {
        return studentService.getStudent(studentId);
    }

    @PostMapping("/ids")
    public List<StudentResponse> getStudentsByIds(@RequestBody List<Integer> studentsIds) {
        return studentService.getStudentsByIds(studentsIds);
    }
}
